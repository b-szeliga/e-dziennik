package com.bszeliga.gui;

import com.bszeliga.gui.panel.PanelWindow;
import com.bszeliga.gui.panel.signup.SignUpWindow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.sql.SQLException;

@Getter
public class GUIEventHandler {
    private PanelWindow panelWindow;
    private SignUpWindow signUpWindow;

    public GUIEventHandler(GridPane mainScreen, VBox mainScreenMenu){
        panelWindow = new PanelWindow(mainScreen, mainScreenMenu);
        signUpWindow = new SignUpWindow(mainScreen, mainScreenMenu);
    }

    public PanelWindow getStudentWindow() {
        return panelWindow.asStudent();
    }

    public PanelWindow getTeacherWindow() {
        return panelWindow.asTeacher();
    }

    public PanelWindow getParentWindow() {
        return panelWindow.asParent();
    }

    public PanelWindow getAdminWindow() {
        return panelWindow.asAdmin();
    }
}