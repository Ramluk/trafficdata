/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomePage;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.viewer.GeoPosition;


/**
 *
 * @author Kiprono
 */
public class GoogleLocator {
    private PreparedStatement query = null;
    private ResultSet results = null;
    private Connection sqlConnection = null;
    
    Frame newF = new Frame();
    
    private  final String userName = "newuser";
    private final String password = "Newuser;1";
    private final String databaseConnection = "jdbc:mysql://localhost:3306/dtraffic";
    
    double y;
    double x;

    String longitude = null;
    String latitude = null;//x
    
    Image myMap = null;
    
    public GoogleLocator(){
        
    }
    /*
    this get Map function takes in station id and size of the container panel.
    */
    
    public JPanel getMap(int stationId, int width, int height){
        JXMapKit jXMapKit = new JXMapKit();//our map--basically all globe
        JPanel thisPanel = new JPanel();//panel container
        GeoPosition location = null;
        
        try{
            //Class.forName("com.mysql.jdbc.driver");
            sqlConnection = DriverManager.getConnection(databaseConnection, userName, password);
            query = sqlConnection.prepareStatement("SELECT COUNTY, LOCATION, X,Y FROM trafficdata where Station_ID=?");
            query.setInt(1,stationId);
            results = query.executeQuery();
            
            results.next();
            y = results.getFloat("X");//longotude
            x = results.getFloat("Y");//latitude
            
            location = new GeoPosition(x,y);
            
            sqlConnection.close();
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        
        
        jXMapKit.setZoom(5);//minimum zoom
        jXMapKit.setAddressLocation(location);//pinned location
        jXMapKit.setSize(width, height);
      
        thisPanel.removeAll();//remove content of new panel
        thisPanel.setLayout(new GridLayout());//enforcing the grid layout on the new panel
        thisPanel.setSize(width, height);
        thisPanel.add(jXMapKit);//adding the map widget into this panel
        
        return thisPanel;
    }
    
    //Scales the map image to fit the panel
    //uses java 2d graphics
    //stack overflows
    public Image getScaledImage(Image image, int width, int height){
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = resizedImage.createGraphics();
        
        graphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphic.drawImage(image, 0,0,width,height,null);
        graphic.dispose();
        
        return resizedImage;
    }
    
    
    //this is Google API for generating static maps
    //deprecated for now
    public Image getImagePanel(int stationId, int width, int height){
        JPanel myPanel = new JPanel();
        //Run query to obtain longitude and latitude of stationId
        int q;
        try{
            //Class.forName("com.mysql.jdbc.driver");
            sqlConnection = DriverManager.getConnection(databaseConnection, userName, password);
            query = sqlConnection.prepareStatement("SELECT COUNTY, LOCATION, X,Y FROM trafficdata where Station_ID=?");
            query.setInt(1,stationId);
            results = query.executeQuery();
            ResultSetMetaData sData = results.getMetaData();
            
            results.next();
            y = results.getFloat("X");
            x = results.getFloat("Y");
            
            longitude = String.valueOf(x);
            latitude = String.valueOf(y);
            
            //System.out.println(y+ " "+ x);
            sqlConnection.close();
            
            
            try {
                //line below has to follow the https format
            
                String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?center="+x+","+y+"&zoom=15&size="+width+"x"+height+"&maptype=satelite" +
                            "&markers=color:red%7Clabel:S%7C"+x+","+y+
                            "&key=AIzaSyADwXf9LZjGy2K8VQxxRNgxOLpCNh3mfKo";//this key is unique to my account, rates are free for first 1000 lookups a month
            
                URL url = new URL(imageUrl);
                myMap = ImageIO.read(url);
    
                //newF.add(new JLabel(new ImageIcon((new ImageIcon("image.jpg")).getImage().getScaledInstance(630, 600,java.awt.Image.SCALE_SMOOTH))));
                //newF.setVisible(true);

            } catch (IOException e) {
                System.out.println(String.valueOf(e)+" url");
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex + " here is error");
        }
        
        return myMap;
    }
}
