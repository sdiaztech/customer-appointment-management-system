package controller;

import model.Customer;
import model.Appointments;
import main.Main;
import database.databaseAppointments;
import database.databaseCustomerRecords;
import database.databaseQuery;
import database.JDBC;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static main.Main.chosenCustomer;

public class viewCustomersController implements Initializable {
    public static ObservableList<Customer> customerRecords;

    @FXML
    public TableView<Customer> customerRecordsTableView;
    @FXML
    private TableColumn<Customer,Integer> columnCustomerID;
    @FXML
    private TableColumn<Customer,String> columnCustomerName;
    @FXML
    private TableColumn<Customer,String> columnAddress;
    @FXML
    private TableColumn<Customer,String> columnPostalCode;
    @FXML
    private TableColumn<Customer,String> columnDivision;
    @FXML
    private TableColumn<Customer,String> columnCountry;
    @FXML
    private TableColumn<Customer,String> columnPhone;
    @FXML
    private Button exitButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button viewApptsButton;
    @FXML
    private Button viewReportsButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;
    @FXML
    private Label deleteConfirmationLabel;

    /** Initializes the View Customer Records Menu
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("View Customer Records Initialize");
        try {
            displayCustomerRecords();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        checkForUpcomingAppointments();
    }

    /** Obtains the stored customer records from the database and displays it within the 'TableView'
     * @throws SQLException if SQL is misshapen
     */
    public void displayCustomerRecords() throws SQLException {
        System.out.println("Display customer records.");

        // Obtain all customer records from the database
        customerRecords = databaseCustomerRecords.getAllCustomerRecords();
        Main.currId = customerRecords.size();

        // Create the columns for the TableView
        TableColumn<Customer,Integer> columnCustomerID = new TableColumn<>("ID");
        TableColumn<Customer,String> columnCustomerName = new TableColumn<>("Name");
        TableColumn<Customer,String> columnAddress = new TableColumn<>("Address");
        TableColumn<Customer,String> columnPostalCode = new TableColumn<>("Postal Code");
        TableColumn<Customer,String> columnDivision = new TableColumn<>("Division");
        TableColumn<Customer,String> columnCountry = new TableColumn<>("Country");
        TableColumn<Customer,String> columnPhone = new TableColumn<>("Phone");

        // Set the cell values for each column
        columnCustomerID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
        columnCustomerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        columnPostalCode.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        columnDivision.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        columnCountry.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        customerRecordsTableView.setItems(customerRecords);
        // Add the columns to the TableView
        customerRecordsTableView.getColumns().addAll(columnCustomerID, columnCustomerName, columnAddress,
                columnPostalCode, columnDivision, columnCountry, columnPhone);
    }

    /** Click of 'Exit' button closes the database connection and exits the application.
     * @param actionEvent Click of button
     */
    public void exitButtonListener(ActionEvent actionEvent) {
        JDBC.closeConnection();
        System.out.println("Exit Clicked!");
        Platform.exit();
    }

    /** Click of 'delete' button deletes the selected customer
     * @param actionEvent Click of button
     */
    public void deleteButtonListener(ActionEvent actionEvent) throws IOException, SQLException {
        // Get the customerId from the selected customer
        Customer deleteChoice = customerRecordsTableView.getSelectionModel().getSelectedItem();
        int customerId = deleteChoice.getCustomerId();
        String customerName = deleteChoice.getCustomerName();

        //Confirmation alert to delete customer
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer Confirmation");
        String deleteConfirmationText = "Are you sure you want to delete customer " + customerName + "?";
        alert.setContentText(deleteConfirmationText);

        Optional<ButtonType> reply = alert.showAndWait();

        // Click of the 'ok' button
        if ((reply.isPresent()) && (reply.get() == ButtonType.OK)) {
            System.out.println("Ok clicked.");

            // Use SQL query to delete the customer at that ID
            String sqlDeleteQuery = "DELETE FROM client_schedule.customers WHERE Customer_ID = ?";

            // Prepare the statement
            databaseQuery.setPreparedStatement(JDBC.getConnection(), sqlDeleteQuery);
            PreparedStatement preparedStatement = databaseQuery.getPreparedStatement();

            // Execute the statement
            preparedStatement.setString(1, String.valueOf(customerId));
            preparedStatement.execute();

            // Remove from ObservableList to update TableView
            customerRecords.remove(deleteChoice);

            // Display Delete Confirmation
            deleteConfirmationLabel.setText("Customer " + deleteChoice.getCustomerName() + " has been deleted!");
        }

        // Click of the 'No' button
        if ((reply.isPresent()) && (reply.get() == ButtonType.NO)) {
            System.out.println("Cancel clicked!");
        }
    }

