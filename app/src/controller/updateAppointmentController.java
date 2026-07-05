package controller;

import database.databaseAppointments;
import database.databaseQuery;
import database.JDBC;
import model.Appointments;
import main.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static controller.viewAppointmentsController.clientAppointments;
import static database.databaseAppointments.getAllAppts;

public class updateAppointmentController implements Initializable {
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
    private Button updateApptButton;

    /** Initializes the Update Appointment Form
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // populate Dropdown menus
        fillContactDropdown();
        fillCustomerIdDropdown();
        fillUserIdDropdown();
        fillApptTypeDropdown();

        if (Main.selectedAppointments != null) {
            // Retrieve the selected Appointment info from the database and populate fields
            retrievePopulateAppt();
        }
    }

    /** Populates the 'contactDropdown' with data from the database */
    public void fillContactDropdown() {
        // Get all contacts from the database
        ObservableList<String> contactsList = databaseAppointments.getContacts();
        // Populate the contactComboBox
        contactDropdown.setItems(contactsList);
    }

    /** Populates the 'CustomerIdDropdown' with data from the database */
    public void fillCustomerIdDropdown() {
        ObservableList<String> customersArrayList = databaseAppointments.getCustomers();
        customerIdDropdown.setItems(customersArrayList);
    }

    /** Populates the 'apptTypeDropdown' with the various meeting types */
    public void fillApptTypeDropdown() {
        ObservableList<String> appointmentTypesList = FXCollections.observableArrayList(
                "Initial", "Planning", "De-Briefing", "Virtual", "Closing");
        apptTypeDropdown.setItems(appointmentTypesList);
    }

    /** Populates the 'userIdDropdown' with the 'User'(s) from the database */
    public void fillUserIdDropdown() {
        ObservableList<String> userIdList = databaseAppointments.getUsers();
        userIdDropdown.setItems(userIdList);
    }

