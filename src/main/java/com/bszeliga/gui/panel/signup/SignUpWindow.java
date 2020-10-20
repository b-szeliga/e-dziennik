package com.bszeliga.gui.panel.signup;

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

import java.io.IOException;
import java.net.URL;
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

    public void register(ActionEvent actionEvent) {

    }

    public void goBack(ActionEvent actionEvent) {
        // change the window into Login window.
        mainScreen.getChildren().setAll(mainScreenMenu);
    }
}