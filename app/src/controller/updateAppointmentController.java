package controller;

import database.DatabaseAppointments;
import database.DatabaseQuery;
import database.JDBC;
import model.Appointment;
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

import static controller.ViewAppointmentsController.clientAppointments;
import static database.DatabaseAppointments.getAllAppts;

public class UpdateAppointmentController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillContactDropdown();
        fillCustomerIdDropdown();
        fillUserIdDropdown();
        fillApptTypeDropdown();

        if (Main.selectedAppointments != null) {
            retrievePopulateAppt();
        }
    }

    public void fillContactDropdown() {
        ObservableList<String> contactsList = DatabaseAppointments.getContacts();
        contactDropdown.setItems(contactsList);
    }

    public void fillCustomerIdDropdown() {
        ObservableList<String> customersArrayList = DatabaseAppointments.getCustomers();
        customerIdDropdown.setItems(customersArrayList);
    }

    public void fillApptTypeDropdown() {
        ObservableList<String> appointmentTypesList = FXCollections.observableArrayList(
                "Initial", "Planning", "De-Briefing", "Virtual", "Closing");
        apptTypeDropdown.setItems(appointmentTypesList);
    }

    public void fillUserIdDropdown() {
        ObservableList<String> userIdList = DatabaseAppointments.getUsers();
        userIdDropdown.setItems(userIdList);
    }

    public void retrievePopulateAppt() {
        Appointment appointmentsToBeUpdated = Main.selectedAppointments;

        apptIdTextField.setText(String.valueOf(appointmentsToBeUpdated.apptIdProperty().getValue()));
        apptTitleTextField.setText(String.valueOf(appointmentsToBeUpdated.titleProperty().getValue()));
        apptDescriptionTextField.setText(String.valueOf(appointmentsToBeUpdated.descriptionProperty().getValue()));
        apptLocationTextField.setText(String.valueOf(appointmentsToBeUpdated.locationProperty().getValue()));

        int selectedApptCustomerId = appointmentsToBeUpdated.customerIdProperty().getValue();
        for (String s : customerIdDropdown.getItems()) {
            int customerInt = Integer.parseInt(s.substring( 0, s.indexOf(" ", 0)));
            if (selectedApptCustomerId == customerInt) {
                customerIdDropdown.setValue(s);
                break;
            }
        }

        int selectedApptUserId = appointmentsToBeUpdated.userIdProperty().getValue();
        for (String user : userIdDropdown.getItems()) {
            int userInt = Integer.parseInt(user.substring(0, user.indexOf( " ", 0)));
            if (selectedApptUserId == userInt) {
                userIdDropdown.setValue(user);
                break;
            }
        }

        String selectedApptType = appointmentsToBeUpdated.typeProperty().getValue();
        for (String type : apptTypeDropdown.getItems()) {
            if (selectedApptType.equals(type)) {
                apptTypeDropdown.setValue(type);
                break;
            }
        }

        int chosenApptContactId = appointmentsToBeUpdated.contactIdProperty().getValue();
        for (String contact : contactDropdown.getItems()) {
            int contactInt = Integer.parseInt(contact.substring( 0, contact.indexOf( " ", 0)));
            if (chosenApptContactId == contactInt) {
                contactDropdown.setValue(contact);
                break;
            }
        }

        String chosenApptStartTimestamp = appointmentsToBeUpdated.startTimeProperty().getValue();
        String startDate = chosenApptStartTimestamp.substring(0, chosenApptStartTimestamp.indexOf(" ", 0));
        int indexOfTime = chosenApptStartTimestamp.indexOf(" ");
        String startTime = chosenApptStartTimestamp.substring(indexOfTime + 1);
        startDateSelector.setValue(LocalDate.parse(startDate));
        startTimeTextField.setText(startTime);

        String selectedApptEndTimestamp = appointmentsToBeUpdated.endTimeProperty().getValue();
        String endDate = selectedApptEndTimestamp.substring(0, selectedApptEndTimestamp.indexOf(" ", 0));
        int indexOfTEnd = chosenApptStartTimestamp.indexOf(" ");
        String endTime = selectedApptEndTimestamp.substring(indexOfTEnd + 1);
        endDateSelector.setValue(LocalDate.parse(endDate));
        endTimeTextField.setText(endTime);
    }

    public void updateApptButtonListener(ActionEvent actionEvent) throws ParseException {
        boolean timeVerify = verifyIfWithinHours();
        boolean overlapVerify = customerApptOverlap();

        if (overlapVerify){
            Alert apptOverlapError = new Alert(Alert.AlertType.ERROR, "New appointment overlaps with a previously scheduled one.");
            apptOverlapError.setTitle("Appointment Overlap Error");
            apptOverlapError.show();
        }
        else {
            if (timeVerify) {
                try {
                    Connection connection = JDBC.getConnection();
                    String updateStatement =
                            "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?," +
                            "Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

                    DatabaseQuery.setPreparedStatement(connection, updateStatement);

                    PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

                    LocalDate startDate = startDateSelector.getValue();
                    LocalTime startTime = LocalTime.parse(startTimeTextField.getText());
                    LocalDateTime localDateTime = startDate.atTime(startTime);
                    ZonedDateTime startTimeInUTC = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                    Timestamp startTimestamp = Timestamp.valueOf(startTimeInUTC.toLocalDateTime());

                    LocalDate endDate = endDateSelector.getValue();
                    LocalTime endTime = LocalTime.parse(endTimeTextField.getText());
                    LocalDateTime endLocalDateTime = endDate.atTime(endTime);
                    ZonedDateTime endTimeInUTC = ZonedDateTime.of(endLocalDateTime, ZoneId.of("UTC"));
                    Timestamp endTimestamp = Timestamp.valueOf(endTimeInUTC.toLocalDateTime());

                    String customerIdString = customerIdDropdown.getValue();
                    int customerIdInt = Integer.parseInt(customerIdString.substring(0, customerIdString.indexOf(" ", 0)));

                    String userIdString = userIdDropdown.getValue();
                    int userIdInt = Integer.parseInt(userIdString.substring(0, userIdString.indexOf(" ", 0)));

                    String contactIdString = contactDropdown.getValue();
                    int contactIdInt = Integer.parseInt(contactIdString.substring(0, contactIdString.indexOf( " ", 0)));

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

                ObservableList<Appointment> newAppointmentsList = FXCollections.observableArrayList(getAllAppts());
                clientAppointments.setAll(newAppointmentsList);
                Stage stage = (Stage) updateApptButton.getScene().getWindow();
                stage.close();
            }
            else {
                Alert scheduleTimeError = new Alert(Alert.AlertType.ERROR);
                scheduleTimeError.setTitle("Appointment Time Error");
                String timeError = "Appointment is not within office hours of 8AM - 10PM EST. (08:00 and 22:00)";
                scheduleTimeError.setContentText(timeError);
                scheduleTimeError.show();
            }
        }
    }

    public void cancelButtonListener(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public boolean verifyIfWithinHours()
    {
        boolean withinBusinessHrs;
        LocalTime localStartTime = LocalTime.parse(startTimeTextField.getText());
        LocalTime localEndTime = LocalTime.parse(endTimeTextField.getText());
        LocalDate localStartDate = startDateSelector.getValue();
        LocalDate localEndDate = endDateSelector.getValue();
        ZonedDateTime localZonedApptStartTime =
                ZonedDateTime.of(localStartDate, localStartTime, ZoneId.of( TimeZone.getDefault().getID()));
        ZonedDateTime easternZonedApptStartTime =
                localZonedApptStartTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime localZonedApptEndTime =
                ZonedDateTime.of(localEndDate, localEndTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime easternZonedApptEndTime =
                localZonedApptEndTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalTime businessOpenLocalTime = LocalTime.of(8,0,0);
        ZonedDateTime businessOpenTime = ZonedDateTime.of(localStartDate, businessOpenLocalTime, ZoneId.of("America/New_York"));
        LocalTime businessCloseLocalTime = LocalTime.of(22, 0, 0);
        ZonedDateTime businessCloseTime = ZonedDateTime.of(localStartDate, businessCloseLocalTime, ZoneId.of("America/New_York"));
        withinBusinessHrs =
                (easternZonedApptStartTime.isAfter(businessOpenTime) ||
                        easternZonedApptStartTime.equals(businessOpenTime)) &&
                        ((easternZonedApptEndTime.isBefore(businessCloseTime)) ||
                                easternZonedApptEndTime.equals(businessCloseTime));
        return withinBusinessHrs;
    }

    public boolean customerApptOverlap() {
        boolean apptOverlap = false;
        String customerIdString = customerIdDropdown.getValue();
        int customerIdInt = Integer.parseInt(customerIdString.substring(0, customerIdString.indexOf(" ", 0)));
        LocalDate localStartDate = startDateSelector.getValue();
        LocalTime localStartTime = LocalTime.parse(startTimeTextField.getText());
        LocalDate localEndDate = endDateSelector.getValue();
        LocalTime localEndTime = LocalTime.parse(endTimeTextField.getText());
        ZonedDateTime userTimeZoneStartTime = ZonedDateTime.of(localStartDate, localStartTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime utcStartTime = userTimeZoneStartTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp utcStartTimestamp = Timestamp.from(utcStartTime.toInstant());
        ZonedDateTime userTimeZoneEndTime = ZonedDateTime.of(localEndDate, localEndTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime utcEndTime = userTimeZoneEndTime.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp utcEndTimestamp = Timestamp.from(utcEndTime.toInstant());
        ObservableList<Appointment> customerAppointments = DatabaseAppointments.getAllApptsForCustomer(customerIdInt);
        ObservableList<Appointment> overlappingAppointments =
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