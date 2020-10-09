package com.bszeliga.gui;

import com.bszeliga.home.HomeWindow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class GUIEventHandler {
    @Getter
    private HomeWindow homeWindow;

    public GUIEventHandler(GridPane mainScreen, VBox mainScreenMenu) {
        homeWindow = new HomeWindow(mainScreen, mainScreenMenu);
    }
}
