package com.bszeliga.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        root.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());
        primaryStage.setTitle("e-Dziennik");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
