
import java.sql.ResultSetMetaData
import java.util.Properties
import Array._
class sparkSQL(url:String,userName:String,userPassword:String) {
  import java.sql.DriverManager
  val properties = new Properties()
  properties.setProperty("driverClassName", "org.apache.hive.jdbc.HiveDriver")
  properties.setProperty("user", userName)
  properties.setProperty("password", userPassword)
  val connection = DriverManager.getConnection(url, properties)
  val statement = connection.createStatement
  def showTable(): Array[String] = {
    var resultSet = statement.executeQuery("show tables")
    var tableNames = new Array[String](100)
    try {
      var a=0;
      while (resultSet.next) {
        val tableName = resultSet.getString(1)
        tableNames(a) = tableName
        a+=1
      }
      resultSet.close()
    }
    return tableNames
  }
  def showTableField(tableName:String): Array[String] = {
    var resultSet = statement.executeQuery("select*from "+tableName+" limit 10")
    var data = resultSet.getMetaData()
//    if(data.getColumnCount > 20){
//      var tableFieldNames = new Array[String](21)
//      try {
//        var a=0;
//        for(a <- 1 to 20) {
//          tableFieldNames(a-1) = data.getColumnName(a)
//        }
//        tableFieldNames(20)="..."
//        resultSet.close()
//      }
//      return tableFieldNames
//    }
//    else{
        var tableFieldNames = new Array[String](data.getColumnCount)
        try {
          var a=0;
          for(a <- 1 to data.getColumnCount) {
            tableFieldNames(a-1) = data.getColumnName(a)
          }
          resultSet.close()
        }
        return tableFieldNames
//      }
  }
  def queryRes(query:String): Array[Array[String]] = {
    //Array[Array[String]]
    var resultSet = statement.executeQuery(query)
    var dataTable = Array.ofDim[String](100, 100)
    try {
      var data=resultSet.getMetaData()
      var a =1
      for (a <- 1 to data.getColumnCount){
        if(a==1){
          dataTable(0)(a - 1)="序列\\列名"
        }
        dataTable(0)(a)=data.getColumnName(a)
      }
      var b=1
      while(resultSet.next){
        a=1
        for (a <- 1 to data.getColumnCount){
          if(a==1) {
            dataTable(b)(a - 1) = b.toString
          }
          dataTable(b)(a)=resultSet.getString(a)
        }
        b+=1
      }
      resultSet.close()
      return dataTable
    }
  }

}
