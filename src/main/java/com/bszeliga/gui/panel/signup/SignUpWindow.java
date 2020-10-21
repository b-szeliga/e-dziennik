package com.bszeliga.gui.panel.signup;

import com.bszeliga.gui.database.Database;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpWindow extends GridPane implements Initializable {
    private final GridPane mainScreen;
    private final VBox mainScreenMenu;

    @FXML
    public TextField idField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public TextField nameField;

    @FXML
    public TextField surnameField;

    @FXML
    public TextField schoolField;

    @FXML
    public ChoiceBox<String> roleChoiceBox;

    private Database db;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roleChoiceBox.setItems(FXCollections.observableArrayList(
                "Student", "Nauczyciel", "Rodzic"
        ));
        roleChoiceBox.setValue("Student");
    }

    public SignUpWindow(GridPane mainScreen, VBox mainScreenMenu) {
        this.mainScreen = mainScreen;
        this.mainScreenMenu = mainScreenMenu;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(ActionEvent actionEvent) throws SQLException {
        System.out.println("Adding user to database");
        if (Objects.nonNull(db)) {
            // do checks, make sure the fields aren't empty. TODO: regex in the future


            // encrypt password
            StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            String encryptedPassword = passwordEncryptor.encryptPassword(passwordField.getText()); // replace with password from registration

            final Connection conn = db.getConnection();

            String regName = nameField.getText();
            String regSurname = surnameField.getText();
            String regRole = roleChoiceBox.getValue();
            int regRoleInt = 0;

            switch(regRole){
                case "Student":
                    regRoleInt = 0;
                    break;
                case "Rodzic":
                    regRoleInt = 1;
                    break;
                case "Nauczyciel":
                    regRoleInt = 2;
                    break;
            }


            String regSchool = schoolField.getText();

            String sql = "INSERT INTO users (name, lastname, role, school, password, verified) VALUES (?, ?, ?, ?, ?, 0)";

            PreparedStatement loginR = conn.prepareStatement(sql);

            loginR.setString(1, regName);
            loginR.setString(2, regSurname);
            loginR.setInt(3, regRoleInt);
            loginR.setString(4, regSchool);
            loginR.setString(5, encryptedPassword);

            loginR.executeUpdate();
            System.out.println(loginR);

            // show alert telling user that his registration was completed. Show him his ID in IDField and in alert.

            //ResultSet rs = stmt.executeUpdate(sql);
            //stmt.executeUpdate(sql);
//            if (Objects.nonNull(rs)) {
//                ObservableList<TableRow> users = FXCollections.observableArrayList();
//                while (rs.next()) {
//                    int id = rs.getInt("id");
//                    String name = rs.getString("name");
//                    String lastname = rs.getString("lastname");
//                    int role = rs.getInt("role");
//                    String school = rs.getString("school");
//                    users.add(new TableRow(id, name, lastname, role, school));
//                }
//
//                // show user what ID they got!
//            } else {
//                // handle situation where database is empty or there was an error.
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error");
//                alert.setHeaderText("A database error occurred.");
//                alert.setContentText("Database is empty!\nUnknown error occured.");
//                alert.showAndWait();
//            }
        }
    }

    public void goBack(ActionEvent actionEvent) {
        // change the window into Login window.
        mainScreen.getChildren().setAll(mainScreenMenu);
    }

    public void sendDatabase(Database db) {
        this.db = db;
    }
}