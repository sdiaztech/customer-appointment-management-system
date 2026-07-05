package controller;

import database.DatabaseAppointments;
import database.DatabaseQuery;
import database.JDBC;
import model.Appointment;
import static controller.ViewAppointmentsController.clientAppointments;
import static main.Main.randNumber;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class AddAppointmentController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        randAppointmentId();
        fillContactDropdown();
        fillCustomerIdDropdown();
        fillAppointmentTypeDropdown();
        fillUserIdDropdown();
    }

    public void randAppointmentId() {
        Random randomNumber = new Random();
        randNumber = randomNumber.nextInt(999);
        apptIdTextField.setText(String.valueOf(randNumber));
    }

    public void fillContactDropdown() {
        ObservableList<String> contactsList = DatabaseAppointments.getContacts();
        contactDropdown.setItems(contactsList);
    }

    public void fillCustomerIdDropdown() {
        ObservableList<String> customersList = DatabaseAppointments.getCustomers();
        customerIdDropdown.setItems(customersList);
    }

    public void fillAppointmentTypeDropdown() {
        ObservableList<String> apptsTypesList =
                FXCollections.observableArrayList("Initial", "Planning", "De-Briefing", "Virtual", "Closing");
        apptTypeDropdown.setItems(apptsTypesList);
    }

    public void fillUserIdDropdown() {
        ObservableList<String> userIdList = DatabaseAppointments.getUsers();
        userIdDropdown.setItems(userIdList);
    }


    public void addApptButtonListener(ActionEvent actionEvent) {
        if (apptsOverlap()) {
            Alert apptOverlapError = new Alert(Alert.AlertType.ERROR,"New appointment overlaps with a previously scheduled one.");
            apptOverlapError.setTitle("Appointment Overlap Error");
            apptOverlapError.show();
        }
        else {
            if (apptWithinHrs()) {
                String customerIdString = customerIdDropdown.getValue();
                int customerIdInt = Integer.parseInt(customerIdString.substring(0, customerIdString.indexOf(" ", 0)));
                IntegerProperty customerIdIntProperty = new SimpleIntegerProperty(customerIdInt);
                String userIdString = userIdDropdown.getValue();
                int userIdInt = Integer.parseInt(userIdString.substring(0, userIdString.indexOf(" ", 0)));
                IntegerProperty userIdIntProperty = new SimpleIntegerProperty(userIdInt);

                StringProperty titleProperty = new SimpleStringProperty(apptTitleTextField.getText());
                StringProperty descriptionProperty = new SimpleStringProperty(apptDescriptionTextField.getText());
                StringProperty locationProperty = new SimpleStringProperty(apptLocationTextField.getText());
                StringProperty apptTypeProperty = new SimpleStringProperty(apptTypeDropdown.getValue());
                String contactIdString = contactDropdown.getValue();
                int contactIdInt = Integer.parseInt(contactIdString.substring(0,contactIdString.indexOf(" ", 0)));
                IntegerProperty contactIdProperty = new SimpleIntegerProperty(contactIdInt);
                LocalDate startDate = startDateSelector.getValue();
                LocalTime startTime = LocalTime.parse(startTimeTextField.getText());
                LocalDateTime startLocalDateTime = startDate.atTime(startTime);
                ZonedDateTime startTimeUTC = ZonedDateTime.of(startLocalDateTime, ZoneId.of("UTC"));
                Timestamp startTimestamp = Timestamp.valueOf(startTimeUTC.toLocalDateTime());
                StringProperty startTimeProperty = new SimpleStringProperty(startTimestamp.toString());
                LocalDate endDate = endDateSelector.getValue();
                LocalTime endTime = LocalTime.parse(endTimeTextField.getText());
                LocalDateTime endLocalDateTime = endDate.atTime(endTime);
                ZonedDateTime endTimeUTC = ZonedDateTime.of(endLocalDateTime, ZoneId.of("UTC"));
                Timestamp endTimestamp = Timestamp.valueOf(endTimeUTC.toLocalDateTime());
                StringProperty endTimeProperty = new SimpleStringProperty(endTimestamp.toString());
                IntegerProperty currApptIdProperty = new SimpleIntegerProperty(randNumber);
                Connection connection = JDBC.getConnection();
                String sqlInsertQuery = "INSERT INTO client_schedule.appointments(Title, Description, Location, " +
                        "Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try {
                    DatabaseQuery.setPreparedStatement(connection, sqlInsertQuery);
                    PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();
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
                clientAppointments.add(new Appointment(currApptIdProperty, titleProperty, descriptionProperty,
                        locationProperty, apptTypeProperty, startTimeProperty, endTimeProperty, userIdIntProperty,
                        customerIdIntProperty, contactIdProperty));
                Stage stage = (Stage)addApptButton.getScene().getWindow();
                stage.close();
            }
            else {
                TimeZone userTimeZone = TimeZone.getDefault();
                LocalTime businessOpenLocalTime = LocalTime.of(8,0,0);
                ZonedDateTime businessOpen = ZonedDateTime.of(LocalDate.now(),businessOpenLocalTime, ZoneId.of("America/New_York"));
                LocalTime businessCloseLocalTime = LocalTime.of(22, 0, 0);
                ZonedDateTime businessClose = ZonedDateTime.of(LocalDate.now(), businessCloseLocalTime, ZoneId.of("America/New_York"));
                ZonedDateTime businessOpenInUserTimeZone = businessOpen.withZoneSameInstant(ZoneId.of(userTimeZone.getID()));
                ZonedDateTime businessCloseInUserTimeZone = businessClose.withZoneSameInstant(ZoneId.of(userTimeZone.getID()));
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

    public boolean apptWithinHrs() {
        boolean withinBusinessHrs;
        LocalTime startTime = LocalTime.parse(startTimeTextField.getText());
        LocalTime endTime = LocalTime.parse(endTimeTextField.getText());
        LocalDate startDate = startDateSelector.getValue();
        LocalDate endDate = endDateSelector.getValue();
        ZonedDateTime localApptStartTime = ZonedDateTime.of(startDate, startTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime easternApptStartTime = localApptStartTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime localZonedAppointmentEndTime = ZonedDateTime.of(endDate, endTime, ZoneId.of(TimeZone.getDefault().getID()));
        ZonedDateTime easternZonedApptEndTime = localZonedAppointmentEndTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalTime businessOpenLocalTime = LocalTime.of(8, 0, 0);
        ZonedDateTime businessOpenTime = ZonedDateTime.of( startDate, businessOpenLocalTime, ZoneId.of("America/New_York") );
        LocalTime businessCloseLocalTime = LocalTime.of( 22, 0, 0);
        ZonedDateTime businessCloseTime = ZonedDateTime.of(startDate, businessCloseLocalTime, ZoneId.of("America/New_York"));
        withinBusinessHrs =
                easternApptStartTime.isAfter(businessOpenTime) ||
                        easternApptStartTime.equals(businessOpenTime) &&
                                easternZonedApptEndTime.isBefore(businessCloseTime) ||
                                        easternZonedApptEndTime.equals(businessCloseTime);
        return withinBusinessHrs;
    }

    public boolean apptsOverlap() {
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

    public void cancelButtonListener(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}