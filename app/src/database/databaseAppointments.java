package database;

import model.Appointment;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.ParseException;

public class DatabaseAppointments {
    public static ObservableList<Appointment> getAllAppts() throws ParseException {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {

            String sqlQuery = "SELECT * FROM appointments";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int apptId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp start = Timestamp.valueOf(resultSet.getTimestamp("Start").toLocalDateTime());
                Timestamp end = Timestamp.valueOf(resultSet.getTimestamp("End").toLocalDateTime());
                int customerId = resultSet.getInt("Customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");

                IntegerProperty apptIdProperty = new SimpleIntegerProperty(apptId);
                StringProperty titleProperty = new SimpleStringProperty(title);
                StringProperty descriptionProperty = new SimpleStringProperty(description);
                StringProperty locationProperty = new SimpleStringProperty(location);
                StringProperty typeProperty = new SimpleStringProperty(type);
                StringProperty startProperty = new SimpleStringProperty(start.toString());
                StringProperty endProperty = new SimpleStringProperty(end.toString());
                IntegerProperty customerIdProperty = new SimpleIntegerProperty(customerId);
                IntegerProperty userIdProperty = new SimpleIntegerProperty(userId);
                IntegerProperty contactIdProperty = new SimpleIntegerProperty(contactId);

                Appointment currentAppointments = new Appointment(
                        apptIdProperty, titleProperty, descriptionProperty, locationProperty, typeProperty,
                        startProperty, endProperty, userIdProperty, customerIdProperty, contactIdProperty);

                appointments.add(currentAppointments);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public static ObservableList<Appointment> getAllApptsForComparison() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM appointments";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet apptsResultSet = preparedStatement.executeQuery();

            while (apptsResultSet.next()) {
                int apptId = apptsResultSet.getInt("Appointment_ID");
                String title = apptsResultSet.getString("Title");
                String description = apptsResultSet.getString("Description");
                String location = apptsResultSet.getString("Location");
                String type  = apptsResultSet.getString("Type");
                Timestamp start = apptsResultSet.getTimestamp("Start");
                Timestamp end = apptsResultSet.getTimestamp("End");
                int customerId = apptsResultSet.getInt("Customer_ID");
                int userId = apptsResultSet.getInt("User_ID");
                int contactId = apptsResultSet.getInt("Contact_ID");

                IntegerProperty apptIdProperty = new SimpleIntegerProperty(apptId);
                StringProperty titleProperty = new SimpleStringProperty(title);
                StringProperty descriptionProperty = new SimpleStringProperty(description);
                StringProperty locationProperty = new SimpleStringProperty(location);
                StringProperty typeProperty = new SimpleStringProperty(type);
                StringProperty startProperty = new SimpleStringProperty(start.toString());
                StringProperty endProperty = new SimpleStringProperty(end.toString());
                IntegerProperty customerIdProperty = new SimpleIntegerProperty(customerId);
                IntegerProperty userIdProperty = new SimpleIntegerProperty(userId);
                IntegerProperty contactIdProperty = new SimpleIntegerProperty(contactId);

                Appointment currAppointments = new Appointment(
                        apptIdProperty, titleProperty, descriptionProperty, locationProperty, typeProperty,
                        startProperty, endProperty, userIdProperty, customerIdProperty, contactIdProperty);

                appointments.add(currAppointments);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public static ObservableList<Appointment> getAllApptsForCustomer(int customerIdToFind)
    {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM appointments WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, customerIdToFind);

            ResultSet apptsResultSet = preparedStatement.executeQuery();

            while (apptsResultSet.next()) {
                int apptId = apptsResultSet.getInt("Appointment_ID");
                String title = apptsResultSet.getString("Title");
                String description = apptsResultSet.getString("Description");
                String location = apptsResultSet.getString("Location");
                String type = apptsResultSet.getString("Type");
                Timestamp start = apptsResultSet.getTimestamp("Start");
                Timestamp end = apptsResultSet.getTimestamp("End");
                int customerId = apptsResultSet.getInt("Customer_ID");
                int userId = apptsResultSet.getInt("User_ID");
                int contactId = apptsResultSet.getInt("Contact_ID");

                IntegerProperty apptIdProperty = new SimpleIntegerProperty(apptId);
                StringProperty titleProperty = new SimpleStringProperty(title);
                StringProperty descriptionProperty = new SimpleStringProperty(description);
                StringProperty locationProperty = new SimpleStringProperty(location);
                StringProperty typeProperty = new SimpleStringProperty(type);
                StringProperty startProperty = new SimpleStringProperty(start.toString());
                StringProperty endProperty = new SimpleStringProperty(end.toString());
                IntegerProperty customerIdProperty = new SimpleIntegerProperty(customerId);
                IntegerProperty userIdProperty = new SimpleIntegerProperty(userId);
                IntegerProperty contactIdProperty = new SimpleIntegerProperty(contactId);

                Appointment currentAppointments = new Appointment(
                        apptIdProperty, titleProperty, descriptionProperty, locationProperty, typeProperty,
                        startProperty, endProperty, userIdProperty, customerIdProperty, contactIdProperty);

                appointments.add(currentAppointments);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static ObservableList<String> getContacts() {
        ObservableList<String> contacts = FXCollections.observableArrayList();
        try {
            String sqlQuery = "Select * FROM client_schedule.contacts";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet contactsResultSet = preparedStatement.executeQuery();

            while (contactsResultSet.next()) {
                int contactId = contactsResultSet.getInt("Contact_ID");
                String contactName = contactsResultSet.getString("Contact_Name");

                String contact = contactId + " " + contactName;
                contacts.add(contact);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    public static ObservableList<String> getCustomers() {
        ObservableList<String> customerChoices = FXCollections.observableArrayList();
        try {
            String sqlQuery = "SELECT * FROM client_schedule.customers";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");

                String customerChoice = customerId + " " + customerName;
                customerChoices.add(customerChoice);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return customerChoices;
    }

    public static ObservableList<String> getUsers() {
        ObservableList<String> users = FXCollections.observableArrayList();

        try {
            String selectUsers = "SELECT * FROM client_schedule.users";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(selectUsers);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");

                String user = userId + " " + userName;
                users.add(user);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static String getCustomerIdName(int customerIdNumber) {
        String customerName = "";

        try {
            String sqlQuery = "SELECT * FROM client_schedule.customers WHERE Customer_ID= ?";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, String.valueOf(customerIdNumber));
            ResultSet customerResultSet = preparedStatement.executeQuery();

            while (customerResultSet.next()) {
                customerName = customerResultSet.getString("Customer_Name");
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        String customerIdName = customerIdNumber + " " + customerName;

        return customerIdName;
    }

    public static int getCustomerIdString(String customerOption) {
        String customerIdString = customerOption.substring(0, customerOption.indexOf(" ", 0));
        return Integer.parseInt(customerIdString);
    }
}
