package com.bszeliga.gui.panel;

import com.bszeliga.main.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class PanelWindow extends GridPane {
    private final GridPane mainScreen;
    private final VBox mainScreenMenu;
    @Getter
    @FXML
    private TabPane mainLoggedInPane;
    @FXML
    private Tab mainPanel;
    @FXML
    private Tab adminPanel;
    @FXML
    private Tab gradesPanel;
    @FXML
    private Tab timetablePanel;
    @FXML
    private Tab homeworkPanel;
    @FXML
    private Tab calendarPanel;
    @FXML
    private Tab gradeBookPanel;
    @FXML
    private Tab warningPanel;
    @FXML
    private Tab optionsPanel;

    @FXML
    private TableView<TableRow> usersTableView;
    @FXML
    private TableColumn<TableRow, String> idCol;
    @FXML
    private TableColumn<TableRow, String> nameCol;
    @FXML
    private TableColumn<TableRow, String> lastnameCol;
    @FXML
    private TableColumn<TableRow, String> roleCol;
    @FXML
    private TableColumn<TableRow, String> schoolCol;
    @FXML
    private TableColumn<TableRow, String> isRegisteredCol;

    private Database db;

    public PanelWindow(GridPane mainScreen, VBox mainScreenMenu) {
        this.mainScreen = mainScreen;
        this.mainScreenMenu = mainScreenMenu;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PanelWindow.fxml"));
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

    public PanelWindow asStudent() {
        PanelWindow copy = this;
        copy.getMainLoggedInPane().getTabs().remove(adminPanel);
        copy.getMainLoggedInPane().getTabs().remove(gradeBookPanel);
        copy.getMainLoggedInPane().getTabs().remove(warningPanel);
        return copy;
    }

    public PanelWindow asTeacher() {
        PanelWindow copy = this;
        copy.getMainLoggedInPane().getTabs().remove(adminPanel);
        return copy;
    }

    public PanelWindow asParent() {
        PanelWindow copy = this;
        copy.getMainLoggedInPane().getTabs().remove(adminPanel);
        copy.getMainLoggedInPane().getTabs().remove(gradeBookPanel);
        return copy;
    }

    public PanelWindow asAdmin() {
        return this;
    }

    public void refresh(ActionEvent actionEvent) throws SQLException {
        // reloads all users from database into TableView
        System.out.println("Refreshing tableview of users");

        final Connection conn = db.getConnection();
        Statement stmt = conn.createStatement();

        String sql = "SELECT id, name, lastname, role, school FROM users";
        ResultSet rs = stmt.executeQuery(sql);

        if (Objects.nonNull(rs)) {
            ObservableList<TableRow> users = FXCollections.observableArrayList();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                int role = rs.getInt("role");
                String school = rs.getString("school");
                users.add(new TableRow(id, name, lastname, role, school));
            }
            idCol.setCellValueFactory(
                    new PropertyValueFactory<TableRow, String>("id")
            );
            nameCol.setCellValueFactory(
                    new PropertyValueFactory<TableRow, String>("name")
            );
            lastnameCol.setCellValueFactory(
                    new PropertyValueFactory<TableRow, String>("lastname")
            );
            roleCol.setCellValueFactory(
                    new PropertyValueFactory<TableRow, String>("role")
            );
            schoolCol.setCellValueFactory(
                    new PropertyValueFactory<TableRow, String>("school")
            );
            usersTableView.setItems(users);
        } else {
            // handle situation where database is empty or there was an error.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("A database error occurred.");
            alert.setContentText("Database is empty!\nUnknown error occured.");
            alert.showAndWait();
        }
    }

    public void sendDatabase(Database db) {
        this.db = db;
    }
}