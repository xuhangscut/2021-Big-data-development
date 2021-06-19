import java.sql.{Connection, DriverManager}
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object Main {
  var res=""
  //kafka参数
  val topic = "xh_university_student_1"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"
  def main(args: Array[String]): Unit = {
    val username = "root"
    val password = "xhwf979899"
    val drive = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/university"
    var connection: Connection = null
    try {
      Class.forName(drive)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("select*from student")

      while (resultSet.next()) {
        val ID = resultSet.getString("ID")
        val name = resultSet.getString("name")
        val dept_name = resultSet.getString("dept_name")
        val tot_cred = resultSet.getString("tot_cred")
        res+=ID+","+name+","+dept_name+","+tot_cred+"\n"
      }
    } catch {
      case e: Exception=> e.printStackTrace()
    } finally {
      connection.close()
    }
    produceToKafka(res)
  }

  /**
   * 把数据写入到kafka中
   *
   * @param s3Content 要写入的内容
   */
  def produceToKafka(s3Content: String): Unit = {
    val props = new Properties
    props.put("bootstrap.servers", bootstrapServers)
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val dataArr = s3Content.split("\n")
    for (s <- dataArr) {
      if (!s.trim.isEmpty) {
        val record = new ProducerRecord[String, String](topic, null, s)
        println("开始生产数据：" + s)
        producer.send(record)
      }
    }
    producer.flush()
    producer.close()
  }
}
