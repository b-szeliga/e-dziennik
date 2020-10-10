package com.bszeliga.gui;

import com.bszeliga.gui.headmaster.HeadmasterWindow;
import com.bszeliga.gui.parent.ParentWindow;
import com.bszeliga.gui.student.StudentWindow;
import com.bszeliga.gui.teacher.TeacherWindow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class GUIEventHandler {
    private StudentWindow studentWindow;
    private TeacherWindow teacherWindow;
    private HeadmasterWindow headmasterWindow;
    private ParentWindow parentWindow;

    public GUIEventHandler(GridPane mainScreen, VBox mainScreenMenu) {
        // nie tworzyć wszystkich obiektów na raz, tylko sprawdzić kto się loguję i na tej podstawie tworzyć odpowiedni obiekt gui
        // tymczasowo tworzymy wszystkie
        studentWindow = new StudentWindow(mainScreen, mainScreenMenu);
        teacherWindow = new TeacherWindow(mainScreen, mainScreenMenu);
        headmasterWindow = new HeadmasterWindow(mainScreen, mainScreenMenu);
        parentWindow = new ParentWindow(mainScreen, mainScreenMenu);
    }
}
