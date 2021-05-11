/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomePage;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
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
import java.lang.Object;
import org.jfree.data.category.DefaultCategoryDataset;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.swing.ChartFrame;
import org.jfree.chart.swing.ChartPanel;
import java.awt.print.*;
import java.text.*;
import javax.swing.table.TableModel;


/**
 *
 * @author Kiprono
 */
public class HomePage extends javax.swing.JFrame {
    Methods myMethod = new Methods();
    GoogleLocator googleLocation = new GoogleLocator();
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    private static final String userName = "newuser";
    private static final String password = "Newuser;1";
    private static final String databaseConnection = "jdbc:mysql://localhost:3306/dtraffic";
    DefaultTableModel myRecords = null;
    
    DefaultTableModel selectedIndexedFields = null;//model to hold the indexing row
    DefaultTableModel selectedToCompare = null;//model to hold rows that user want them serialized
    JTable indexRow = null;//indexing row table
    JTable compareList = null;//list of stations to perform calculations
    DefaultTableModel indexRowModel = null;
    
    TableModel thisSelection =null;//will store the selected row as index
    
    Connection sqlConnection = null;
    PreparedStatement query = null;
    ResultSet results = null;
    
    int q, i, id;
    String selectedYear = null;
    public int selectedIndexYear=0;
    public int row =0;//index of the row that was clicked
    boolean ready = false;
    public int selectedColumnIndex=0;
    public int selectedIndexValue=0;//selected value
    
    JTable source=null;
    
    /**
     * Creates new form HomePage
     */
    public HomePage() {
        initComponents();
        selectedIndexedFields = new DefaultTableModel();//initialise our little table
        selectedIndexedFields.setRowCount(0);
        indexRow = new JTable(selectedIndexedFields);
                
        selectedToCompare = new DefaultTableModel();//to be copied over to the next frame
        selectedToCompare.setRowCount(0);
        compareList = new JTable(selectedToCompare);
              
        myRecords = (DefaultTableModel)jTable1.getModel();//all records from queries
        myRecords.setRowCount(0);
        //pack();
        PrepareTable(selectedToCompare);//
        PrepareTable(selectedIndexedFields);
    }
    //this methods create column headers for table models
    private void PrepareTable(DefaultTableModel selectedToCompare){
        selectedToCompare.addColumn("Station Id");
        selectedToCompare.addColumn("STN NO");
        selectedToCompare.addColumn("County");
        selectedToCompare.addColumn("Location");
        selectedToCompare.addColumn("Route");
        selectedToCompare.addColumn("2018");
        selectedToCompare.addColumn("2017");
        selectedToCompare.addColumn("2016");
        selectedToCompare.addColumn("2015");
        selectedToCompare.addColumn("2014");
        selectedToCompare.addColumn("2013");
        selectedToCompare.addColumn("2012");
        selectedToCompare.addColumn("2011");
        selectedToCompare.addColumn("2010");
    }
    
