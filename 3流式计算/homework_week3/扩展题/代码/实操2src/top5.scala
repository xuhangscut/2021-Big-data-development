import java.io.File
import java.util.{Properties, UUID}
import java.io.FileInputStream

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import s3_to_kafka.{produceToKafka, readFile}
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.api.scala.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.{SlidingProcessingTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
object top5 {
  val inputTopic = "xh_buy_ticket_2"
  /**
   * kafka地址
   */
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaConsumer = new FlinkKafkaConsumer010[String](inputTopic,
      new SimpleStringSchema, kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)
    val JSONStream = inputKafkaStream.map(x =>JSON.parseObject(x))
    val destinationStream = JSONStream.map(x =>(x.getString("destination"),1))
    destinationStream.keyBy(0).window(SlidingProcessingTimeWindows.of(Time.seconds(60L), Time.seconds(10L)))
      // 计算top5城市
      .sum(1)
      .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(10L)))
      .process(new ProcessAllWindowFunction[(String, Int), String, TimeWindow] {
        override def process(context: Context, elements: Iterable[(String, Int)], out: Collector[String]): Unit = {
          val top5 = elements.toSeq
            .sortBy(-_._2)
            .take(5)
            .zipWithIndex
            .map { case ((item, sum), idx) => s"   ${idx + 1}. $item: $sum" }
            .mkString("\n")
          out.collect(("-" * 16) + "\n" + top5)
        }
      })
      .print()

    env.execute()
  }
}
