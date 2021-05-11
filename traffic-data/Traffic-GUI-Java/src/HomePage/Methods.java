package HomePage;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;


public class Methods extends javax.swing.JFrame {
    
    private PreparedStatement query = null;//this is equivalent of query statement
    private ResultSet results = null;
    private Connection sqlConnection = null;
    
    private  final String userName = "newuser";
    private final String password = "Newuser;1";
    private final String databaseConnection = "jdbc:mysql://localhost:3306/dtraffic";
    
    public Methods(){
        
    }
    
    public void orderByCounty(DefaultTableModel myRecords, JComboBox<String> jComboBoxCounty, JTable jTable1){
        int q;
        
        try{
            //Class.forName("com.mysql.jdbc.driver");
            sqlConnection = DriverManager.getConnection(databaseConnection, userName, password);
            query = sqlConnection.prepareStatement("SELECT STATION_ID, STN_NUMBER, COUNTY, LOCATION, RTE_NUMBER, AADT_2018,AADT_2017,AADT_2016,AADT_2015,AADT_2014,AADT_2013,AADT_2012,AADT_2011,AADT_2010 FROM trafficdata where COUNTY=?");
            query.setString(1,(String)jComboBoxCounty.getSelectedItem());//appending the valaue of the input
            results = query.executeQuery();
            ResultSetMetaData sData = results.getMetaData();//our results from above statement
            q = sData.getColumnCount();//number of columns returned
            
            myRecords = (DefaultTableModel)jTable1.getModel();//puttint the results into a table
            myRecords.setRowCount(0);//first emptying the sets in the table
            
            while(results.next()){//loops  through the rows in the results set
                Vector colData = new Vector();//row in the results
                for(int i=1; i<=q;i++){
                    colData.add(results.getString("Station_ID"));//returns the value under tags "Station_ID" and adds to the row
                    colData.add(results.getString("STN_NUMBER"));
                    colData.add(results.getString("COUNTY"));
                    colData.add(results.getString("LOCATION"));
                    colData.add(results.getString("RTE_NUMBER"));
                    colData.add(results.getInt("AADT_2018"));
                    colData.add(results.getInt("AADT_2017"));
                    colData.add(results.getInt("AADT_2016"));
                    colData.add(results.getInt("AADT_2015"));
                    colData.add(results.getInt("AADT_2014"));
                    colData.add(results.getInt("AADT_2013"));
                    colData.add(results.getInt("AADT_2012"));
                    colData.add(results.getInt("AADT_2011"));
                    colData.add(results.getInt("AADT_2010"));
                }
                myRecords.addRow(colData);//adds row to the table
            }
            sqlConnection.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);//all errs encountered will be printed out, program wont stop
        }
    }
    
    public void orderByLocation(DefaultTableModel myRecords, JTextField jTextLocation, JTable jTable1){
        int q;
        String location = jTextLocation.getText();
        location = '%'+location + '"';
        location = '"'+location;
        System.out.println(location);
        
        try{
            //Class.forName("com.mysql.jdbc.driver");
            sqlConnection = DriverManager.getConnection(databaseConnection, userName, password);
            query = sqlConnection.prepareStatement("SELECT STATION_ID, STN_NUMBER, COUNTY, LOCATION, RTE_NUMBER, AADT_2018,AADT_2017,AADT_2016,AADT_2015,AADT_2014,AADT_2013,AADT_2012,AADT_2011,AADT_2010 FROM trafficdata where LOCATION LIKE "+location+"");
            //query.setString(1, location);
            results = query.executeQuery();
            ResultSetMetaData sData = results.getMetaData();
            q = sData.getColumnCount();
            
            myRecords = (DefaultTableModel)jTable1.getModel();
            myRecords.setRowCount(0);
            
            while(results.next()){
                Vector colData = new Vector();
                for(int i=1; i<=q;i++){
                    colData.add(results.getString("Station_ID"));
                    colData.add(results.getString("STN_NUMBER"));
                    colData.add(results.getString("COUNTY"));
                    colData.add(results.getString("LOCATION"));
                    colData.add(results.getString("RTE_NUMBER"));
                    colData.add(results.getInt("AADT_2018"));
                    colData.add(results.getInt("AADT_2017"));
                    colData.add(results.getInt("AADT_2016"));
                    colData.add(results.getInt("AADT_2015"));
                    colData.add(results.getInt("AADT_2014"));
                    colData.add(results.getInt("AADT_2013"));
                    colData.add(results.getInt("AADT_2012"));
                    colData.add(results.getInt("AADT_2011"));
                    colData.add(results.getInt("AADT_2010"));
                }
                myRecords.addRow(colData);
            }
            sqlConnection.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void orderByStationID(DefaultTableModel myRecords, JTextField jTextRouteNum, JTable jTable1){
        int q;
        
        try{
            //Class.forName("com.mysql.jdbc.driver");
            sqlConnection = DriverManager.getConnection(databaseConnection, userName, password);
            query = sqlConnection.prepareStatement("SELECT STATION_ID, STN_NUMBER, COUNTY, LOCATION, RTE_NUMBER, AADT_2018,AADT_2017,AADT_2016,AADT_2015,AADT_2014,AADT_2013,AADT_2012,AADT_2011,AADT_2010 FROM trafficdata where STATION_ID=?");
            query.setInt(1,Integer.parseInt(jTextRouteNum.getText()));
            results = query.executeQuery();
            ResultSetMetaData sData = results.getMetaData();
            q = sData.getColumnCount();
            
            myRecords = (DefaultTableModel)jTable1.getModel();
            myRecords.setRowCount(0);
            
            while(results.next()){
                Vector colData = new Vector();
                for(int i=1; i<=q;i++){
                    colData.add(results.getString("Station_ID"));
                    colData.add(results.getString("STN_NUMBER"));
                    colData.add(results.getString("COUNTY"));
                    colData.add(results.getString("LOCATION"));
                    colData.add(results.getString("RTE_NUMBER"));
                    colData.add(results.getInt("AADT_2018"));
                    colData.add(results.getInt("AADT_2017"));
                    colData.add(results.getInt("AADT_2016"));
                    colData.add(results.getInt("AADT_2015"));
                    colData.add(results.getInt("AADT_2014"));
                    colData.add(results.getInt("AADT_2013"));
                    colData.add(results.getInt("AADT_2012"));
                    colData.add(results.getInt("AADT_2011"));
                    colData.add(results.getInt("AADT_2010"));
                }
                myRecords.addRow(colData);
            }
            sqlConnection.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        
    }
    
    public void orderByStateRoutes(DefaultTableModel myRecords, JTextField jTextRouteNum, JTable jTable1){
        int q;
        
        try{
            //Class.forName("com.mysql.jdbc.driver");
            sqlConnection = DriverManager.getConnection(databaseConnection, userName, password);
            query = sqlConnection.prepareStatement("SELECT STATION_ID, STN_NUMBER, COUNTY, LOCATION, RTE_NUMBER, AADT_2018,AADT_2017,AADT_2016,AADT_2015,AADT_2014,AADT_2013,AADT_2012,AADT_2011,AADT_2010 FROM trafficdata where RTE_NUMBER=?");
            query.setString(1, jTextRouteNum.getText());
            results = query.executeQuery();
            ResultSetMetaData sData = results.getMetaData();
            q = sData.getColumnCount();
            
            myRecords = (DefaultTableModel)jTable1.getModel();
            myRecords.setRowCount(0);
            
            while(results.next()){
                Vector colData = new Vector();
                for(int i=1; i<=q;i++){
                    colData.add(results.getString("Station_ID"));
                    colData.add(results.getString("STN_NUMBER"));
                    colData.add(results.getString("COUNTY"));
                    colData.add(results.getString("LOCATION"));
                    colData.add(results.getString("RTE_NUMBER"));
                    colData.add(results.getInt("AADT_2018"));
                    colData.add(results.getInt("AADT_2017"));
                    colData.add(results.getInt("AADT_2016"));
                    colData.add(results.getInt("AADT_2015"));
                    colData.add(results.getInt("AADT_2014"));
                    colData.add(results.getInt("AADT_2013"));
                    colData.add(results.getInt("AADT_2012"));
                    colData.add(results.getInt("AADT_2011"));
                    colData.add(results.getInt("AADT_2010"));
                }
                myRecords.addRow(colData);
            }
            sqlConnection.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }


}
