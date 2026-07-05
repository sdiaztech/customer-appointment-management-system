package controller;

import model.Appointment;
import database.DatabaseAppointments;
import static database.DatabaseAppointments.getAllApptsForComparison;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewReportsController implements Initializable {
    @FXML
    private Button apptsReportButton;
    @FXML
    private Button schedulesReportButton;
    @FXML
    private Button usersReportButton;
    @FXML
    private Button viewApptsButton;
    @FXML
    private Button viewCustomersButton;
    @FXML
    private Button exitButton;
    @FXML
    private ComboBox<Month> monthDropdown;
    @FXML
    private ComboBox<String> apptTypeDropdown;
    @FXML
    private ComboBox<String> contactDropdown;
    @FXML
    private ComboBox<String> usersDropdown;
    @FXML
    private ListView<String> apptsListView;
    @FXML
    private ListView<String> schedulesListView;
    @FXML
    private ListView<String> usersListView;
    @FXML
    private Label totalsLabel;

    private Integer monthInput = null;
    private String typeInput = null;
    private Integer contactInput = null;
    private Integer userInput = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillContactComboBox();
        fillUsersComboBox();
        fillApptTypeComboBox();
        populateMonthComboBox();
    }

    public void apptsReportButtonListener() {
        hideSchedulesReportControls();
        hideUsersReportControls();
        apptTypeDropdown.setDisable(false);
        apptTypeDropdown.setVisible(true);
        monthDropdown.setDisable(false);
        monthDropdown.setVisible(true);
        apptsListView.setDisable(false);
        apptsListView.setVisible(true);
    }

    public void schedulesReportButtonListener() {
        hideApptsReportControls();
        hideUsersReportControls();
        contactDropdown.setDisable(false);
        contactDropdown.setVisible(true);
        schedulesListView.setDisable(false);
        schedulesListView.setVisible(true);

    }

    public void usersReportButtonListener() {
        hideApptsReportControls();
        hideSchedulesReportControls();
        usersDropdown.setDisable(false);
        usersDropdown.setVisible(true);
        usersListView.setDisable(false);
        usersListView.setVisible(true);
    }

    public void viewCustomersButtonListener(ActionEvent actionEvent) throws IOException {
        Parent viewCustomersFXML = FXMLLoader.load(getClass().getResource("../view/viewCustomerRecords.fxml"));
        Scene viewCustomersScene = new Scene(viewCustomersFXML, 800, 360);
        Stage viewCustomersStage = new Stage();
        viewCustomersStage.setTitle("View Customers");
        viewCustomersStage.setScene(viewCustomersScene);
        viewCustomersStage.show();

        Stage viewStage = (Stage)viewCustomersButton.getScene().getWindow();
        viewStage.close();
    }

    public void viewApptsButtonListener(ActionEvent actionEvent) throws IOException {
        Parent viewApptsFXML = FXMLLoader.load(getClass().getResource("../view/viewAppointments.fxml"));
        Scene viewApptsScene = new Scene(viewApptsFXML, 800, 360);
        Stage viewApptsStage = new Stage();
        viewApptsStage.setTitle("View Appointments");
        viewApptsStage.setScene(viewApptsScene);
        viewApptsStage.show();

        Stage customerStage = (Stage)viewApptsButton.getScene().getWindow();
        customerStage.close();

    }

    public void exitButtonListener() {
        Stage stage = (Stage)exitButton.getScene().getWindow();
        stage.close();
    }

    public void hideApptsReportControls() {
        apptTypeDropdown.setVisible(false);
        apptTypeDropdown.setDisable(true);
        apptsListView.setVisible(false);
        apptsListView.setDisable(true);
        monthDropdown.setVisible(false);
        monthDropdown.setDisable(true);
        totalsLabel.setText("");
    }

    public void hideSchedulesReportControls() {
        contactDropdown.setVisible(false);
        contactDropdown.setDisable(true);
        schedulesListView.setVisible(false);
        schedulesListView.setDisable(true);
    }

    public void hideUsersReportControls() {
        usersDropdown.setVisible(false);
        usersDropdown.setDisable(true);
        usersListView.setVisible(false);
        usersListView.setDisable(true);
        totalsLabel.setText("");
    }

    public void populateMonthComboBox() {
        Month[] monthsNumValuesArray = Month.values();
        ObservableList<Month> months = FXCollections.observableArrayList(monthsNumValuesArray);
        monthDropdown.setItems(months);
    }

    public void fillApptTypeComboBox() {
        ObservableList<String> apptTypesList =
                FXCollections.observableArrayList("Initial", "Planning", "De-Briefing", "Virtual", "Closing");
        apptTypeDropdown.setItems(apptTypesList);
    }

    public void fillContactComboBox() {
        ObservableList<String> contactsList = DatabaseAppointments.getContacts();
        contactDropdown.setItems(contactsList);
    }

    public void fillUsersComboBox() {
        ObservableList<String> userIdList = DatabaseAppointments.getUsers();
        usersDropdown.setItems(userIdList);
    }

    public void monthDropdownListener() {
        Month monthDropdownValue = monthDropdown.getValue();
        int monthDropdownInt = monthDropdownValue.getValue();
        monthInput = monthDropdownInt;

        if (typeInput != null && monthInput != null) {
            createDisplayApptReport();
        }
    }

    public void apptTypeComboBoxListener() {
        String apptTypeComboBoxValue = apptTypeDropdown.getValue();
        typeInput = apptTypeComboBoxValue;
        if (typeInput != null && monthInput != null) {
            createDisplayApptReport();
        }
    }

    public void createDisplayApptReport(){
        try {
            ObservableList<Appointment> allAppointments = getAllApptsForComparison();
            ObservableList<Appointment> filteredList =
                    allAppointments.stream().filter(appt -> appt.getStartMonthInt() + 1 == monthInput)
                            .filter(appt -> appt.getType().equals(typeInput))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));

            ObservableList<String> apptsFiltered = FXCollections.observableArrayList();
            filteredList.forEach(appointment ->
                    apptsFiltered.add("".concat("ID: " + appointment.getApptId() + System.lineSeparator())));
            apptsListView.setItems(apptsFiltered);
            totalsLabel.setText("Appointments Matching Criteria: " + String.valueOf(apptsFiltered.size()));

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void contactDropdownListener() {
        String contactDropdownInput = contactDropdown.getValue();
        contactInput = Integer.parseInt(contactDropdownInput.substring(0, 1));

        createDisplayScheduleReport();
    }

    public void createDisplayScheduleReport() {
        try {
            ObservableList<Appointment> allAppointments = getAllApptsForComparison();
            ObservableList<Appointment> filteredAppointments = allAppointments.stream()
                            .filter(appt -> appt.getContactId() == contactInput)
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
            ObservableList<String> apptInfo = FXCollections.observableArrayList();

            filteredAppointments.forEach(appt -> apptInfo.add("".concat(
                        "Appointment ID: " + appt.getApptId() + System.lineSeparator() +
                            " | Title: " + appt.getTitle() + System.lineSeparator() +
                            " | Type: " + appt.getType() + System.lineSeparator() +
                            " | Description: " + appt.getDescription() + System.lineSeparator() +
                            " | Start: " + appt.getStartTime() + System.lineSeparator() +
                            " | End: " + appt.getEndTime() + System.lineSeparator() +
                            " | Customer ID: " + appt.getCustomerId() + System.lineSeparator())));

            schedulesListView.setItems(apptInfo);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void usersDropdownListener() {
        String userDropdownInput = usersDropdown.getValue();
        userInput = Integer.parseInt(userDropdownInput.substring(0, 1));

        createDisplayUserReport();
    }

    public void createDisplayUserReport(){
        try {
            ObservableList<Appointment> allAppointments = getAllApptsForComparison();
            ObservableList<Appointment> filteredAppointments = allAppointments.stream()
                            .filter(appointment -> appointment.getUserId() == userInput)
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
            ObservableList<String> apptInfo = FXCollections.observableArrayList();
            filteredAppointments.forEach(appt -> apptInfo.add("".concat(
                        "Appointment ID: " + appt.getApptId() + System.lineSeparator() +
                            "Appointment title: " + appt.getTitle())));

            usersListView.setItems(apptInfo);
            totalsLabel.setText("Appointments by user: " + filteredAppointments.size());

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}