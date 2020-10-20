package com.bszeliga.gui.panel;

import com.bszeliga.gui.AlertHandler;
import com.bszeliga.gui.TableRow;
import com.bszeliga.logic.ValidateTextField;
import com.bszeliga.gui.database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PanelWindow extends GridPane implements Initializable {
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
    private TableColumn<TableRow, String> verifiedCol;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private TextField schoolField;
    @FXML
    private RadioButton trueRadioButton;
    @FXML
    private RadioButton falseRadioButton;

    private Database db;
    private ValidateTextField validateTextField;
    private ObservableList<TableRow> users;
    private List<String> choiceBoxValues = new ArrayList<String>();

    int id = 0;
    String name = "";
    String lastname = "";
    String school = null;
    int role = 0;
    boolean isVerified = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // check idField regex, only numbers allowed.
        validateTextField = new ValidateTextField(idField);
        idField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateTextField.replaceLettersToNumbers(newValue);
        });

        // group radiobuttons
        final ToggleGroup group = new ToggleGroup();
        trueRadioButton.setToggleGroup(group);
        falseRadioButton.setToggleGroup(group);


        // add values to choiceBoxValues
        choiceBoxValues.add("Student");
        choiceBoxValues.add("Rodzic");
        choiceBoxValues.add("Nauczyciel");
        choiceBoxValues.add("Admin");

        // add values to roleChoiceBox
        roleChoiceBox.setItems(FXCollections.observableArrayList(choiceBoxValues));
    }

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

        String sql = "SELECT id, name, lastname, role, school, verified FROM users";
        ResultSet rs = stmt.executeQuery(sql);

        if (Objects.nonNull(rs)) {
            users = FXCollections.observableArrayList();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                int role = rs.getInt("role");
                String school = rs.getString("school");
                boolean verified = rs.getBoolean("verified");
                users.add(new TableRow(id, name, lastname, role, school, verified));
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
            verifiedCol.setCellValueFactory(
                    new PropertyValueFactory<TableRow, String>("verified")
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

    public void fetchID(ActionEvent actionEvent) throws SQLException {
        // if idField is not a null and is not empty
        boolean wasNotFound = true;
        if (!Objects.isNull(idField.getText()) && !idField.getText().isEmpty()) {
            System.out.println("Fetch id: " + idField.getText());

            // refresh user list in TableView
            refresh(new ActionEvent()); // not sure if that's the best practice... and then throw SQLException.

            // if idField's number is higher than users size
            if (Integer.parseInt(idField.getText()) > (users.size() + 1)) {
                // wrong id provided.
                System.out.println("Wrong ID provided.");
                // show warning alert
                AlertHandler alertHandler = new AlertHandler(Alert.AlertType.WARNING, "Wrong ID provided.");
                alertHandler.showAndWait();
                return;
            }
            for (int i = 0; i < users.size(); i++) {
                id = users.get(i).getId(); // get id from every user in database
                // check if id is equal to id in idField
                if (id == Integer.parseInt(idField.getText())) {
                    // it is equal, get all user info
                    name = users.get(i).getName();
                    lastname = users.get(i).getLastname();
                    school = users.get(i).getSchool();
                    role = users.get(i).getRole();
                    isVerified = users.get(i).isVerified();
                    // update all controls
                    nameField.setText(name);
                    lastnameField.setText(lastname);
                    schoolField.setText(school);
                    switch (role) {
                        case 0:
                            roleChoiceBox.setValue(choiceBoxValues.get(0));
                            break;
                        case 1:
                            roleChoiceBox.setValue(choiceBoxValues.get(1));
                            break;
                        case 2:
                            roleChoiceBox.setValue(choiceBoxValues.get(2));
                            break;
                        case 3:
                            roleChoiceBox.setValue(choiceBoxValues.get(3));
                            break;
                    }
                    trueRadioButton.setSelected(isVerified);
                    falseRadioButton.setSelected(!isVerified);
                    wasNotFound = false;
                    break;
                }
            }
            if (wasNotFound) {
                // no one with that id was found.
                System.out.println("No one with that id was found.");
                // show warning alert
                AlertHandler alertHandler = new AlertHandler(Alert.AlertType.WARNING, "No one was found.");
                alertHandler.showAndWait();
            }
        }
    }

    public void update(ActionEvent actionEvent) throws SQLException {
        if (id != 0) {
            System.out.println("Updating user: " + nameField.getText() + " " + lastnameField.getText());

            final Connection conn = db.getConnection();
            Statement stmt = conn.createStatement();

            int verifiedAsInt;
            if (trueRadioButton.isSelected()) {
                verifiedAsInt = 1;
            } else {
                verifiedAsInt = 0;
            }

            int choiceBoxValueAsInt = 0;
            switch (roleChoiceBox.getValue()) {
                case "Student":
                    choiceBoxValueAsInt = 0;
                    break;
                case "Rodzic":
                    choiceBoxValueAsInt = 1;
                    break;
                case "Nauczyciel":
                    choiceBoxValueAsInt = 2;
                    break;
                case "Admin":
                    choiceBoxValueAsInt = 3;
                    break;
            }

            String sql = "UPDATE users SET name = \"" + nameField.getText() + "\", lastname = \"" + lastnameField.getText() + "\", school = \"" + schoolField.getText() + "\", role = \"" + choiceBoxValueAsInt + "\", verified = \"" + verifiedAsInt + "\" WHERE id = \"" + id + "\"";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        }
        refresh(new ActionEvent());
    }
}