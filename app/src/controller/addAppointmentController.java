package controller;

import database.databaseAppointments;
import database.databaseQuery;
import database.JDBC;
import model.Appointments;
import static controller.viewAppointmentsController.clientAppointments;
import static main.Main.randNumber;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class addAppointmentController implements Initializable {
    @FXML
    private TextField apptIdTextField;
    @FXML
    private ComboBox<String> customerIdDropdown;
    @FXML
    private ComboBox<String> userIdDropdown;
    @FXML
    private TextField apptTitleTextField;
    @FXML
    private TextField apptDescriptionTextField;
    @FXML
    private TextField apptLocationTextField;
    @FXML
    private ComboBox<String> apptTypeDropdown;
    @FXML
    private ComboBox<String> contactDropdown;
    @FXML
    private DatePicker startDateSelector;
    @FXML
    private DatePicker endDateSelector;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addApptButton;

    /** Initialize Add Appointment Form
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        randAppointmentId();
        fillContactDropdown();
        fillCustomerIdDropdown();
        fillAppointmentTypeDropdown();
        fillUserIdDropdown();
    }

    /** Auto generates new 'appointmentId' */
    public void randAppointmentId() {
        Random randomNumber = new Random();
        randNumber = randomNumber.nextInt(999);
        apptIdTextField.setText(String.valueOf(randNumber));
    }

    /** Fills the 'contactComboBox' with data from the database */
    public void fillContactDropdown() {
        // Get all contacts from database
        ObservableList<String> contactsList = databaseAppointments.getContacts();
        // Populate contactComboBox
        contactDropdown.setItems(contactsList);
    }

    /** Fills the 'customerIdComboBox' with data from the database */
    public void fillCustomerIdDropdown() {
        ObservableList<String> customersList = databaseAppointments.getCustomers();
        customerIdDropdown.setItems(customersList);
    }

    /** Fills the 'appointmentTypeComboBox' with the various meeting types */
    public void fillAppointmentTypeDropdown() {
        ObservableList<String> apptsTypesList =
                FXCollections.observableArrayList("Initial", "Planning", "De-Briefing", "Virtual", "Closing");
        apptTypeDropdown.setItems(apptsTypesList);
    }

    /** Populates the 'userIdComboBox' with the 'User's from the database */
    public void fillUserIdDropdown() {
        ObservableList<String> userIdList = databaseAppointments.getUsers();
        userIdDropdown.setItems(userIdList);
    }


    /** Adds the new 'Appointment' to the 'ObservableList'
     * @param actionEvent Click of 'Button'
     */
    public void addApptButtonListener(ActionEvent actionEvent) {
        System.out.println("'Add button' clicked!");

        // Display error alert if appointment overlaps another
        if (apptsOverlap()) {
            Alert apptOverlapError = new Alert(Alert.AlertType.ERROR,"New appointment overlaps with a previously scheduled one.");
            apptOverlapError.setTitle("Appointment Overlap Error");
            apptOverlapError.show();
        }
        //If appointment is within hours and does not overlap another
        else {
            if (apptWithinHrs()) {
                // Get value from customerIdComboBox
                String customerIdString = customerIdDropdown.getValue();
                int customerIdInt = Integer.parseInt(customerIdString.substring(0, customerIdString.indexOf(" ", 0)));
                IntegerProperty customerIdIntProperty = new SimpleIntegerProperty(customerIdInt);

                // Get value from userIdComboBox
                String userIdString = userIdDropdown.getValue();
                int userIdInt = Integer.parseInt(userIdString.substring(0, userIdString.indexOf(" ", 0)));
                IntegerProperty userIdIntProperty = new SimpleIntegerProperty(userIdInt);

                StringProperty titleProperty = new SimpleStringProperty(apptTitleTextField.getText());
                StringProperty descriptionProperty = new SimpleStringProperty(apptDescriptionTextField.getText());
                StringProperty locationProperty = new SimpleStringProperty(apptLocationTextField.getText());
                StringProperty apptTypeProperty = new SimpleStringProperty(apptTypeDropdown.getValue());

                // Get value from contactComboBox
                String contactIdString = contactDropdown.getValue();
                int contactIdInt = Integer.parseInt(contactIdString.substring(0,contactIdString.indexOf(" ", 0)));
                IntegerProperty contactIdProperty = new SimpleIntegerProperty(contactIdInt);

                // Get LocalDate from startDatePicker and LocalTime from startTimeTextField
                LocalDate startDate = startDateSelector.getValue();
                LocalTime startTime = LocalTime.parse(startTimeTextField.getText());
                // Merge both LocalDate and LocalTime values into LocalDateTime
                LocalDateTime startLocalDateTime = startDate.atTime(startTime);
                // Create ZonedDateTime from the LocalDateTime
                ZonedDateTime startTimeUTC = ZonedDateTime.of(startLocalDateTime, ZoneId.of("UTC"));
                // Convert the ZonedDateTime into a Timestamp
                Timestamp startTimestamp = Timestamp.valueOf(startTimeUTC.toLocalDateTime());
                StringProperty startTimeProperty = new SimpleStringProperty(startTimestamp.toString());

                // Get LocalDate from endDateSelector and LocalTime from endTimeTextField
                LocalDate endDate = endDateSelector.getValue();
                LocalTime endTime = LocalTime.parse(endTimeTextField.getText());
                // Merge both LocalDate and LocalTime values into LocalDateTime
                LocalDateTime endLocalDateTime = endDate.atTime(endTime);
                // Create ZonedDateTime from the LocalDateTime
                ZonedDateTime endTimeUTC = ZonedDateTime.of(endLocalDateTime, ZoneId.of("UTC"));
                // Convert ZonedDateTime into a Timestamp
                Timestamp endTimestamp = Timestamp.valueOf(endTimeUTC.toLocalDateTime());
                StringProperty endTimeProperty = new SimpleStringProperty(endTimestamp.toString());

                // Create IntegerProperty from the currentAppointmentId
                IntegerProperty currApptIdProperty = new SimpleIntegerProperty(randNumber);

                // Prepare the SQL statement and connection
                Connection connection = JDBC.getConnection();
                String sqlInsertQuery = "INSERT INTO client_schedule.appointments(Title, Description, Location, " +
                        "Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try {
                    databaseQuery.setPreparedStatement(connection, sqlInsertQuery);
                    PreparedStatement preparedStatement = databaseQuery.getPreparedStatement();

                    // Add the new appointment into database
                    preparedStatement.setString(1, titleProperty.getValue());
                    preparedStatement.setString(2, descriptionProperty.getValue());
                    preparedStatement.setString(3, locationProperty.getValue());
                    preparedStatement.setString(4, apptTypeProperty.getValue());
                    preparedStatement.setTimestamp(5, Timestamp.valueOf(startTimeProperty.getValue()));
                    preparedStatement.setTimestamp(6, Timestamp.valueOf(endTimeProperty.getValue()));
                    preparedStatement.setString(7, String.valueOf(customerIdInt));
                    preparedStatement.setString(8, String.valueOf(userIdInt));
                    preparedStatement.setString(9, String.valueOf(contactIdInt));
                    preparedStatement.execute();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

                // Create new Appointment Object and add it to ObservableList
                clientAppointments.add(new Appointments(currApptIdProperty, titleProperty, descriptionProperty,
                        locationProperty, apptTypeProperty, startTimeProperty, endTimeProperty, userIdIntProperty,
                        customerIdIntProperty, contactIdProperty));

                Stage stage = (Stage)addApptButton.getScene().getWindow();
                stage.close();
            }
            else {
                // Get user's timezone
                TimeZone userTimeZone = TimeZone.getDefault();

                // Create 08:00 EST && 22:00 EST
                LocalTime businessOpenLocalTime = LocalTime.of(8,0,0);
                ZonedDateTime businessOpen = ZonedDateTime.of(LocalDate.now(),businessOpenLocalTime, ZoneId.of("America/New_York"));
                LocalTime businessCloseLocalTime = LocalTime.of(22, 0, 0);
                ZonedDateTime businessClose = ZonedDateTime.of(LocalDate.now(), businessCloseLocalTime, ZoneId.of("America/New_York"));

                // Convert the times to the user's timezone
                ZonedDateTime businessOpenInUserTimeZone = businessOpen.withZoneSameInstant(ZoneId.of(userTimeZone.getID()));
                ZonedDateTime businessCloseInUserTimeZone = businessClose.withZoneSameInstant(ZoneId.of(userTimeZone.getID()));

                System.out.println("Business open (User TimeZone): " + businessOpenInUserTimeZone);
                System.out.println("Business close (User TimeZone): " + businessCloseInUserTimeZone);

                // Create a new Alert
                Alert outsideBusinessHrsError = new Alert(Alert.AlertType.ERROR);
                outsideBusinessHrsError.setTitle("Appointment Scheduling Error");
                String schedulingErrorMessage =
                        "Appointment is not within office hours of 8AM - 10PM EST. ( " + businessOpenInUserTimeZone.getHour() +
                                ":00 and " + businessCloseInUserTimeZone.getHour() + ":00 in your Timezone.)";
                outsideBusinessHrsError.setContentText(schedulingErrorMessage);
                outsideBusinessHrsError.show();
            }
        }
    }

    /** Checks if the times in the form are between business hours
     * @return boolean value - whether the times are within business hours
     */
    public boolean apptWithinHrs() {
        boolean withinBusinessHrs;

        // Convert String in the TextField to a LocalTime
        LocalTime startTime = LocalTime.parse(startTimeTextField.getText());
        LocalTime endTime = LocalTime.parse(endTimeTextField.getText());

        // Get date from the DatePicker
        LocalDate startDate = startDateSelector.getValue();
        LocalDate endDate = endDateSelector.getValue();

        // Convert LocalDate and LocalTime to a ZonedDateTime of the user's local timezone
        ZonedDateTime localApptStartTime = ZonedDateTime.of(startDate, startTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime easternApptStartTime = localApptStartTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        System.out.println("Start Time (User Timezone): " + localApptStartTime);
        System.out.println("Eastern Timezone converted Start Time: " + easternApptStartTime);

        ZonedDateTime localZonedAppointmentEndTime = ZonedDateTime.of(endDate, endTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime easternZonedApptEndTime = localZonedAppointmentEndTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        System.out.println("User Timezone End Time: " + localZonedAppointmentEndTime);
        System.out.println("Eastern Timezone Converted End Time: " + easternZonedApptEndTime);

        // Create the office hour times
        LocalTime businessOpenLocalTime = LocalTime.of(8, 0, 0);
        ZonedDateTime businessOpenTime = ZonedDateTime.of( startDate, businessOpenLocalTime, ZoneId.of("America/New_York") );
        LocalTime businessCloseLocalTime = LocalTime.of( 22, 0, 0);
        ZonedDateTime businessCloseTime = ZonedDateTime.of(startDate, businessCloseLocalTime, ZoneId.of("America/New_York"));

        // Check if easternApptStartTime is between 08:00 and 22:00 EST
        withinBusinessHrs =
                easternApptStartTime.isAfter(businessOpenTime) ||
                        easternApptStartTime.equals(businessOpenTime) &&
                                easternZonedApptEndTime.isBefore(businessCloseTime) ||
                                        easternZonedApptEndTime.equals(businessCloseTime);
        return withinBusinessHrs;
    }

    /** Checks to see if the times on the form overlap with a customer's previously scheduled appointments
     * @return Whether there are any overlapping appointments
     */
    public boolean apptsOverlap() {
        boolean apptOverlap = false;

        // Get customer ID from comboBox
        String customerIdString = customerIdDropdown.getValue();
        int customerIdInt = Integer.parseInt(customerIdString.substring(0, customerIdString.indexOf(" ", 0)));

        // Get time and date from the form
        LocalDate localStartDate = startDateSelector.getValue();
        LocalTime localStartTime = LocalTime.parse(startTimeTextField.getText());
        LocalDate localEndDate = endDateSelector.getValue();
        LocalTime localEndTime = LocalTime.parse(endTimeTextField.getText());

        // Convert UTC timestamps to compare with times from the database
        ZonedDateTime userTimeZoneStartTime = ZonedDateTime.of(localStartDate, localStartTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime utcStartTime = userTimeZoneStartTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp utcStartTimestamp = Timestamp.from(utcStartTime.toInstant());

        ZonedDateTime userTimeZoneEndTime = ZonedDateTime.of(localEndDate, localEndTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime utcEndTime = userTimeZoneEndTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp utcEndTimestamp = Timestamp.from(utcEndTime.toInstant());

        // Retrieve all appointments for customer
        ObservableList<Appointments> customerAppointments = databaseAppointments.getAllApptsForCustomer(customerIdInt);

        // Go through the list of appointments and compare the date and time ranges to the one in the form
        // Checks if utcStartTime and utcEndTime come before the localEndTime and after the localStartTime of another appt
        // or if utcStartTime = starTime of another Appointment
        ObservableList<Appointments> overlappingAppointments =
                customerAppointments.stream().filter(appt -> (utcStartTimestamp.before((Timestamp.valueOf((appt.endTimeProperty().get())))) &&
                        utcStartTimestamp.after((Timestamp.valueOf((appt.startTimeProperty().get())))) ||
                        utcStartTimestamp.equals((Timestamp.valueOf((appt.startTimeProperty().get())))))||
                        utcEndTimestamp.after((Timestamp.valueOf((appt.startTimeProperty().get())))) &&
                        utcEndTimestamp.before((Timestamp.valueOf((appt.endTimeProperty().get()))))).collect(Collectors.toCollection(FXCollections::observableArrayList));

        if (overlappingAppointments.size() > 0 ) {
            apptOverlap = true;
        }
        else if (overlappingAppointments.size() == 0 ) {
            apptOverlap = false;
        }

        return apptOverlap;
    }

    /** Cancels the operation and closes the window
     * @param actionEvent Click of the 'cancelButton'
     */
    public void cancelButtonListener(ActionEvent actionEvent) {
        System.out.println("Cancel button clicked.");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}