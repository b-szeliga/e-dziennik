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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // make the button mainTitle possible to configure in Main.css
        mainTitle.getStyleClass().add("mainTitleLabel");
        // create object of GUI Helper class.
        guiEventHandler = new GUIEventHandler(mainScreen, mainScreenMenu);
    }

    public void loginUser(ActionEvent actionEvent) {
        // button clicked, check ID and Password in this method.

        // encrypting example REPLACE ASAP
        // WIP: replace example with JDBC request

        // password is encrypted when user registers (requires account verification!)
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword("admin"); // replace with password from registration

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
