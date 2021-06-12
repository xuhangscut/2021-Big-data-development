import javax.swing.*;

public class SQLqueryInfo {
    public JPanel SQLqueryInfo(String url, String userName, String userPassword,String[] tableNames){
        JPanel table= new JPanel();
        Box vBox = Box.createVerticalBox();
        final sparkSQL s = new sparkSQL(url,userName,userPassword);
        for(int i = 0; i < tableNames.length; i++) {
            JLabel tableTitle = new JLabel(tableNames[i]);
            String[] TableFieldNames = s.showTableField(tableNames[i]);
            JList list=new JList(TableFieldNames);
            vBox.add(tableTitle);
            list.setVisibleRowCount(4);
            JScrollPane jsp = new JScrollPane(list);
            vBox.add(jsp);
        }
        table.add(vBox);

          return table;
    }
}