    /*
    this method generates graph from the values in the table
    accepts mouse event and table as parameters
    */
    public void generateGraph(java.awt.event.MouseEvent evt, JTable tableModel){
        DefaultCategoryDataset dataFrame = new DefaultCategoryDataset();//this is frame used to hold data for the graph
        JTable source = (JTable)evt.getSource();
        //Image mapImage = null;
        
        this.row = source.rowAtPoint( evt.getPoint());//index of the row that was xlicked
        
        if(tableModel.getModel().getRowCount() <= 0){//checks to make sure that the table is not empty
            JOptionPane.showMessageDialog(null, "No Data to display.\nRun your Queries First!");
        }else if(source.getModel().getValueAt(0, 0)==null){
            JOptionPane.showMessageDialog(null, "No Data to display.\nRun your Queries First!");
            
        }else{

            int column = 5;//first column where integers are(2018)
            int numCols = source.getColumnCount();//number of columns in our table
            String location = String.valueOf(source.getValueAt(row, 3));//returns the location value of the selected row

            int seedYear = 2010;//the lowest year, for this example in the table. we will ask client which years to include. I limited to from 2010 for this iteration
            String s = null;
            int q = 0;
            
            //looping from column 2010 to 2018, getting the AADT and adding to the dataframe
            //number is ec=xtracted as string then parsed to a double
            //set value takes 3 arguments
            //-> value, y-axis label and x-axis label
            for(int i=numCols; i>column; i--){
                s = source.getModel().getValueAt(row, i-1).toString();
                dataFrame.setValue(Double.parseDouble(s), "Traffic Counts",String.valueOf(seedYear+q));
                q++;
            }

            JFreeChart chart = ChartFactory.createLineChart("Historical AADT---> "+location, "Year", "Traffic Counts",dataFrame);
            chart.setBackgroundPaint(new Color(255, 255, 255));//also can do Color.BLUE, these numbers are HEX values of colors
            chart.getTitle().setPaint(new Color(0,0,0));//go to https://www.google.com/search?q=color+picker
            
            
            ChartPanel cPanel = new ChartPanel(chart);//panel to hold our height
            //cPanel.setSize(450, jPanelGraphGraph.getHeight());//making sure that the chart height is always same as the parent panel
            //cPanel.setLayout(new GridBagLayout());
            
            
            cPanel.addComponentListener(new ComponentAdapter(){//adding the listener to the chart, scrolling
                public void componentResized(ActionEvent e){
                    cPanel.setSize(cPanel.getSize());
                }
            });
            q = Integer.valueOf(String.valueOf(source.getValueAt(row, 0)));
            //System.out.print(q);
            
            //mapImage = googleLocation.getImagePanel(q,jPanelMaps.getWidth(),jPanelMaps.getHeight());
            //mapImage = googleLocation.getScaledImage(mapImage,jPanelMaps.getWidth(),jPanelMaps.getHeight());
            
            cPanel.setMouseWheelEnabled(true);
            
            jPanelGraph.removeAll();//clearing contents of parent panel
            jPanelGraph.add(cPanel, new GridLayout());//align chart to left of panel. right side is still unused thats why it spans across the bottom right panel
            jPanelGraph.validate();
            jPanelGraph.repaint();//displaying it instantly after mouse click
            //Adding map
            jPanelMaps.removeAll();
            jPanelMaps.add(googleLocation.getMap(q,jPanelMaps.getWidth(),jPanelMaps.getHeight()));//getMap returns a panel
            jPanelMaps.validate();
            jPanelMaps.repaint();
                        
        }

    }
    //prepares the second jFrame-->indexer.java
    public void getIndexForm(){
        Indexer indexForm = new Indexer();
        indexForm.jTableIndexRow.setModel(selectedIndexedFields);
        indexForm.jTableToCompare.setModel(selectedToCompare);
        
        indexForm.setVisible(true);
    }
    
    public void getSelected(java.awt.event.MouseEvent evt, JTable tableModel){
        JTable source = (JTable)evt.getSource();//source of the event
        
        if(selectedIndexedFields.getRowCount()!=0){}
        
        
        this.row = source.rowAtPoint(evt.getPoint());//index of the row that was xlicked
        
        if(tableModel.getModel().getRowCount() <= 0){//checks to make sure that the table is not empty
            JOptionPane.showMessageDialog(null, "No Data to display.\nRun your Queries First!");
        }else if(source.getModel().getValueAt(0, 0)==null){
            JOptionPane.showMessageDialog(null, "No Data to display.\nRun your Queries First!");
            
        }else{
            String selectedLocation = String.valueOf(source.getValueAt(row, 3));//getting the location from thr table
            int stationId = Integer.valueOf(String.valueOf(source.getValueAt(row,0)));//getting the station id from table
            int selectYear = Integer.valueOf(String.valueOf(jComboBoxYears.getSelectedItem()));//the year entered from UI
            
            
            Vector colData = new Vector();
            colData.add(stationId);
            colData.add(selectedLocation);
            colData.add(selectYear);
            
            
        }
    }
    
