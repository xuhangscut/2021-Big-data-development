import javax.swing.*;
import javax.swing.JTextArea;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SQLqueryPage {
    private JFrame frame = new JFrame();
    private JButton queryBtn = new JButton("查询");
//    private JButton refreshBtn = new JButton("刷新");
    private JTextArea queryText = new JTextArea();
    private JScrollPane jsp = new JScrollPane();
    public void SQLqueryPage(String url, String userName, String userPassword){
        final sparkSQL s = new sparkSQL(url,userName,userPassword);
        frame.setBounds(200, 100, 850, 500);
        frame.setTitle("新建连接");
        frame.setLayout(null);
        JLabel info = new JLabel("当前连接下的数据库信息：");
        info.setBounds(20,20,200,20);
        int index = url.lastIndexOf('/');
        String db_name = url.substring(index+1,url.length());
        JLabel info1 = new JLabel("数据库："+db_name);
        info1.setBounds(20,40,200,20);
        JLabel info2 = new JLabel("请输入查询语句：");
        info2.setBounds(200,20,200,20);
        queryText.setBounds(200,50,600,300);
//        refreshBtn.setBounds(150,400,100,20);
        queryBtn.setBounds(700,400,100,20);

        queryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = queryText.getText();
//              String[][] dataTable =new String[100][100];
                String[][] dataTable =s.queryRes(query);
//                for(int i=0;i<100;i++){
//                    if(dataTable[i][0]==null){
//                        break;
//                    }
//                    for(int j=0;j<100;j++){
//                        if(dataTable[i][j]==null){
//                            System.out.println("|");
//                            break;
//                        }
//                        System.out.printf(dataTable[i][j]);
//                    }
//                }
                SQLqueryTable table = new SQLqueryTable();
                table.SQLqueryTable(dataTable);
            }
        });
        final SQLqueryInfo infos = new SQLqueryInfo();
        String[] items=s.showTable();
        ArrayList<String> tableNames=new ArrayList<String>();
        for(int i=0;i<100;i++){
            if(items[i]==null){
                break;
            }
            tableNames.add(items[i]);
        }
        final String[] tableName=tableNames.toArray(new String[tableNames.size()]);
        JPanel infop = infos.SQLqueryInfo(url,userName,userPassword,tableName);
        jsp.setViewportView(infop);
        jsp.setBounds(20,70,165,280);


        frame.add(info);
        frame.add(info1);
        frame.add(jsp);
        frame.add(info2);
        frame.add(queryBtn);
//        frame.add(refreshBtn);
        frame.add(queryText);
        frame.setVisible(true);
    }

}