    /** Retrieve the Appointment information from the database and populate the form fields */
    public void retrievePopulateAppt() {

        Appointments appointmentsToBeUpdated = Main.selectedAppointments;

        // Fill in TextFields with data from database
        apptIdTextField.setText(String.valueOf(appointmentsToBeUpdated.apptIdProperty().getValue()));
        apptTitleTextField.setText(String.valueOf(appointmentsToBeUpdated.titleProperty().getValue()));
        apptDescriptionTextField.setText(String.valueOf(appointmentsToBeUpdated.descriptionProperty().getValue()));
        apptLocationTextField.setText(String.valueOf(appointmentsToBeUpdated.locationProperty().getValue()));

        // Get the customerID of the selected Appointment
        int selectedApptCustomerId = appointmentsToBeUpdated.customerIdProperty().getValue();
        System.out.println(selectedApptCustomerId);

        // Loop through the customerIdDropdown options
        for (String s : customerIdDropdown.getItems()) {
            System.out.println(customerIdDropdown.getItems());
            System.out.println("Customer: " + s);
            // Extract only the number from the Dropdown option
            int customerInt = Integer.parseInt(s.substring( 0, s.indexOf(" ", 0)));
            // Check if the selected value matches the current value being checked
            if (selectedApptCustomerId == customerInt) {
                // If so, set the ComboBox value to the current value
                customerIdDropdown.setValue(s);
                break;
            }
        }

        // Retrieve the userID of the selected Appointment
        int selectedApptUserId = appointmentsToBeUpdated.userIdProperty().getValue();
        System.out.println(selectedApptUserId);
        // Loop through the userIdComboBox options
        for (String user : userIdDropdown.getItems()) {
            // Extract only the number from the ComboBox option
            int userInt = Integer.parseInt(user.substring(0, user.indexOf( " ", 0)));
            // Check if the selected value matches the current value being checked
            if (selectedApptUserId == userInt) {
                // If so, set the ComboBox value to the current value
                userIdDropdown.setValue(user);
                break;
            }
        }

        // Get the appointmentType of the selected Appointment
        String selectedApptType = appointmentsToBeUpdated.typeProperty().getValue();
        System.out.println("Selected Appointment Type: " + selectedApptType);
        // Loop through the appointmentTypeComboBox options
        for (String type : apptTypeDropdown.getItems()) {
            // If the selected type matches an option in the ComboBox
            if (selectedApptType.equals(type)) {
                // Set the value of the ComboBox to that value
                apptTypeDropdown.setValue(type);
                break;
            }
        }

        // Get the contactID of the selected Appointment
        int chosenApptContactId = appointmentsToBeUpdated.contactIdProperty().getValue();
        System.out.println("Chosen appointment contact id: " + chosenApptContactId);
        System.out.println("Contact: " + chosenApptContactId);
        // Loop through the contactComboBox options
        for (String contact : contactDropdown.getItems()) {
            // Extract only the number from the ComboBoxOption
            int contactInt = Integer.parseInt(contact.substring( 0, contact.indexOf( " ", 0)));
            // If the selected contactId matches an option in the ComboBox
            if (chosenApptContactId == contactInt) {
                // Set the ComboBox value to that option
                contactDropdown.setValue(contact);
                break;
            }
        }

        // Get the Start Timestamp from the database
        String chosenApptStartTimestamp = appointmentsToBeUpdated.startTimeProperty().getValue();
        System.out.println("Timestamp string: " + chosenApptStartTimestamp);
        // Separate the date from the time
        String startDate = chosenApptStartTimestamp.substring(0, chosenApptStartTimestamp.indexOf(" ", 0));
        int indexOfTime = chosenApptStartTimestamp.indexOf(" ");
        int startTimestampLength = chosenApptStartTimestamp.length();
        String startTime = chosenApptStartTimestamp.substring(indexOfTime + 1);

        System.out.println("Start date: " + startDate);
        System.out.println("Start time: " + startTime);
        // Set the Start Date and time from the database into the form fields
        startDateSelector.setValue(LocalDate.parse(startDate));
        startTimeTextField.setText(startTime);


        // Get the End Timestamp from the database
        String selectedApptEndTimestamp = appointmentsToBeUpdated.endTimeProperty().getValue();
        System.out.println("Timestamp string: " + selectedApptEndTimestamp);

        // Separate the date from the time
        String endDate = selectedApptEndTimestamp.substring(0, selectedApptEndTimestamp.indexOf(" ", 0));
        int indexOfTEnd = chosenApptStartTimestamp.indexOf(" ");
        String endTime = selectedApptEndTimestamp.substring(indexOfTEnd + 1);

        System.out.println("End date: " + endDate);
        System.out.println("End time: " + endTime);
        // Set the End Date and time from the database into the form fields
        endDateSelector.setValue(LocalDate.parse(endDate));
        endTimeTextField.setText(endTime);

    }

