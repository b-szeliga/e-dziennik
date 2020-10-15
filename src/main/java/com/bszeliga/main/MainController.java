package com.bszeliga.main;

import com.bszeliga.gui.GUIEventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import org.jasypt.util.password.StrongPasswordEncryptor;
import java.sql.*;

public class MainController implements Initializable {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/szkola?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    //  Database credentials
    static final String USER = "guest";
    static final String PASS = "";

    @FXML
    public GridPane mainScreen;

    @FXML
    public VBox mainScreenMenu;

    @FXML
    public TextField idField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Label mainTitle;

    private GUIEventHandler guiEventHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // make the button mainTitle possible to configure in Main.css
        mainTitle.getStyleClass().add("mainTitleLabel");
        // create object of GUI Helper class.
        guiEventHandler = new GUIEventHandler(mainScreen, mainScreenMenu);
    }

    public void loginUser(ActionEvent actionEvent) {
        Connection conn = null;
        Statement stmt = null;

        // button clicked, check ID and Password in this method.

        // encrypting example REPLACE ASAP
        // WIP: replace example with JDBC request

        // password is encrypted when user registers (requires account verification!)
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword("admin"); // replace with password from registration

        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, name, lastname, role, school FROM users";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("id");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                int role = rs.getInt("role");
                String school = rs.getString("school");

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", First: " + name);
                System.out.print(", Last: " + lastname);
                System.out.print(", Role: " + role);
                System.out.println(", School: " + school);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        // if check ID and Password are correct, change the login screen into main e-dziennik panel.
        if (idField.getText().equals("admin") && passwordEncryptor.checkPassword(passwordField.getText(), encryptedPassword)) {
            mainScreen.getChildren().setAll(guiEventHandler.getHeadmasterWindow());
        } else { // this one is temporary until JDBC gets there
            mainScreen.getChildren().setAll(guiEventHandler.getStudentWindow());
        }


        //mainScreen.getChildren().setAll(guiEventHandler.getParentWindow());
        //mainScreen.getChildren().setAll(guiEventHandler.getTeacherWindow());

    }
}