    public void getSelectedRow(JTable table, int row, DefaultTableModel model){
  
        DefaultTableModel modeller = (DefaultTableModel)table.getModel();
        if(modeller.getRowCount() <= 0){//checks to make sure that the table is not empty
            JOptionPane.showMessageDialog(null, "No Data to display.\nRun your Queries First!");
        }else if(modeller.getRowCount() <=0){
            JOptionPane.showMessageDialog(null, "No Data to display.\nRun your Queries First!");

        }else{

            Vector colData = new Vector();
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,0))));//stn id
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,1))));//stn number
            colData.add(String.valueOf(modeller.getValueAt(row, 2)));//county
            colData.add(String.valueOf(modeller.getValueAt(row, 3)));//location
            colData.add(String.valueOf(modeller.getValueAt(row, 4)));//route
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,5))));//2018
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,6))));//2017
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,7))));//2016
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,8))));//2015
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,9))));//2014
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,10))));//2013
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,11))));//2012
            colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,12))));//2011
            //colData.add(Integer.valueOf(String.valueOf(modeller.getValueAt(row,13))));//2010
            colData.add(Integer.valueOf(modeller.getValueAt(row,13).toString()));//2010


            model.addRow(colData);
            System.out.println(model.getRowCount());
        }
        
    }
    
    //resets the values entered into the textboxes and values selected
    public void resetJTextBoxes(){
        JTextField jtext = null;
        JComboBox cbo = null;
        
        for(Component c: jPanelQueries.getComponents()){
            if(c.getClass().toString().contains("javax.swing.JTextField")){
                jtext = (JTextField)c;
                jtext.setText(null);
            }
            else if(c.getClass().toString().contains("javax.swing.JComboBox")){
                cbo = (JComboBox)c;
                cbo.setSelectedIndex(0);
            }
        }
        
    }
    
    public void resetAll(){
        myRecords = (DefaultTableModel)jTable1.getModel();
        myRecords.getDataVector().removeAllElements();
        myRecords.fireTableDataChanged();//noties jtable that model has changed
        
        JTextField jtext = null;
        JComboBox cbo = null;
        //removing the selected values in comboboxes and entries
        for(Component c: jPanelQueries.getComponents()){
            if(c.getClass().toString().contains("javax.swing.JTextField")){
                jtext = (JTextField)c;
                jtext.setText(null);
            }
            else if(c.getClass().toString().contains("javax.swing.JComboBox")){
                cbo = (JComboBox)c;
                cbo.setSelectedIndex(0);
            }
        }
        //removing the graphs
        jPanelMaps.removeAll();
        jPanelMaps.revalidate();
        jPanelGraph.removeAll();  
        jPanelGraph.revalidate();
        jPanelGraph.repaint();
        jPanelMaps.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    public void county(){
        myMethod.orderByCounty(myRecords, jComboBoxCounty, jTable1);
    }
    
    //would do this if you want to save into database
    //not used now
    public void queryDatabase(){
        try{
            //Class.forName("com.mysql.jdbc.driver");
            sqlConnection = DriverManager.getConnection(databaseConnection, userName, password);
            query = sqlConnection.prepareStatement("SELECT STATION_ID, STN_NUMBER, COUNTY, LOCATION, RTE_NUMBER, AADT_2018,AADT_2017,AADT_2016,AADT_2015,AADT_2014,AADT_2013,AADT_2012,AADT_2011,AADT_2010 FROM trafficdata where COUNTY=?");
            query.setString(1,(String)jComboBoxCounty.getSelectedItem());
            results = query.executeQuery();
            ResultSetMetaData sData = results.getMetaData();
            q = sData.getColumnCount();
            
            
            while(results.next()){
                Vector colData = new Vector();
                for(i=1; i<=q;i++){
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
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    /*private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {                                     
        JTable source = (JTable)evt.getSource();
        int row = source.rowAtPoint( evt.getPoint() );
        int column = source.columnAtPoint( evt.getPoint() );
        String s=source.getModel().getValueAt(row, column)+"";

        JOptionPane.showMessageDialog(null, s);
    } */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabelTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jbtnReset = new javax.swing.JButton();
        jbtnExit = new javax.swing.JButton();
        jbtnPrint1 = new javax.swing.JButton();
        jbtnPrint2 = new javax.swing.JButton();
        jbtnAdmin = new javax.swing.JButton();
        jbtnSearch = new javax.swing.JButton();
        jbtnSettings = new javax.swing.JButton();
        jbtnInspect = new javax.swing.JButton();
        jbtnStats = new javax.swing.JButton();
        jbtnStats1 = new javax.swing.JButton();
        jPanelQueries = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelStationID = new javax.swing.JLabel();
        jTextFieldStationId = new javax.swing.JTextField();
        jLabelCounty = new javax.swing.JLabel();
        jLabelRouteNo = new javax.swing.JLabel();
        jTextRouteNum = new javax.swing.JTextField();
        jLabelYear = new javax.swing.JLabel();
        jLabelLocation = new javax.swing.JLabel();
        jTextLocation = new javax.swing.JTextField();
        jComboBoxYears = new javax.swing.JComboBox<>();
        jButtonCounty = new javax.swing.JButton();
        jButtonStationId = new javax.swing.JButton();
        jButtonRTEnumber = new javax.swing.JButton();
        jButtonResetQBox = new javax.swing.JButton();
        jComboBoxCounty = new javax.swing.JComboBox<>();
        jButtonSearchByLocation = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldSelectedId = new javax.swing.JTextField();
        jTextFieldSelectedYear = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableCompare = new javax.swing.JTable();
        jButtonClearSelection = new javax.swing.JButton();
        jButtonRun = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonAddToCompare = new javax.swing.JButton();
        jButtonUseAsIndex = new javax.swing.JButton();
        jButtonClearIndex = new javax.swing.JButton();
        jPanelMaps = new javax.swing.JPanel();
        jPanelGraph = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AADT United States");
        setName("MainFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 255), 3, true));
        jPanel2.setAutoscrolls(true);
        jPanel2.setName("AADT"); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jLabelTitle.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitle.setText("AADT State of Tennessee");

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jbtnReset.setText("Reset");
        jbtnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnResetActionPerformed(evt);
            }
        });

        jbtnExit.setText("Exit");
        jbtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExitActionPerformed(evt);
            }
        });

        jbtnPrint1.setText("Print");
        jbtnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPrint1ActionPerformed(evt);
            }
        });

        jbtnPrint2.setText("Options");
        jbtnPrint2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPrint2ActionPerformed(evt);
            }
        });

        jbtnAdmin.setText("Admin mode");
        jbtnAdmin.setToolTipText("Admin mode");
        jbtnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAdminActionPerformed(evt);
            }
        });

        jbtnSearch.setText("Search");
        jbtnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSearchActionPerformed(evt);
            }
        });

        jbtnSettings.setText("Settings");
        jbtnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSettingsActionPerformed(evt);
            }
        });

        jbtnInspect.setText("Inspect");
        jbtnInspect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnInspectActionPerformed(evt);
            }
        });

        jbtnStats.setText("Traffic Stats");
        jbtnStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnStatsActionPerformed(evt);
            }
        });

        jbtnStats1.setText("Traffic Stats");
        jbtnStats1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnStats1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jbtnStats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnStats1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14)
                .addComponent(jbtnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnInspect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnPrint2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnPrint1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(98, 98, 98))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jbtnSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jbtnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jbtnAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnStats1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnStats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnInspect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnPrint2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnPrint1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Query Fields");

        jLabelStationID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelStationID.setText("Station ID");

        jTextFieldStationId.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextFieldStationId.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextFieldStationId.setToolTipText("");

        jLabelCounty.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelCounty.setText("County");

        jLabelRouteNo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelRouteNo.setText("Route Number");

        jTextRouteNum.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextRouteNum.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextRouteNum.setToolTipText("");

        jLabelYear.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelYear.setText("Year");

        jLabelLocation.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelLocation.setText("Location");

        jTextLocation.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextLocation.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextLocation.setToolTipText("");

        jComboBoxYears.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jComboBoxYears.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select year", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982" }));

        jButtonCounty.setText("Go");
        jButtonCounty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCountyActionPerformed(evt);
            }
        });

        jButtonStationId.setText("Go");
        jButtonStationId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStationIdActionPerformed(evt);
            }
        });

        jButtonRTEnumber.setText("Go");
        jButtonRTEnumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRTEnumberActionPerformed(evt);
            }
        });

        jButtonResetQBox.setText("Clear fields");
        jButtonResetQBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetQBoxActionPerformed(evt);
            }
        });

        jComboBoxCounty.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jComboBoxCounty.setMaximumRowCount(5);
        jComboBoxCounty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select County", "Anderson", "Bedford", "Benton", "Bledsoe", "Blount", "Bradley", "Campbell", "Cannon", "Carroll", "Carter", "Cheatham", "Chester", "Claiborne", "Clay", "Cocke", "Coffee", "Crockett", "Cumberland", "Davidson", "Decatur", "DeKalb", "Dickson", "Dyer", "Fayette", "Fentress", "Franklin", "Gibson", "Giles", "Grainger", "Greene", "Grundy", "Hamblen", "Hamilton", "Hancock", "Hardeman", "Hardin", "Hawkins", "Haywood", "Henderson", "Henry", "Hickman", "Houston", "Humphreys", "Jackson", "Jefferson", "Johnson", "Knox", "Lake", "Lauderdale", "Lawrence", "Lewis", "Lincoln", "Loudon", "Macon", "Madison", "Marion", "Marshall", "Maury", "McMinn", "McNairy", "Meigs", "Monroe", "Montgomery", "Moore", "Morgan", "Obion", "Overton", "Perry", "Pickett", "Polk", "Putnam", "Rhea", "Roane", "Robertson", "Rutherford", "Scott", "Sequatchie", "Sevier", "Shelby", "Smith", "Stewart", "Sullivan", "Sumner", "Tipton", "Trousdale", "Unicoi", "Union", "Van Buren", "Warren", "Washington", "Wayne", "Weakley", "White", "Williamson", "Wilson" }));

        jButtonSearchByLocation.setText("Go");
        jButtonSearchByLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchByLocationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelQueriesLayout = new javax.swing.GroupLayout(jPanelQueries);
        jPanelQueries.setLayout(jPanelQueriesLayout);
        jPanelQueriesLayout.setHorizontalGroup(
            jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQueriesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelStationID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCounty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelRouteNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jComboBoxYears, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextRouteNum, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxCounty, javax.swing.GroupLayout.Alignment.LEADING, 0, 124, Short.MAX_VALUE)
                    .addComponent(jTextFieldStationId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextLocation))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelQueriesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonRTEnumber)
                            .addComponent(jButtonCounty)
                            .addComponent(jButtonStationId)))
                    .addComponent(jButtonSearchByLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
            .addGroup(jPanelQueriesLayout.createSequentialGroup()
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelQueriesLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelQueriesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonResetQBox, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelQueriesLayout.setVerticalGroup(
            jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQueriesLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelStationID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelQueriesLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldStationId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonStationId))))
                .addGap(9, 9, 9)
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCounty, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBoxCounty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCounty)))
                .addGap(5, 5, 5)
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelRouteNo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextRouteNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonRTEnumber)))
                .addGap(10, 10, 10)
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelYear, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxYears, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelQueriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonSearchByLocation)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(jButtonResetQBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelQueriesLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonCounty, jButtonRTEnumber, jButtonResetQBox, jButtonStationId});

        jTextPane1.setBorder(null);
        jTextPane1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jTextPane1.setText("To generate comparison,Enter the year and select  the row in the table to the left. Specify the year that you want to set as index year below. First row is your index station and year");
        jTextPane1.setToolTipText("");
        jScrollPane3.setViewportView(jTextPane1);

        jLabel2.setText("You have selected station ID-> ");

        jLabel3.setText("as your reference station and year-> ");

        jTextFieldSelectedId.setEditable(false);

        jTextFieldSelectedYear.setEditable(false);

        jLabel4.setText("select the other stations");

        jLabel5.setText("below are the selected stations");

        jTableCompare.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Station ID", "Location", "Year"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableCompare);

        jButtonClearSelection.setText("Clear all");
        jButtonClearSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearSelectionActionPerformed(evt);
            }
        });

        jButtonRun.setText("Run");
        jButtonRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRunActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jButtonClearSelection)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonRun))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextFieldSelectedYear, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextFieldSelectedId, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSelectedId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSelectedYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClearSelection)
                    .addComponent(jButtonRun))
                .addContainerGap())
        );

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Station ID", "STN No", "County", "Location", "Route", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setShowGrid(true);
        jTable1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTable1MouseMoved(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButtonAddToCompare.setText("Add to compare list");
        jButtonAddToCompare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddToCompareActionPerformed(evt);
            }
        });

        jButtonUseAsIndex.setText("Use as Index");
        jButtonUseAsIndex.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonUseAsIndexMouseClicked(evt);
            }
        });
        jButtonUseAsIndex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUseAsIndexActionPerformed(evt);
            }
        });

        jButtonClearIndex.setText("Remove index");
        jButtonClearIndex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearIndexActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1114, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonUseAsIndex, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonAddToCompare)
                .addGap(18, 18, 18)
                .addComponent(jButtonClearIndex)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClearIndex)
                    .addComponent(jButtonAddToCompare)
                    .addComponent(jButtonUseAsIndex))
                .addContainerGap())
        );

        jPanelMaps.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanelMaps.setPreferredSize(new java.awt.Dimension(540, 371));
        jPanelMaps.setLayout(new java.awt.GridLayout(1, 0));

        jPanelGraph.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanelGraph.setPreferredSize(new java.awt.Dimension(540, 371));
        jPanelGraph.setLayout(new java.awt.GridLayout(1, 0));

        jDesktopPane1.setLayer(jLabelTitle, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanelQueries, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanelMaps, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanelGraph, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(440, 440, 440)
                .addComponent(jLabelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                .addGap(415, 415, 415))
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelQueries, javax.swing.GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addComponent(jPanelGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelMaps, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanelQueries, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelMaps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelGraph, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        jPanel2.add(jDesktopPane1);

        getContentPane().add(jPanel2);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnResetActionPerformed
        // TODO add your handling code here:
       resetAll();
    }//GEN-LAST:event_jbtnResetActionPerformed
    
    private JFrame frame;
    private void jbtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExitActionPerformed
        // TODO add your handling code here:
        frame = new JFrame("Exit");
        if(JOptionPane.showConfirmDialog(frame, "Confirm you want to Exit","AADT State of Tn", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_jbtnExitActionPerformed

    private void jbtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPrint1ActionPerformed
        // TODO add your handling code here:
        //PRINT BUTTON
        MessageFormat printheader = new MessageFormat("Traffic Data Report");//header
        MessageFormat printfooter = new MessageFormat("Page{0,number,integer}");//page numbers
        try{
            
            jTable1.print(JTable.PrintMode.NORMAL, printheader, printfooter);//format page layout.
        }
        catch(java.awt.print.PrinterException e){
            
            System.err.format("Cannnot print %s%n,", e.getMessage());//print cannot print table
        }
    }//GEN-LAST:event_jbtnPrint1ActionPerformed

    private void jbtnPrint2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPrint2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnPrint2ActionPerformed

    private void jbtnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAdminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnAdminActionPerformed

    private void jbtnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnSearchActionPerformed

    private void jbtnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSettingsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnSettingsActionPerformed

    private void jbtnInspectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnInspectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnInspectActionPerformed

    private void jbtnStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnStatsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnStatsActionPerformed

    private void jButtonCountyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCountyActionPerformed
        // TODO add your handling code here:        
        if(jComboBoxCounty.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null, "Select County First!");
        }else{
            myMethod.orderByCounty(myRecords, jComboBoxCounty, jTable1);
        }
    }//GEN-LAST:event_jButtonCountyActionPerformed

    private void jButtonStationIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStationIdActionPerformed
        // TODO add your handling code here
        if(jTextFieldStationId.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Enter Station ID First!");
        }else{
            myMethod.orderByStationID(myRecords, jTextFieldStationId, jTable1);
        }
    }//GEN-LAST:event_jButtonStationIdActionPerformed

    private void jButtonResetQBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetQBoxActionPerformed
        // TODO add your handling code here:
        resetJTextBoxes();
    }//GEN-LAST:event_jButtonResetQBoxActionPerformed

    private void jbtnStats1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnStats1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnStats1ActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        //insertData();
    }//GEN-LAST:event_formWindowActivated

    private void jButtonRTEnumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRTEnumberActionPerformed
        // TODO add your handling code here:
        if(jTextRouteNum.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Enter State Route!");
        }else{
            myMethod.orderByStateRoutes(myRecords, jTextRouteNum, jTable1);
        }
    }//GEN-LAST:event_jButtonRTEnumberActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
        System.out.println("Clicked!");
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        
    }//GEN-LAST:event_jTable1MousePressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        //jTable1MousePressed(evt);
        this.source = (JTable)evt.getSource();
        if(source == null){
            JOptionPane.showMessageDialog(null, "Run your Queries First!");
        }else{
            generateGraph(evt, source);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jComboBoxYearsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxYearsActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jComboBoxYearsActionPerformed

    private void jButtonSearchByLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchByLocationActionPerformed
        // TODO add your handling code here:
        if(jTextLocation.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Enter Station ID First!");
        }else{
            myMethod.orderByLocation(myRecords, jTextLocation, jTable1);
        }
    }//GEN-LAST:event_jButtonSearchByLocationActionPerformed

    private void jButtonClearSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearSelectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonClearSelectionActionPerformed

    private void jButtonRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRunActionPerformed
        // TODO add your handling code here:
        getIndexForm();//run the indexing field
    }//GEN-LAST:event_jButtonRunActionPerformed

    private void jTable1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseMoved

    private void jButtonUseAsIndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUseAsIndexActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButtonUseAsIndexActionPerformed

    private void jButtonUseAsIndexMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonUseAsIndexMouseClicked
        // TODO add your handling code here:
        //find row that is selected on the table
        //extract the year specified
        //Run 2 selectedIndexYear
        int index = jTable1.getSelectedRow();//selected row
        this.selectedColumnIndex = jTable1.getSelectedColumn();//selected column
        if(source == null){
            JOptionPane.showMessageDialog(null, "Run your Queries First!");
        }else{   
                        
            try{
                this.getSelectedRow(jTable1, jTable1.getSelectedRow(),selectedIndexedFields);//save the selected row
                thisSelection = jTable1.getModel();
                selectedIndexYear = Integer.parseInt(String.valueOf(thisSelection.getColumnName(this.selectedColumnIndex)));//name of the index
                jTextFieldSelectedYear.setText(String.valueOf(selectedIndexYear));
                jButtonUseAsIndex.setEnabled(false);//dim this button, we dont want user to set index anymore
                //update jtext field
                jTextFieldSelectedId.setText(String.valueOf(thisSelection.getValueAt(index, 0)));
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Select a valid Column!!");
                selectedIndexedFields.setRowCount(0);//clear the tables
            }
        }
    }//GEN-LAST:event_jButtonUseAsIndexMouseClicked

    private void jButtonClearIndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearIndexActionPerformed
        // TODO add your handling code here:
        if(source == null){
            JOptionPane.showMessageDialog(null, "Run your Queries First!");
        }else{            
            thisSelection = null;//remove previous set model
            jButtonUseAsIndex.setEnabled(true);//reenable this button, allow user to set index 
            jTextFieldSelectedId.setText("");//clear text fields
            jTextFieldSelectedYear.setText("");//clear text fields
            selectedIndexedFields.setRowCount(0);//clear the tables
        }
    }//GEN-LAST:event_jButtonClearIndexActionPerformed

    private void jButtonAddToCompareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddToCompareActionPerformed
        // TODO add your handling code here:
        if(source == null){
            JOptionPane.showMessageDialog(null, "Run your Queries First!");
        }else{
            this.getSelectedRow(jTable1, jTable1.getSelectedRow(),selectedToCompare);//call function, pass in the selected row
        }
    }//GEN-LAST:event_jButtonAddToCompareActionPerformed

    /**
     * @paramargs the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddToCompare;
    private javax.swing.JButton jButtonClearIndex;
    private javax.swing.JButton jButtonClearSelection;
    private javax.swing.JButton jButtonCounty;
    private javax.swing.JButton jButtonRTEnumber;
    private javax.swing.JButton jButtonResetQBox;
    private javax.swing.JButton jButtonRun;
    private javax.swing.JButton jButtonSearchByLocation;
    private javax.swing.JButton jButtonStationId;
    private javax.swing.JButton jButtonUseAsIndex;
    private javax.swing.JComboBox<String> jComboBoxCounty;
    private javax.swing.JComboBox<String> jComboBoxYears;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelCounty;
    private javax.swing.JLabel jLabelLocation;
    private javax.swing.JLabel jLabelRouteNo;
    private javax.swing.JLabel jLabelStationID;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelYear;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelGraph;
    private javax.swing.JPanel jPanelMaps;
    private javax.swing.JPanel jPanelQueries;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableCompare;
    private javax.swing.JTextField jTextFieldSelectedId;
    public javax.swing.JTextField jTextFieldSelectedYear;
    private javax.swing.JTextField jTextFieldStationId;
    private javax.swing.JTextField jTextLocation;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextField jTextRouteNum;
    private javax.swing.JButton jbtnAdmin;
    private javax.swing.JButton jbtnExit;
    private javax.swing.JButton jbtnInspect;
    private javax.swing.JButton jbtnPrint1;
    private javax.swing.JButton jbtnPrint2;
    private javax.swing.JButton jbtnReset;
    private javax.swing.JButton jbtnSearch;
    private javax.swing.JButton jbtnSettings;
    private javax.swing.JButton jbtnStats;
    private javax.swing.JButton jbtnStats1;
    // End of variables declaration//GEN-END:variables
}