    /** Uses the information in the form fields to update the selected 'Appointment'
     * @param actionEvent Click of the 'Update' 'Button'
     */
    public void updateApptButtonListener(ActionEvent actionEvent) throws ParseException {
        System.out.println("Update appt button clicked.");

        // Check if the appointment time is within office hours or overlaps with a previously scheduled appointment
        boolean timeVerify = verifyIfWithinHours();
        boolean overlapVerify = customerApptOverlap();

        if (overlapVerify){
            // Create and show an error alert
            Alert apptOverlapError = new Alert(Alert.AlertType.ERROR, "New appointment overlaps with a previously scheduled one.");
            apptOverlapError.setTitle("Appointment Overlap Error");
            apptOverlapError.show();
        }
        else {
            if (timeVerify) {

                try {
                    // Get the connection
                    Connection connection = JDBC.getConnection();
                    // Update SQL Statement
                    String updateStatement =
                            "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?," +
                            "Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

                    databaseQuery.setPreparedStatement(connection, updateStatement);

                    PreparedStatement preparedStatement = databaseQuery.getPreparedStatement();

                    // Retrieve the values from the startDatePicker and startTimeTextField and combine them into a Timestamp
                    LocalDate startDate = startDateSelector.getValue();
                    LocalTime startTime = LocalTime.parse(startTimeTextField.getText());
                    // Create a LocalDateTime from the values
                    LocalDateTime localDateTime = startDate.atTime(startTime);
                    // Create a ZonedDateTime from the LocalDateTime
                    ZonedDateTime startTimeInUTC = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                    // Convert the ZonedDateTime into a Timestamp so that it can be used in the database
                    Timestamp startTimestamp = Timestamp.valueOf(startTimeInUTC.toLocalDateTime());

                    // Retrieve the values from the endDatePicker && endTimeTextField and combine them into a Timestamp
                    LocalDate endDate = endDateSelector.getValue();
                    LocalTime endTime = LocalTime.parse(endTimeTextField.getText());
                    // Create a LocalDateTime from the values
                    LocalDateTime endLocalDateTime = endDate.atTime(endTime);
                    // Create a ZonedDateTime from the LocalDateTime
                    ZonedDateTime endTimeInUTC = ZonedDateTime.of(endLocalDateTime, ZoneId.of("UTC"));
                    // Convert the ZonedDateTime into a Timestamp so that it can be stored in the database
                    Timestamp endTimestamp = Timestamp.valueOf(endTimeInUTC.toLocalDateTime());

                    // Retrieve the value from the customerIdComboBox and extract only the integer
                    String customerIdString = customerIdDropdown.getValue();
                    int customerIdInt = Integer.parseInt(customerIdString.substring(0, customerIdString.indexOf(" ", 0)));

                    // Retrieve the value from the userIdComboBox and extract only the integer
                    String userIdString = userIdDropdown.getValue();
                    int userIdInt = Integer.parseInt(userIdString.substring(0, userIdString.indexOf(" ", 0)));

                    // Retrieve the value from the contactIdComboBox and extract only the integer
                    String contactIdString = contactDropdown.getValue();
                    int contactIdInt = Integer.parseInt(contactIdString.substring(0, contactIdString.indexOf( " ", 0)));

                    // Set the values into the SQL statement
                    preparedStatement.setString(1, apptTitleTextField.getText());
                    preparedStatement.setString(2, apptDescriptionTextField.getText());
                    preparedStatement.setString(3, apptLocationTextField.getText());
                    preparedStatement.setString(4, apptTypeDropdown.getValue());
                    preparedStatement.setString(5, String.valueOf(startTimestamp));
                    preparedStatement.setString(6, String.valueOf(endTimestamp));
                    preparedStatement.setString(7, String.valueOf(customerIdInt));
                    preparedStatement.setString(8, String.valueOf(userIdInt));
                    preparedStatement.setString(9, String.valueOf(contactIdInt));
                    preparedStatement.setString(10, apptIdTextField.getText());
                    preparedStatement.execute();

                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

                // Create a new ObservableList containing the updated Appointment
                ObservableList<Appointments> newAppointmentsList = FXCollections.observableArrayList(getAllAppts());

                // Update the ObservableList to update the table
                clientAppointments.setAll(newAppointmentsList);

                // Close the window
                Stage stage = (Stage) updateApptButton.getScene().getWindow();
                stage.close();
            }
            else {
                // Create Alert
                Alert scheduleTimeError = new Alert(Alert.AlertType.ERROR);
                // Set title
                scheduleTimeError.setTitle("Appointment Time Error");
                // Create error message
                String timeError = "Appointment is not within office hours of 8AM - 10PM EST. (08:00 and 22:00)";
                // Set alert content
                scheduleTimeError.setContentText(timeError);

                scheduleTimeError.show();
            }
        }
    }

    /** Cancels the Update Appointment action and closes the window
     * @param actionEvent Click of the 'Cancel' 'Button'
     */
    public void cancelButtonListener(ActionEvent actionEvent) {
        System.out.println("Cancel add appointment!");

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /** Checks to see if the times in the form are between office hours
     * @return Whether the times are within office hours
     */
    public boolean verifyIfWithinHours()
    {
        boolean withinBusinessHrs;

        // Convert the String in the TextField to a LocalTime
        LocalTime localStartTime = LocalTime.parse(startTimeTextField.getText());
        LocalTime localEndTime = LocalTime.parse(endTimeTextField.getText());

        // Get the date from the DatePicker
        LocalDate localStartDate = startDateSelector.getValue();
        LocalDate localEndDate = endDateSelector.getValue();

        // Convert the LocalDate and LocalTime to a ZonedDateTime of the user's local timezone
        ZonedDateTime localZonedApptStartTime =
                ZonedDateTime.of(localStartDate, localStartTime, ZoneId.of( TimeZone.getDefault().getID()));
        ZonedDateTime easternZonedApptStartTime =
                localZonedApptStartTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        System.out.println("User time zone: " + TimeZone.getDefault().getID());
        System.out.println("User timezone start time: " + localZonedApptStartTime);
        System.out.println("Eastern timezone converted start time: " + easternZonedApptStartTime);

        ZonedDateTime localZonedApptEndTime =
                ZonedDateTime.of(localEndDate, localEndTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime easternZonedApptEndTime =
                localZonedApptEndTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        System.out.println("User Timezone End Time: " + localZonedApptEndTime);
        System.out.println("Eastern Timezone Converted End Time: " + easternZonedApptEndTime);

        // Create the office hour times
        LocalTime businessOpenLocalTime = LocalTime.of(8,0,0);
        ZonedDateTime businessOpenTime = ZonedDateTime.of(localStartDate, businessOpenLocalTime, ZoneId.of("America/New_York"));
        LocalTime businessCloseLocalTime = LocalTime.of(22, 0, 0);
        ZonedDateTime businessCloseTime = ZonedDateTime.of(localStartDate, businessCloseLocalTime, ZoneId.of("America/New_York"));

        // Check if easternZonedApptStartTime is between 8am and 10pm EST (8:00 and 22:00)
        withinBusinessHrs =
                (easternZonedApptStartTime.isAfter(businessOpenTime) ||
                        easternZonedApptStartTime.equals(businessOpenTime)) &&
                        ((easternZonedApptEndTime.isBefore(businessCloseTime)) ||
                                easternZonedApptEndTime.equals(businessCloseTime));

        return withinBusinessHrs;
    }

    /** Checks to see if the times on the form overlap with a customer's previously scheduled appointments
     * LAMBDA used - purpose of Stream API
     * @return boolean value - whether there are any overlapping appointments
     */
    public boolean customerApptOverlap() {
        boolean apptOverlap = false;

        // Get customer ID from comboBox
        String customerIdString = customerIdDropdown.getValue();
        int customerIdInt = Integer.parseInt(customerIdString.substring(0, customerIdString.indexOf(" ", 0)));

        // Get the time and date from the form
        LocalDate localStartDate = startDateSelector.getValue();
        LocalTime localStartTime = LocalTime.parse(startTimeTextField.getText());
        LocalDate localEndDate = endDateSelector.getValue();
        LocalTime localEndTime = LocalTime.parse(endTimeTextField.getText());

        // Convert to UTC timestamps for comparison to values in the database
        ZonedDateTime userTimeZoneStartTime = ZonedDateTime.of(localStartDate, localStartTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime utcStartTime = userTimeZoneStartTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp utcStartTimestamp = Timestamp.from(utcStartTime.toInstant());

        ZonedDateTime userTimeZoneEndTime = ZonedDateTime.of(localEndDate, localEndTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime utcEndTime = userTimeZoneEndTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp utcEndTimestamp = Timestamp.from(utcEndTime.toInstant());

        // Retrieve all appointments for customer
        ObservableList<Appointments> customerAppointments = databaseAppointments.getAllApptsForCustomer(customerIdInt);

        // Go through the list of appts and compare the date and time ranges to the one in the form
        // Checks to see if the utcStartTime and utcEndTime is before the localEndTime and after the localStartTime of another
        // or if the utcStartTime = starTime of another Appointment
        ObservableList<Appointments> overlappingAppointments =
                customerAppointments.stream().filter(a -> (utcStartTimestamp.before((Timestamp.valueOf((a.endTimeProperty().get())))) &&
                        utcStartTimestamp.after((Timestamp.valueOf((a.startTimeProperty().get())))) ||
                        utcStartTimestamp.equals((Timestamp.valueOf((a.startTimeProperty().get())))))||
                        utcEndTimestamp.after((Timestamp.valueOf((a.startTimeProperty().get())))) &&
                                utcEndTimestamp.before((Timestamp.valueOf((a.endTimeProperty().get()))))).filter(a -> a.getApptId() != Integer.parseInt(apptIdTextField.getText())).collect(Collectors.toCollection(FXCollections::observableArrayList));


        if (overlappingAppointments.size() > 0) {
            apptOverlap = true;
        }
        else if (overlappingAppointments.size() == 0 ) {
            apptOverlap = false;
        }

        return apptOverlap;
    }
}