    /** Click of 'Add' prompts 'addCustomer' window
     * @param actionEvent Click of button
     * @throws IOException if there is a problem locating the FXML file.
     */
    public void addCustomerButtonListener(ActionEvent actionEvent) throws IOException {
        // Load the Add Customer FXML document
        Parent addCustomerFXML = null;
        try {
            addCustomerFXML = FXMLLoader.load(getClass().getResource("../view/addCustomer.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Create and display the new Stage and Scene
        try {
            Scene addCustomerScene = new Scene(addCustomerFXML, 360, 600);
            Stage addCustomerStage = new Stage();
            addCustomerStage.setTitle("Add Customer");
            addCustomerStage.setScene(addCustomerScene);
            addCustomerStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Click of 'Update' button prompts 'updateCustomer' window
     * @param actionEvent Click of button
     */
    public void updateButtonListener(ActionEvent actionEvent) {
        // Get the customer that is selected in the TableView
        chosenCustomer = customerRecordsTableView.getSelectionModel().getSelectedItem();
        System.out.println(chosenCustomer);

        // Load Update Customer FXML
        Parent updateCustomerFXML = null;
        try {
            updateCustomerFXML = FXMLLoader.load(getClass().getResource("../view/updateCustomer.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Create a new Scene and Stage and launch them
        Scene updateCustomerScene = new Scene(updateCustomerFXML, 360, 600);
        Stage updateCustomerStage = new Stage();
        updateCustomerStage.setTitle("Update Customer");
        updateCustomerStage.setScene(updateCustomerScene);
        updateCustomerStage.show();

    }

    /** Click of 'View Appointments' button opens the 'View Appointments' window
     * @param actionEvent Click of button
     */
    public void viewApptsButtonListener(ActionEvent actionEvent) throws IOException {
        // Load View Appointments FXML
        Parent viewAppointmentsFXML = FXMLLoader.load(getClass().getResource("../view/viewAppointments.fxml"));
        // Create the new stage and scene
        Scene viewAppointmentsScene = new Scene(viewAppointmentsFXML, 800, 360);
        Stage viewAppointmentsStage = new Stage();
        viewAppointmentsStage.setTitle("View Appointments");
        viewAppointmentsStage.setScene(viewAppointmentsScene);
        viewAppointmentsStage.show();

        // Close out the ViewCustomerRecords stage
        Stage customerStage = (Stage)viewApptsButton.getScene().getWindow();
        customerStage.close();

    }

    /** Displays an alert for upcoming appointments within 15 minutes of the current time. */
    public void checkForUpcomingAppointments()
    {
        // Create a Timestamp of the current time in UTC
        ZonedDateTime currUserZonedTime = ZonedDateTime.now(ZoneId.of(TimeZone.getDefault().getID()));
        Timestamp currTimeUTC = Timestamp.from(currUserZonedTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant());

        Instant addFifteenMins = currTimeUTC.toInstant().plus(Duration.ofMinutes(15));

        System.out.println("Current time: " + currUserZonedTime);
        System.out.println("Current time add 15 minutes: " + Timestamp.from(addFifteenMins));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        try {
            // Get all the appointments in the database
            ObservableList<Appointments> allAppointments = databaseAppointments.getAllApptsForComparison();

            // Check each appointment startTime and see if it is within 15 minutes of the current time
            ObservableList<Appointments> foundAppointments =
                    allAppointments.stream().filter(appt -> {
                        try {
                            return dateFormat.parse(appt.startTimeProperty().get()).toInstant().isBefore(addFifteenMins) &&
                                    dateFormat.parse(appt.startTimeProperty().get()).toInstant().isAfter(currTimeUTC.toInstant());
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return false;
                    } ).collect(Collectors.toCollection(FXCollections::observableArrayList));

            Alert upcomingApptAlert = new Alert(Alert.AlertType.INFORMATION);
            upcomingApptAlert.setTitle("Upcoming Appointments");
            Stage stage = (Stage) upcomingApptAlert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            // If any appointments are found, display the appointment information
            if (foundAppointments.size() > 0)
            {
                ObservableList<String> apptsList = FXCollections.observableArrayList();
                Consumer<Appointments> consumer = appt -> apptsList.add(String.valueOf(new StringBuilder().append
                        ("Appointment ID: ").append(appt.getApptId()).append
                        (" | On: ").append(appt.startTimeProperty().get().substring(0, appt.startTimeProperty().get().indexOf(" ") )).append
                        (" at: ").append(appt.startTimeProperty().get().substring(appt.startTimeProperty().get().indexOf(" ") + 1 ))));
                foundAppointments.stream().forEach(consumer);
                String apptInfo = "";
                for (String appt : apptsList) {
                    apptInfo = apptInfo.concat(appt + System.lineSeparator());
                }
                upcomingApptAlert.setContentText(apptInfo);
                upcomingApptAlert.show();
            }
            else
            {
                upcomingApptAlert.setContentText("There are no upcoming appointments.");
                upcomingApptAlert.show();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Handles click of 'View Reports' 'Button'
     * @throws IOException if the FXML file not found.
     */
    public void viewReportsButtonListener() throws IOException
    {
        // Load the View Reports FXML
        Parent reportsFXML = FXMLLoader.load(getClass().getResource("../view/reports.fxml"));
        // Create the new stage and scene
        Scene viewReportsScene = new Scene(reportsFXML, 800, 360);
        Stage viewReportsStage = new Stage();
        viewReportsStage.setTitle("View Reports");
        viewReportsStage.setScene(viewReportsScene);
        viewReportsStage.show();

        //Close out the ViewCustomerRecords stage
        Stage customerStage = (Stage)viewReportsButton.getScene().getWindow();
        customerStage.close();
    }

}