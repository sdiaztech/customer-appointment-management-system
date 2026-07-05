package controller;

import model.Appointments;
import database.databaseAppointments;
import static database.databaseAppointments.getAllApptsForComparison;

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

public class viewReportsController implements Initializable {
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


    /** Initializes the Reports Menu
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate all comboBoxes
        fillContactComboBox();
        fillUsersComboBox();
        fillApptTypeComboBox();
        populateMonthComboBox();
    }

    /** Displays only the Appointments Report controls, all others are hidden */
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

    /**  Displays only the Schedules Report controls, all others are hidden  */
    public void schedulesReportButtonListener() {
        hideApptsReportControls();
        hideUsersReportControls();
        contactDropdown.setDisable(false);
        contactDropdown.setVisible(true);
        schedulesListView.setDisable(false);
        schedulesListView.setVisible(true);

    }

    /**  Displays only the Users Report controls, all others are hidden  */
    public void usersReportButtonListener() {
        hideApptsReportControls();
        hideSchedulesReportControls();
        usersDropdown.setDisable(false);
        usersDropdown.setVisible(true);
        usersListView.setDisable(false);
        usersListView.setVisible(true);
    }

    /** Handles click of 'View Customers' button. Opens 'View Customers' window.
     * @param actionEvent Click of button
     * @throws IOException if there is an error in locating the FXML file
     */
    public void viewCustomersButtonListener(ActionEvent actionEvent) throws IOException {
        // Open the ViewCustomers scene
        Parent viewCustomersFXML = FXMLLoader.load(getClass().getResource("../view/viewCustomerRecords.fxml"));
        Scene viewCustomersScene = new Scene(viewCustomersFXML, 800, 360);
        Stage viewCustomersStage = new Stage();
        viewCustomersStage.setTitle("View Customers");
        viewCustomersStage.setScene(viewCustomersScene);
        viewCustomersStage.show();

        // Close out the current scene
        Stage viewStage = (Stage)viewCustomersButton.getScene().getWindow();
        viewStage.close();
    }

    /** Handles click of 'View Appointments' button. Opens 'View Appointments' window.
     * @param actionEvent Click of button
     * @throws IOException if there is an error in locating the FXML file
     */
    public void viewApptsButtonListener(ActionEvent actionEvent) throws IOException {
        // Load the View Appointments FXML
        Parent viewApptsFXML = FXMLLoader.load(getClass().getResource("../view/viewAppointments.fxml"));
        // Create the new stage and scene
        Scene viewApptsScene = new Scene(viewApptsFXML, 800, 360);
        Stage viewApptsStage = new Stage();
        viewApptsStage.setTitle("View Appointments");
        viewApptsStage.setScene(viewApptsScene);
        viewApptsStage.show();

        // Close out the ViewCustomerRecords stage
        Stage customerStage = (Stage)viewApptsButton.getScene().getWindow();
        customerStage.close();

    }

    /** Handles click of the 'Exit' 'Button' */
    public void exitButtonListener() {
        Stage stage = (Stage)exitButton.getScene().getWindow();
        stage.close();
    }

    /** Disables/hides controls for the Appointments Report */
    public void hideApptsReportControls() {
        apptTypeDropdown.setVisible(false);
        apptTypeDropdown.setDisable(true);
        apptsListView.setVisible(false);
        apptsListView.setDisable(true);
        monthDropdown.setVisible(false);
        monthDropdown.setDisable(true);
        totalsLabel.setText("");
    }

    /** Disables/hides controls for the Schedules Report*/
    public void hideSchedulesReportControls() {
        contactDropdown.setVisible(false);
        contactDropdown.setDisable(true);
        schedulesListView.setVisible(false);
        schedulesListView.setDisable(true);
    }

    /** Disables/hides controls for the Users Report*/
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

    /** Fills the 'appointmentTypeComboBox' with the distinct types of meetings */
    public void fillApptTypeComboBox() {
        ObservableList<String> apptTypesList =
                FXCollections.observableArrayList("Initial", "Planning", "De-Briefing", "Virtual", "Closing");
        apptTypeDropdown.setItems(apptTypesList);
    }

