import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class Main {
    //输入的kafka主题名称
    private static String inputTopic = "xh_buy_ticket_2";
    //kafka地址
    private static String bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037";
    public static void main(String[] args) throws Exception{
        //构建流执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //kafka
        Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", bootstrapServers);
        kafkaProperties.put("group.id", UUID.randomUUID().toString());
        kafkaProperties.put("auto.offset.reset", "earliest");
        kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        DataStreamSource<String> dataStreamSource = env.addSource(new FlinkKafkaConsumer010<String>(
                inputTopic,
                new SimpleStringSchema(),
                kafkaProperties
        )).
                //单线程打印，控制台不乱序，不影响结果
                        setParallelism(1);

        //从kafka里读取数据，转换成Person对象
        DataStream<buy_record> dataStream = dataStreamSource.map(x->JSONObject.parseObject(x, buy_record.class));
        dataStream.timeWindowAll(Time.seconds(5L)).apply(
                new AllWindowFunction<buy_record, List<buy_record>, TimeWindow>() {

                    @Override
                    public void apply(TimeWindow timeWindow, Iterable<buy_record> iterable, Collector<List<buy_record>> collector) throws Exception {
                        List<buy_record> records = Lists.newArrayList(iterable);

                        if(records.size()>0){
                            System.out.println("5秒总共收到的条数："+records.size());
                            collector.collect(records);
                        }
                    }


                }
        ).addSink(new mySQLWriter());
        //打印到控制台
        //.print();


        env.execute();
    }

}