
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;


public class mySQLWriter extends RichSinkFunction<List<buy_record>> {

    private PreparedStatement ps;
    private Connection connection;
    private String username = "root";
    private String password = "xhwf979899";
    private String drive = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/university";

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //获取数据库连接，准备写入数据库
        Class.forName(drive);
        connection = DriverManager.getConnection(url, username, password);
        String sql = "insert into buy_record(buy_time, buy_address, origin, destination, username) values (?, ?, ?, ?, ?); ";
        ps = connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {
        super.close();
        //关闭并释放资源
        if(connection != null) {
            connection.close();
        }

        if(ps != null) {
            ps.close();
        }
    }

    @Override
    public void invoke(List<buy_record> records, Context context) throws Exception {
        for(buy_record record : records) {
            ps.setString(1, record.getBuy_time());
            ps.setString(2, record.getBuy_address());
            ps.setString(3, record.getOrigin());
            ps.setString(4, record.getDestination());
            ps.setString(5, record.getUsername());
            ps.addBatch();
        }

        //一次性写入
        int[] count = ps.executeBatch();
        System.out.println("成功写入Mysql数量：" + count.length);

    }
}