    /** Fills the 'contactComboBox' with 'Contact's from the database */
    public void fillContactComboBox() {
        // Retrieve all contacts from the database
        ObservableList<String> contactsList = databaseAppointments.getContacts();
        // Populate contactComboBox
        contactDropdown.setItems(contactsList);
    }

    /** Populates the 'userIdComboBox' with the 'User's from the database*/
    public void fillUsersComboBox() {
        ObservableList<String> userIdList = databaseAppointments.getUsers();
        usersDropdown.setItems(userIdList);
    }

    /** Listens for changes to monthComboBox */
    public void monthDropdownListener() {
        // Get the value of the monthComboBox as an int
        Month monthDropdownValue = monthDropdown.getValue();
        int monthDropdownInt = monthDropdownValue.getValue();
        monthInput = monthDropdownInt;

        if (typeInput != null && monthInput != null) {
            createDisplayApptReport();
        }
        System.out.println(monthInput);
    }

    /** Listens for changes to appointmentTypeComboBox */
    public void apptTypeComboBoxListener() {
        String apptTypeComboBoxValue = apptTypeDropdown.getValue();
        typeInput = apptTypeComboBoxValue;
        if (typeInput != null && monthInput != null) {
            createDisplayApptReport();
        }
    }

    /** Creates and shows the Appointments Report.
     * LAMBDAS used - purpose of Stream API.
     */
    public void createDisplayApptReport(){
        try {
            // Get all appointments
            ObservableList<Appointments> allAppointments = getAllApptsForComparison();
            // Filter the allAppointments list. First, by the chosen month. Then, by the chosen type.
            ObservableList<Appointments> filteredList =
                    allAppointments.stream().filter(appt -> appt.getStartMonthInt() + 1 == monthInput)
                            .filter(appt -> appt.getType().equals(typeInput))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));

            System.out.println("Filtered list: " + filteredList);
            System.out.println("monthInput: " + monthInput);
            System.out.println("typeInput: " + typeInput);
            // Create a new list to hold the appointment information
            ObservableList<String> apptsFiltered = FXCollections.observableArrayList();
            // Take the filtered list and extract the appointment ID for each appointment
            filteredList.forEach(appointment ->
                    apptsFiltered.add("".concat("ID: " + appointment.getApptId() + System.lineSeparator())));
            // Set the items in the ListView
            apptsListView.setItems(apptsFiltered);
            // Update the message under the ListView
            totalsLabel.setText("Appointments Matching Criteria: " + String.valueOf(apptsFiltered.size()));

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Listens for changes to contactTypeComboBox. */
    public void contactDropdownListener() {
        String contactDropdownInput = contactDropdown.getValue();
        contactInput = Integer.parseInt(contactDropdownInput.substring(0, 1));
        System.out.println("Contact Choice: " + contactInput);

        createDisplayScheduleReport();
    }

    /** Creates and displays the schedule report.
     * LAMBDAS USED - purpose of Stream API.
     */
    public void createDisplayScheduleReport() {
        try {
            ObservableList<Appointments> allAppointments = getAllApptsForComparison();
            ObservableList<Appointments> filteredAppointments = allAppointments.stream()
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

            System.out.println("All appointments: " + allAppointments);
            schedulesListView.setItems(apptInfo);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Listens for changes to 'usersComboBox' */
    public void usersDropdownListener() {
        String userDropdownInput = usersDropdown.getValue();
        userInput = Integer.parseInt(userDropdownInput.substring(0, 1));
        System.out.println("User Choice: " + userInput);

        createDisplayUserReport();
    }

    /** Create and display the User Report.
     * Displays a list of chosen appointments and counts total appointments scheduled by the User.
     * LAMBDAS USED - purpose of Stream API
     */
    public void createDisplayUserReport(){
        try {
            ObservableList<Appointments> allAppointments = getAllApptsForComparison();
            ObservableList<Appointments> filteredAppointments = allAppointments.stream()
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