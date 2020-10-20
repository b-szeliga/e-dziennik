package com.bszeliga.main;

import com.bszeliga.gui.GUIEventHandler;
import com.bszeliga.gui.database.Database;
import com.bszeliga.logic.ValidateTextField;
import com.bszeliga.gui.panel.signup.SignUpWindow;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
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
    private SignUpWindow signUpWindow;
    private Database db;
    private boolean isDatabaseConnected;
    private ValidateTextField validateTextField;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // make the button mainTitle possible to configure in Main.css
        mainTitle.getStyleClass().add("mainTitleLabel");
        // create object of GUI Helper class.
        guiEventHandler = new GUIEventHandler(mainScreen, mainScreenMenu);
        handleDatabase();
        // check idField regex, only numbers allowed.
        validateTextField = new ValidateTextField(idField);
        idField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateTextField.replaceLettersToNumbers(newValue);
        });
    }

    private void handleDatabase() {
        db = new Database();

        // if check ID and Password are correct, change the login screen into main e-dziennik panel.
        final String HOST = "localhost";
        final String BASE = "szkola?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        final String USER = "guest";
        final String PASS = "";

        isDatabaseConnected = db.connect(HOST, BASE, USER, PASS);

        // send Database object to Panel Window in order to view users in Admin Panel.
        guiEventHandler.getPanelWindow().sendDatabase(db);
        // send Database object to SignUpWindow in order to add registered user.
        guiEventHandler.getSignUpWindow().sendDatabase(db);
    }

    public void loginUser(ActionEvent actionEvent) throws SQLException {
        // cursor should change to loading cursor
        mainScreen.setCursor(Cursor.WAIT);

        // creating loginTask as separate thread to keep non UI elements outside FX Application Thread.
        // Returns user's role.
        Task<Integer> loginTask = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                // password is encrypted when user registers (requires account verification!)
                StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                //String encryptedPassword = passwordEncryptor.encryptPassword("student"); // replace with password from registration

                if (isDatabaseConnected) {
                    final Connection conn = db.getConnection();
                    //STEP 1: Execute a query
                    Statement stmt = conn.createStatement();

                    if (Objects.isNull(idField.getText()) || idField.getText().isEmpty() || Objects.isNull(passwordField.getText()) || passwordField.getText().isEmpty()) {
                        return -1;
                    } else {
                        String sql = "SELECT id, password, role FROM users WHERE id = " + idField.getText();
                        ResultSet rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            String id = rs.getString("id");
                            String pass = rs.getString("password");
                            int role = rs.getInt("role");
                            if (idField.getText().equals(id) && passwordEncryptor.checkPassword(passwordField.getText(), pass)) {
                                return role;
                            } else {
                                // incorrect password
                                return -3;
                            }
                        }
                    }
                } else {
                    // connection to database failed
                    return -2;
                }
                // everything failed, fields might be empty
                return -1;
            }
        };
        loginTask.setOnSucceeded(event -> {
            mainScreen.setCursor(Cursor.DEFAULT);
            // change the screen according to returned role value
            if (loginTask.getValue() != null) {
                switch (loginTask.getValue()) {
                    case -3:
                        System.out.println("Wrong password!");
                        createAlert("A login error occured", "Your password does not match!");
                        break;
                    case -2:
                        System.out.println("Could not connect to database.");
                        createAlert("A network error occured.", "Could not connect to database.");
                        break;
                    case -1:
                        System.out.println("Connection failed.");
                        createAlert("An unknown error occured.", "Your fields might be empty.");
                        break;
                    case 0:
                        // Student
                        System.out.println("Student logged in.");
                        mainScreen.getChildren().setAll(guiEventHandler.getStudentWindow());
                        break;
                    case 1:
                        // Parent
                        System.out.println("Parent logged in.");
                        mainScreen.getChildren().setAll(guiEventHandler.getParentWindow());
                        break;
                    case 2:
                        // Teacher
                        System.out.println("Teacher logged in.");
                        mainScreen.getChildren().setAll(guiEventHandler.getTeacherWindow());
                        break;
                    case 3:
                        // Admin
                        System.out.println("Admin logged in.");
                        mainScreen.getChildren().setAll(guiEventHandler.getAdminWindow());
                        break;
                    default:
                        System.out.println("Different role windows are not implemented yet");
                }
            } else {
                createAlert("An unknown error occured.", "Possible reasons:\nCould not connect to database.\nYour account does not exist.\nYour login and password does not match.\nLogin or password is empty.");
            }
        });
        new Thread(loginTask).start();
    }

    private void createAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void signUp(MouseEvent mouseEvent) {
        mainScreen.getChildren().setAll(guiEventHandler.getSignUpWindow());
    }
}
