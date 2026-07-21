package controller;

import database.DatabaseQuery;
import database.JDBC;
import model.Appointment;
import main.Main;
import static database.DatabaseAppointments.getAllAppts;
import static database.DatabaseAppointments.getAllApptsForComparison;

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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewAppointmentsController implements Initializable {
    public static ObservableList<Appointment> clientAppointments = FXCollections.observableArrayList();

    @FXML
    private TableView<Appointment> viewApptsTableView;
    @FXML
    private Button viewCustomersButton;
    @FXML
    private Label deleteApptConfirmationLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            displayAppts();
        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void displayAppts() throws SQLException, ParseException {
        clientAppointments = getAllAppts();

        TableColumn<Appointment,Number> columnApptId = new TableColumn<>("Appointment_ID");
        TableColumn<Appointment,String> columnTitle = new TableColumn<>("Title");
        TableColumn<Appointment,String> columnDescription = new TableColumn<>("Description");
        TableColumn<Appointment,String> columnLocation = new TableColumn<>("Location");
        TableColumn<Appointment,String> columnType = new TableColumn<>("Type");
        TableColumn<Appointment,String> columnStartTime = new TableColumn<>("Start");
        TableColumn<Appointment,String> columnEndTime = new TableColumn<>("End");
        TableColumn<Appointment,Number> columnCustomerId = new TableColumn<>("Customer_ID");
        TableColumn<Appointment,Number> columnUserId = new TableColumn<>("User_ID");
        TableColumn<Appointment,Number> columnContactId = new TableColumn<>("Contact_ID");

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

        viewApptsTableView.setItems(clientAppointments);
        viewApptsTableView.getColumns().addAll(List.of(
                columnApptId, columnTitle, columnDescription, columnLocation, columnType,
                columnStartTime, columnEndTime, columnCustomerId, columnUserId, columnContactId));
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

    public void exitButtonListener(ActionEvent actionEvent) {
        JDBC.closeConnection();
        Platform.exit();
    }

    public void deleteButtonListener(ActionEvent actionEvent) throws SQLException {
        Appointment deleteSelection = viewApptsTableView.getSelectionModel().getSelectedItem();
        int apptId = deleteSelection.getApptId();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setContentText("Are you sure you want to delete appointment with ID " + apptId + "?");
        Optional<ButtonType> input = alert.showAndWait();

        if ((input.isPresent()) && (input.get() == ButtonType.OK)) {
            String sqlDeleteQuery = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?";
            DatabaseQuery.setPreparedStatement(JDBC.getConnection(), sqlDeleteQuery);
            PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();
            preparedStatement.setString(1, String.valueOf(apptId));
            preparedStatement.execute();
            clientAppointments.remove(deleteSelection);
            deleteApptConfirmationLabel.setText("Appointment with ID " + apptId + " cancelled.");
            viewApptsTableView.refresh();
        }
    }

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

    public void updateButtonListener(ActionEvent actionEvent) {
        Main.selectedAppointments = viewApptsTableView.getSelectionModel().getSelectedItem();
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

    public void allViewRadioButtonListener(ActionEvent actionEvent) {
        deleteApptConfirmationLabel.setText("");
        viewApptsTableView.getItems().clear();
        try {
            displayAppts();
        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void monthViewRadioButtonListener(ActionEvent actionEvent) {
        deleteApptConfirmationLabel.setText("");
        Month currMonth = LocalDate.now().getMonth();
        int currMonthInt = currMonth.getValue();
        try {
            ObservableList<Appointment> allAppointments = getAllApptsForComparison();
            ObservableList<Appointment> apptsThisMonth =
                    allAppointments.stream().filter(appt -> appt.getStartMonthInt() + 1 == currMonthInt).collect(Collectors.toCollection(FXCollections::observableArrayList));
            viewApptsTableView.getColumns().clear();
            TableColumn<Appointment,Number> columnApptId = new TableColumn<>("Appointment_ID");
            TableColumn<Appointment,String> columnTitle = new TableColumn<>("Title");
            TableColumn<Appointment,String> columnDescription = new TableColumn<>("Description");
            TableColumn<Appointment,String> columnLocation = new TableColumn<>("Location");
            TableColumn<Appointment,String> columnType = new TableColumn<>("Type");
            TableColumn<Appointment,String> columnStart = new TableColumn<>("Start");
            TableColumn<Appointment,String> columnEnd = new TableColumn<>("End");
            TableColumn<Appointment,Number> columnCustomerId = new TableColumn<>("Customer_ID");
            TableColumn<Appointment,Number> columnUserId = new TableColumn<>("User_ID");
            TableColumn<Appointment,Number> columnContactId = new TableColumn<>("Contact_ID");
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
            viewApptsTableView.setItems(clientAppointments);
            viewApptsTableView.getColumns().addAll(List.of(
                    columnApptId, columnTitle, columnDescription, columnLocation, columnType,
                    columnStart, columnEnd, columnCustomerId, columnUserId, columnContactId));
            viewApptsTableView.setItems(apptsThisMonth);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void weekViewRadioButtonListener(ActionEvent actionEvent)
    {
        deleteApptConfirmationLabel.setText("");
        Calendar calendar = Calendar.getInstance();
        int currentWeekInt = calendar.get(Calendar.WEEK_OF_YEAR);
        try {
            ObservableList<Appointment> allAppointments = getAllApptsForComparison();
            ObservableList<Appointment> appointmentsThisWeek =
                    allAppointments.stream().filter(appt -> appt.getStartWeek() == currentWeekInt).collect(Collectors.toCollection(FXCollections::observableArrayList));
            viewApptsTableView.getColumns().clear();
            TableColumn<Appointment,Number> columnApptId = new TableColumn<>("Appointment_ID");
            TableColumn<Appointment,String> columnTitle = new TableColumn<>("Title");
            TableColumn<Appointment,String> columnDescription = new TableColumn<>("Description");
            TableColumn<Appointment,String> columnLocation = new TableColumn<>("Location");
            TableColumn<Appointment,String> columnType = new TableColumn<>("Type");
            TableColumn<Appointment,String> columnStart = new TableColumn<>("Start");
            TableColumn<Appointment,String> columnEnd = new TableColumn<>("End");
            TableColumn<Appointment,Number> columnCustomerId = new TableColumn<>("Customer_ID");
            TableColumn<Appointment,Number> columnUserId = new TableColumn<>("User_ID");
            TableColumn<Appointment,Number> columnContactId = new TableColumn<>("Contact_ID");
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
            viewApptsTableView.setItems(clientAppointments);
            viewApptsTableView.getColumns().addAll(List.of(
                    columnApptId, columnTitle, columnDescription, columnLocation, columnType,
                    columnStart, columnEnd, columnCustomerId, columnUserId, columnContactId));
            viewApptsTableView.setItems(appointmentsThisWeek);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
