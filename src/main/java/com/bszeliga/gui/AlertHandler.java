package com.bszeliga.gui;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class AlertHandler {
    private Alert alert;
    public AlertHandler(Alert.AlertType alertType) {
        alert = new Alert(alertType);
        switch (alertType) {
            case NONE:
                //alert.setTitle("Dialog");
                break;
            case ERROR:
                //alert.setTitle("Error Dialog");
                break;
            case WARNING:
                //alert.setTitle("Warning Dialog");
                break;
            case INFORMATION:
                //alert.setTitle("Information Dialog");
                break;
            case CONFIRMATION:
                //alert.setTitle("Confirmation Dialog");
                break;
        }
    }

    public AlertHandler(Alert.AlertType alertType, String contentText) {
        this(alertType);
        setContentText(contentText);
    }

    public AlertHandler(Alert.AlertType alertType, String headerText, String contentText) {
        this(alertType);
        setHeaderText(headerText);
        setContentText(contentText);
    }

    public void setModality(Modality modality) {
        alert.initModality(modality);
    }

    public void setHeaderText(String headerText) {
        alert.setHeaderText(headerText);
    }

    public void setContentText(String contentText) {
        alert.setContentText(contentText);
    }

    public void show() {
        alert.show();
    }

    public void showAndWait() {
        alert.showAndWait();
    }
}
