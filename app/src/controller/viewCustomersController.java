package controller;

import model.Customer;
import model.Appointment;
import main.Main;
import database.DatabaseAppointments;
import database.DatabaseCustomerRecords;
import database.DatabaseQuery;
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

public class ViewCustomersController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            displayCustomerRecords();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        checkForUpcomingAppointments();
    }

    public void displayCustomerRecords() throws SQLException {
        customerRecords = DatabaseCustomerRecords.getAllCustomerRecords();
        Main.currId = customerRecords.size();

        TableColumn<Customer,Integer> columnCustomerID = new TableColumn<>("ID");
        TableColumn<Customer,String> columnCustomerName = new TableColumn<>("Name");
        TableColumn<Customer,String> columnAddress = new TableColumn<>("Address");
        TableColumn<Customer,String> columnPostalCode = new TableColumn<>("Postal Code");
        TableColumn<Customer,String> columnDivision = new TableColumn<>("Division");
        TableColumn<Customer,String> columnCountry = new TableColumn<>("Country");
        TableColumn<Customer,String> columnPhone = new TableColumn<>("Phone");

        columnCustomerID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
        columnCustomerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        columnPostalCode.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        columnDivision.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        columnCountry.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        customerRecordsTableView.setItems(customerRecords);
        customerRecordsTableView.getColumns().addAll(columnCustomerID, columnCustomerName, columnAddress,
                columnPostalCode, columnDivision, columnCountry, columnPhone);
    }

    public void exitButtonListener(ActionEvent actionEvent) {
        JDBC.closeConnection();
        Platform.exit();
    }

    public void deleteButtonListener(ActionEvent actionEvent) throws IOException, SQLException {
        Customer deleteChoice = customerRecordsTableView.getSelectionModel().getSelectedItem();
        int customerId = deleteChoice.getCustomerId();
        String customerName = deleteChoice.getCustomerName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer Confirmation");
        String deleteConfirmationText = "Are you sure you want to delete customer " + customerName + "?";
        alert.setContentText(deleteConfirmationText);

        Optional<ButtonType> reply = alert.showAndWait();

        if ((reply.isPresent()) && (reply.get() == ButtonType.OK)) {
            String sqlDeleteQuery = "DELETE FROM client_schedule.customers WHERE Customer_ID = ?";

            DatabaseQuery.setPreparedStatement(JDBC.getConnection(), sqlDeleteQuery);
            PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

            preparedStatement.setString(1, String.valueOf(customerId));
            preparedStatement.execute();

            customerRecords.remove(deleteChoice);

            deleteConfirmationLabel.setText("Customer " + deleteChoice.getCustomerName() + " has been deleted!");
        }

        if ((reply.isPresent()) && (reply.get() == ButtonType.NO)) {
        }
    }

    public void addCustomerButtonListener(ActionEvent actionEvent) throws IOException {
        Parent addCustomerFXML = null;
        try {
            addCustomerFXML = FXMLLoader.load(getClass().getResource("../view/addCustomer.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

    public void updateButtonListener(ActionEvent actionEvent) {
        chosenCustomer = customerRecordsTableView.getSelectionModel().getSelectedItem();

        Parent updateCustomerFXML = null;
        try {
            updateCustomerFXML = FXMLLoader.load(getClass().getResource("../view/updateCustomer.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Scene updateCustomerScene = new Scene(updateCustomerFXML, 360, 600);
        Stage updateCustomerStage = new Stage();
        updateCustomerStage.setTitle("Update Customer");
        updateCustomerStage.setScene(updateCustomerScene);
        updateCustomerStage.show();

    }

    public void viewApptsButtonListener(ActionEvent actionEvent) throws IOException {
        Parent viewAppointmentsFXML = FXMLLoader.load(getClass().getResource("../view/viewAppointments.fxml"));
        Scene viewAppointmentsScene = new Scene(viewAppointmentsFXML, 800, 360);
        Stage viewAppointmentsStage = new Stage();
        viewAppointmentsStage.setTitle("View Appointments");
        viewAppointmentsStage.setScene(viewAppointmentsScene);
        viewAppointmentsStage.show();

        Stage customerStage = (Stage)viewApptsButton.getScene().getWindow();
        customerStage.close();

    }

    public void checkForUpcomingAppointments()
    {
        ZonedDateTime currUserZonedTime = ZonedDateTime.now(ZoneId.of(TimeZone.getDefault().getID()));
        Timestamp currTimeUTC = Timestamp.from(currUserZonedTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant());

        Instant addFifteenMins = currTimeUTC.toInstant().plus(Duration.ofMinutes(15));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        try {
            ObservableList<Appointment> allAppointments = DatabaseAppointments.getAllApptsForComparison();

            ObservableList<Appointment> foundAppointments =
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

            if (foundAppointments.size() > 0)
            {
                ObservableList<String> apptsList = FXCollections.observableArrayList();
                Consumer<Appointment> consumer = appt -> apptsList.add(String.valueOf(new StringBuilder().append
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

    public void viewReportsButtonListener() throws IOException
    {
        Parent reportsFXML = FXMLLoader.load(getClass().getResource("../view/reports.fxml"));
        Scene viewReportsScene = new Scene(reportsFXML, 800, 360);
        Stage viewReportsStage = new Stage();
        viewReportsStage.setTitle("View Reports");
        viewReportsStage.setScene(viewReportsScene);
        viewReportsStage.show();

        Stage customerStage = (Stage)viewReportsButton.getScene().getWindow();
        customerStage.close();
    }

}