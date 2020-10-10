package com.bszeliga.gui.teacher;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TeacherWindow extends GridPane {
    private GridPane mainScreen;
    private VBox mainScreenMenu;

    public TeacherWindow(GridPane mainScreen, VBox mainScreenMenu) {
        this.mainScreen = mainScreen;
        this.mainScreenMenu = mainScreenMenu;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent actionEvent) {
        // safely disconnect connected user from database etc.
        // make sure the object closes itself in GUIEventHandler

        // change the window into Login window.
        mainScreen.getChildren().setAll(mainScreenMenu);
    }
}
