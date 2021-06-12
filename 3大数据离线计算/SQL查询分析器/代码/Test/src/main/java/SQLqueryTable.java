import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SQLqueryTable {
    private JFrame frame =new JFrame();
    public void SQLqueryTable(String[][] dataTable){
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contentPane=frame.getContentPane();
        ArrayList<String> name = new ArrayList<String>();
        for(int i = 0;i<100;i++){
            if(dataTable[0][i] == null){
                break;
            }
            name.add(dataTable[0][i]);
        }
        int col=name.size();
        int row=0;
        for(int j=0;j<100;j++){
            if(dataTable[j][0]==null){
                break;
            }
            row++;
        }

        Object[][] tableContent = new Object[row-1][col];
        for(int i=1;i<row;i++){
            for(int j=0;j<col;j++){
                tableContent[i-1][j]=dataTable[i][j];
            }

        }
        String[] arr = name.toArray(new String[name.size()]);

        JTable table=new JTable(tableContent,arr);
        contentPane.add(new JScrollPane(table));
        frame.setVisible(true);
    }
}
