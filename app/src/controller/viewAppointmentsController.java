package controller;

import database.databaseQuery;
import database.JDBC;
import model.Appointments;
import main.Main;
import static database.databaseAppointments.getAllAppts;
import static database.databaseAppointments.getAllApptsForComparison;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class viewAppointmentsController implements Initializable {
    public static ObservableList<Appointments> clientAppointments = FXCollections.observableArrayList();

    @FXML
    private TableView<Appointments> viewApptsTableView;
    @FXML
    private TableColumn<Appointments,Number> colAppointmentId;
    @FXML
    private TableColumn<Appointments,String> colTitle;
    @FXML
    private TableColumn<Appointments,String> colDescription;
    @FXML
    private TableColumn<Appointments,String> colType;
    @FXML
    private TableColumn<Appointments,String> colLocation;
    @FXML
    private TableColumn<Appointments,Timestamp> colStart;
    @FXML
    private TableColumn<Appointments,Timestamp> colEnd;
    @FXML
    private TableColumn<Appointments,Number> colContactId;
    @FXML
    private TableColumn<Appointments,Number> colUserId;
    @FXML
    private TableColumn<Appointments,Number> colCustomerId;


    @FXML
    private Button exitButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button viewCustomersButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;
    @FXML
    private RadioButton monthViewRadioButton;
    @FXML
    private RadioButton weekViewRadioButton;
    @FXML
    private RadioButton allViewRadioButton;
    @FXML
    private Label deleteApptConfirmationLabel;

    /** Initializes the View Appointments window
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("View Appointments Opening");
        try {
            displayAppts();
        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    /** Obtains the stored 'Appointment' records and displays them within a 'TableView'
     * LAMBDAS used - shortens the code, and forego the type declaration of each column
     * @throws SQLException if the SQL is misshapen
     */
    public void displayAppts() throws SQLException, ParseException {
        System.out.println("Display appointment records.");
        // Obtain all Appointments from the database
        clientAppointments = getAllAppts();

        // Create the columns for the TableView
        TableColumn<Appointments,Number> columnApptId = new TableColumn<>("Appointment_ID");
        TableColumn<Appointments,String> columnTitle = new TableColumn<>("Title");
        TableColumn<Appointments,String> columnDescription = new TableColumn<>("Description");
        TableColumn<Appointments,String> columnLocation = new TableColumn<>("Location");
        TableColumn<Appointments,String> columnType = new TableColumn<>("Type");
        TableColumn<Appointments,String> columnStartTime = new TableColumn<>("Start");
        TableColumn<Appointments,String> columnEndTime = new TableColumn<>("End");
        TableColumn<Appointments,Number> columnCustomerId = new TableColumn<>("Customer_ID");
        TableColumn<Appointments,Number> columnUserId = new TableColumn<>("User_ID");
        TableColumn<Appointments,Number> columnContactId = new TableColumn<>("Contact_ID");

        // Set the cell values for each column
        columnApptId.setCellValueFactory(cell -> cell.getValue().apptIdProperty());
        columnTitle.setCellValueFactory(cell -> cell.getValue().titleProperty());
        columnDescription.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        columnLocation.setCellValueFactory(cell -> cell.getValue().locationProperty());
        columnType.setCellValueFactory(cell -> cell.getValue().typeProperty());
        columnStartTime.setCellValueFactory(cell -> cell.getValue().startTimeProperty());
        columnEndTime.setCellValueFactory(cell -> cell.getValue().endTimeProperty());
        columnCustomerId.setCellValueFactory(cell -> cell.getValue().customerIdProperty());
        columnUserId.setCellValueFactory(cell -> cell.getValue().userIdProperty());
        columnContactId.setCellValueFactory(cell -> cell.getValue().contactIdProperty());

        // Set the items in the TableView
        viewApptsTableView.setItems(clientAppointments);
        viewApptsTableView.getColumns().addAll(columnApptId, columnTitle, columnDescription, columnLocation,
                columnType, columnStartTime, columnEndTime, columnCustomerId, columnUserId, columnContactId);

    }

    /** Switches the window to show all Customer Records
     * @param actionEvent Click of button
     * @throws IOException if there is an error locating the FXML file
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

    /** Click of 'Exit' button closes the database connection and exits the application.
     * @param actionEvent Click of 'Exit' button
     */
    public void exitButtonListener(ActionEvent actionEvent) {
        JDBC.closeConnection();
        System.out.println("Exit Clicked!");
        Platform.exit();
    }

    /** Click of 'Delete' Button deletes the selected 'Appointment'
     * @param actionEvent Click of 'Delete' 'Button'
     * @throws SQLException if SQL is misshapen
     */
    public void deleteButtonListener(ActionEvent actionEvent) throws SQLException {
        // Get the apptId from the selected Appointment
        Appointments deleteSelection = viewApptsTableView.getSelectionModel().getSelectedItem();
        int apptId = deleteSelection.getApptId();

        // Create a delete confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setContentText("Are you sure you want to delete appointment with ID " + apptId + "?");
        Optional<ButtonType> input = alert.showAndWait();

        // Handle click of 'Ok' Button
        if ((input.isPresent()) && (input.get() == ButtonType.OK)) {
            System.out.println("Ok clicked.");

            // Use SQL query to delete the customer at that ID
            String sqlDeleteQuery = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?";

            // Prepare the statement
            databaseQuery.setPreparedStatement(JDBC.getConnection(), sqlDeleteQuery);

            PreparedStatement preparedStatement = databaseQuery.getPreparedStatement();

            // Execute the statement
            preparedStatement.setString(1, String.valueOf(apptId));
            preparedStatement.execute();

            // Remove from ObservableList to update TableView
            clientAppointments.remove(deleteSelection);

            // Display Delete Confirmation
            deleteApptConfirmationLabel.setText("Appointment with ID " + apptId + " cancelled.");

            viewApptsTableView.refresh();
        }

        // Handle click of 'Cancel' button
        if ((input.isPresent()) && (input.get() == ButtonType.NO)) {
            System.out.println("Cancel button clicked.");
        }
    }

    /** Handles click of 'Add' 'Button', opens the Add Appointment form.
     * @param actionEvent Click of 'Add' 'Button'
     */
    public void addButtonListener(ActionEvent actionEvent) {
        try {
            Parent addApptFXML = FXMLLoader.load(getClass().getResource("../view/addAppointment.fxml"));
            Stage addApptStage = new Stage();
            Scene addApptScene = new Scene(addApptFXML, 360, 600);
            addApptStage.setTitle("Add Appointment");
            addApptStage.setScene(addApptScene);
            addApptStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** Handles click of 'Update' 'Button', opens the Update Appointment form.
     * @param actionEvent Click of 'Update' 'Button'
     */
    public void updateButtonListener(ActionEvent actionEvent) {

        Main.selectedAppointments = viewApptsTableView.getSelectionModel().getSelectedItem();
        System.out.println("Selected Appointment: " + Main.selectedAppointments);

        Parent updateApptFXML = null;
        try {
            updateApptFXML = FXMLLoader.load(getClass().getResource("../view/updateAppointment.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Stage updateAppointmentStage = new Stage();
        Scene updateAppointmentScene = new Scene(updateApptFXML, 360, 600);
        updateAppointmentStage.setTitle("Update Appointment");
        updateAppointmentStage.setScene(updateAppointmentScene);
        updateAppointmentStage.show();
    }

    /** Displays all scheduled appointments
     * @param actionEvent Click of 'All' 'RadioButton'
     */
    public void allViewRadioButtonListener(ActionEvent actionEvent) {
        deleteApptConfirmationLabel.setText("");
        // Clear the TableView
        viewApptsTableView.getItems().clear();
        try {
            displayAppts();
        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    /** Displays scheduled appointments in the current month.
     * LAMBDA used - to shorten the code and forego a return statement and declaring the type 'Appointment'
     * @param actionEvent Click of 'Month' 'RadioButton'
     */
    public void monthViewRadioButtonListener(ActionEvent actionEvent) {
        deleteApptConfirmationLabel.setText("");
        // Check current month
        Month currMonth = LocalDate.now().getMonth();
        int currMonthInt = currMonth.getValue();
        System.out.println(currMonth + " : " + currMonthInt);

        // Filter ObservableList clientAppts for Appointments occurring this month
        try {
            // Create a list containing all appointments
            ObservableList<Appointments> allAppointments = getAllApptsForComparison();
            // Create a list from the allAppts list by filtering
            ObservableList<Appointments> apptsThisMonth =
                    allAppointments.stream().filter(appt -> appt.getStartMonthInt() + 1 == currMonthInt).collect(Collectors.toCollection(FXCollections::observableArrayList));
            // Clear the TableView
            viewApptsTableView.getColumns().clear();

            // Create the columns for the TableView
            TableColumn<Appointments,Number> columnApptId = new TableColumn<>("Appointment_ID");
            TableColumn<Appointments,String> columnTitle = new TableColumn<>("Title");
            TableColumn<Appointments,String> columnDescription = new TableColumn<>("Description");
            TableColumn<Appointments,String> columnLocation = new TableColumn<>("Location");
            TableColumn<Appointments,String> columnType = new TableColumn<>("Type");
            TableColumn<Appointments,String> columnStart = new TableColumn<>("Start");
            TableColumn<Appointments,String> columnEnd = new TableColumn<>("End");
            TableColumn<Appointments,Number> columnCustomerId = new TableColumn<>("Customer_ID");
            TableColumn<Appointments,Number> columnUserId = new TableColumn<>("User_ID");
            TableColumn<Appointments,Number> columnContactId = new TableColumn<>("Contact_ID");

            // Set the cell values for each column
            columnApptId.setCellValueFactory(cell -> cell.getValue().apptIdProperty());
            columnTitle.setCellValueFactory(cell -> cell.getValue().titleProperty());
            columnDescription.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
            columnLocation.setCellValueFactory(cell -> cell.getValue().locationProperty());
            columnType.setCellValueFactory(cell -> cell.getValue().typeProperty());
            columnStart.setCellValueFactory(cell -> cell.getValue().startTimeProperty());
            columnEnd.setCellValueFactory(cell -> cell.getValue().endTimeProperty());
            columnCustomerId.setCellValueFactory(cell -> cell.getValue().customerIdProperty());
            columnUserId.setCellValueFactory(cell -> cell.getValue().userIdProperty());
            columnContactId.setCellValueFactory(cell -> cell.getValue().contactIdProperty());

            // Set the items in the TableView
            viewApptsTableView.setItems(clientAppointments);
            viewApptsTableView.getColumns().addAll(columnApptId, columnTitle, columnDescription, columnLocation,
                    columnType, columnStart, columnEnd, columnCustomerId, columnUserId, columnContactId);

            // Set the filtered items in the TableView
            viewApptsTableView.setItems(apptsThisMonth);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /** Displays scheduled appointments in the current week.
     * LAMBDA used - to shorten the code and forego a return statement and declaring the type * 'Appointment'
     * @param actionEvent Click of 'Week' 'RadioButton'
     */
    public void weekViewRadioButtonListener(ActionEvent actionEvent)
    {
        deleteApptConfirmationLabel.setText("");
        // Create a Calendar Object and set the current Date on it
        Calendar calendar = Calendar.getInstance();
        // Get the current week number from the calendar
        int currentWeekInt = calendar.get(Calendar.WEEK_OF_YEAR);
        System.out.println("Current week int: " + currentWeekInt);

        try {
            // Create a new list containing all appointments
            ObservableList<Appointments> allAppointments = getAllApptsForComparison();
            // Create a new list by filtering for appointments that match the current week
            ObservableList<Appointments> appointmentsThisWeek =
                    allAppointments.stream().filter(appt -> appt.getStartWeek() == currentWeekInt).collect(Collectors.toCollection(FXCollections::observableArrayList));
            // Clear the TableView
            viewApptsTableView.getColumns().clear();

            // Create the columns for the TableView
            TableColumn<Appointments,Number> columnApptId = new TableColumn<>("Appointment_ID");
            TableColumn<Appointments,String> columnTitle = new TableColumn<>("Title");
            TableColumn<Appointments,String> columnDescription = new TableColumn<>("Description");
            TableColumn<Appointments,String> columnLocation = new TableColumn<>("Location");
            TableColumn<Appointments,String> columnType = new TableColumn<>("Type");
            TableColumn<Appointments,String> columnStart = new TableColumn<>("Start");
            TableColumn<Appointments,String> columnEnd = new TableColumn<>("End");
            TableColumn<Appointments,Number> columnCustomerId = new TableColumn<>("Customer_ID");
            TableColumn<Appointments,Number> columnUserId = new TableColumn<>("User_ID");
            TableColumn<Appointments,Number> columnContactId = new TableColumn<>("Contact_ID");

            // Set the cell values for each column
            columnApptId.setCellValueFactory(cell -> cell.getValue().apptIdProperty());
            columnTitle.setCellValueFactory(cell -> cell.getValue().titleProperty());
            columnDescription.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
            columnLocation.setCellValueFactory(cell -> cell.getValue().locationProperty());
            columnType.setCellValueFactory(cell -> cell.getValue().typeProperty());
            columnStart.setCellValueFactory(cell -> cell.getValue().startTimeProperty());
            columnEnd.setCellValueFactory(cell -> cell.getValue().endTimeProperty());
            columnCustomerId.setCellValueFactory(cell -> cell.getValue().customerIdProperty());
            columnUserId.setCellValueFactory(cell -> cell.getValue().userIdProperty());
            columnContactId.setCellValueFactory(cell -> cell.getValue().contactIdProperty());

            // Set the items in the TableView
            viewApptsTableView.setItems(clientAppointments);
            viewApptsTableView.getColumns().addAll(columnApptId, columnTitle, columnDescription, columnLocation,
                    columnType, columnStart, columnEnd, columnCustomerId, columnUserId, columnContactId);

            // Set the filtered items in the TableView
            viewApptsTableView.setItems(appointmentsThisWeek);
        }

        catch (SQLException e){
            e.printStackTrace();
        }
    }
}