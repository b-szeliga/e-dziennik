package com.bszeliga.gui;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import lombok.Getter;

public class AlertCreator {
    @Getter
    private final Alert alert;
    public AlertCreator(Alert.AlertType alertType) {
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

    public AlertCreator(Alert.AlertType alertType, String contentText) {
        this(alertType);
        setContentText(contentText);
    }

    public AlertCreator(Alert.AlertType alertType, String headerText, String contentText) {
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
}
