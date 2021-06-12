import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class SQLqueryMenu extends JFrame
{
    private JTextField hostField = new JTextField();
    private JTextField postField = new JTextField();
    private JTextField dbField = new JTextField();
    private JTextField userField = new JTextField();
    private JPasswordField pwdField = new JPasswordField();
    private JButton connectButton = new JButton("连接");
    public SQLqueryMenu(){
        this.setBounds(200, 100, 550, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("新建连接");
        this.setLayout(null);
        this.setResizable(false);
        final int xTitle = 120;
        final int xBox = 220;
        final int boxWidth = 150;
        JLabel title = new JLabel("配置Spark SQL连接信息");
        title.setBounds(180, 30, 200, 20);
        JLabel host = new JLabel("主机：");
        host.setBounds(xTitle, 90, 100, 20);
        JLabel post = new JLabel("端口：");
        post.setBounds(xTitle, 120, 100, 20);
        JLabel database = new JLabel("初始数据库：");
        database.setBounds(xTitle, 150, 100, 20);
        JLabel userName = new JLabel("用户名：");
        userName.setBounds(xTitle, 180, 100, 20);
        JLabel pwdTest = new JLabel("密码：");
        pwdTest.setBounds(xTitle, 210, 100, 20);


        hostField.setBounds(xBox, 90, 180, 20);
        postField.setBounds(xBox, 120, 50, 20);
        dbField.setBounds(xBox, 150, boxWidth, 20);
        userField.setBounds(xBox, 180, boxWidth, 20);
        pwdField.setBounds(xBox, 210, boxWidth, 20);

        connectButton.setBounds(xBox, 250, 100, 30);
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == connectButton) {// 判断触发源是否为按钮
                    String url = hostField.getText() + ":" + postField.getText() + "/" + dbField.getText();
                    String userName = userField.getText();
                    String userPassword = pwdField.getPassword().toString();
                    sparkSQL s = new sparkSQL(url, userName, userPassword);
//                    sparkSQL s = new sparkSQL("jdbc:hive2://bigdata115.depts.bingosoft.net:22115/user13_db","user13","pass@bingo13");
                    JOptionPane.showMessageDialog(null, "连接成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                    dispose();
                    final SQLqueryPage ui2 = new SQLqueryPage();
//                    ui2.SQLqueryPage("jdbc:hive2://bigdata115.depts.bingosoft.net:22115/user13_db","user13","pass@bingo13");
                    ui2.SQLqueryPage(url, userName, userPassword);
                }
            }
        });// 加入动作监听
        this.add(title);
        this.add(host);
        this.add(post);
        this.add(database);
        this.add(userName);
        this.add(pwdTest);
        this.add(hostField);
        this.add(postField);
        this.add(dbField);
        this.add(userField);
        this.add(pwdField);
        this.add(connectButton);
    }
    public static void main(String[] agrs)
    {
        SQLqueryMenu ui=new SQLqueryMenu();
        ui.setVisible(true);
//            Demo.demo();
//      val url = "jdbc:hive2://bigdata115.depts.bingosoft.net:22115/user13_db"

    }
}
