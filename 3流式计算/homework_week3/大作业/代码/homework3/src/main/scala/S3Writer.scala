import java.io.{File, FileWriter}
import java.util.{Timer, TimerTask}

import com.bingocloud.auth.BasicAWSCredentials
import com.bingocloud.{ClientConfiguration, Protocol}
import com.bingocloud.services.s3.AmazonS3Client
import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.common.io.OutputFormat
import org.apache.flink.configuration.Configuration

class S3Writer(accessKey: String, secretKey: String, endpoint: String, bucket: String, keyPrefix: String, period: Int) extends OutputFormat[String] {
  var timer: Timer = _
  var file: File = _
  var fileWriter: FileWriter = _
  var length = 0L
  var amazonS3: AmazonS3Client = _

  def upload: Unit = {
    this.synchronized {
      if (length > 0) {
        fileWriter.close()
        val targetKey = keyPrefix + System.nanoTime()
        amazonS3.putObject(bucket, targetKey, file)
        println("开始上传文件：%s 至 %s 桶的 %s 目录下".format(file.getAbsoluteFile, bucket, targetKey))
        file = null
        fileWriter = null
        length = 0L
      }
    }
  }

  override def configure(configuration: Configuration): Unit = {
    timer = new Timer("S3Writer")
    timer.schedule(new TimerTask() {
      def run(): Unit = {
        upload
      }
    }, 1000, period)
    val credentials = new BasicAWSCredentials(accessKey, secretKey)
    val clientConfig = new ClientConfiguration()
    clientConfig.setProtocol(Protocol.HTTP)
    amazonS3 = new AmazonS3Client(credentials, clientConfig)
    amazonS3.setEndpoint(endpoint)

  }

  override def open(taskNumber: Int, numTasks: Int): Unit = {

  }

  override def writeRecord(it: String): Unit = {
    this.synchronized {
      if (StringUtils.isNoneBlank(it)) {
        if (fileWriter == null) {
          file = new File(System.nanoTime() + ".txt")
          fileWriter = new FileWriter(file, true)
        }
        fileWriter.append(it + "\n")
        length += it.length
        fileWriter.flush()
      }
    }
  }


  override def close(): Unit = {
    fileWriter.flush()
    fileWriter.close()
    timer.cancel()
  }
}

