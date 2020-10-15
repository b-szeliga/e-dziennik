package com.bszeliga.main;

import com.bszeliga.gui.GUIEventHandler;
import com.bszeliga.gui.headmaster.HeadmasterWindow;
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

import lombok.SneakyThrows;
import org.jasypt.util.password.StrongPasswordEncryptor;
import java.sql.*;

public class MainController implements Initializable {



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

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // make the button mainTitle possible to configure in Main.css
        mainTitle.getStyleClass().add("mainTitleLabel");
        // create object of GUI Helper class.
        guiEventHandler = new GUIEventHandler(mainScreen, mainScreenMenu);
    }

    public void loginUser(ActionEvent actionEvent) throws SQLException {
        // button clicked, check ID and Password in this method.

        // password is encrypted when user registers (requires account verification!)
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        // String encryptedPassword = passwordEncryptor.encryptPassword("admin"); // replace with password from registration

        /*
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();

        finally{
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
        */

        // if check ID and Password are correct, change the login screen into main e-dziennik panel.
        final String HOST = "localhost";
        final String BASE = "szkola?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        final String USER = "guest";
        final String PASS = "";

        Database db = new Database();

        if(db.connect(HOST, BASE, USER, PASS)){
            final Connection conn = db.getConnection();
            //STEP 1: Execute a query
            Statement stmt = conn.createStatement();

            /* sql = "SELECT id, name, lastname, role, school FROM users";
            ResultSet rs = stmt.executeQuery(sql);
                //STEP 2: Extract data from result set
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
            } */

            String sql = "SELECT id, password FROM users WHERE id = " + idField.getText();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                String id = rs.getString("id");
                String pass = rs.getString("password");
                if (idField.getText().equals(id) && passwordEncryptor.checkPassword(passwordField.getText(), pass)) {
                    mainScreen.getChildren().setAll(guiEventHandler.getHeadmasterWindow());
                } else{
                    System.out.println("Bad Password!");
                }
            }
        }


        //mainScreen.getChildren().setAll(guiEventHandler.getParentWindow());
        //mainScreen.getChildren().setAll(guiEventHandler.getTeacherWindow());

    }
}
