package database;

import model.Appointments;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.ParseException;

public class databaseAppointments {
    /** Retrieves all 'Appointment' records from the appointment table.
     * @return ArrayList containing all the customers from the database.
     * @throws SQLException if the SQL is misshapen.
     */
    public static ObservableList<Appointments> getAllAppts() throws ParseException {

        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        // Retrieve stored appointments from database
        try {

            String sqlQuery = "SELECT * FROM appointments";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Get the information from the database
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

                // Convert all to Properties to display in the TableView
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

                // Create a new Appointment using the data obtained from the database
                Appointments currentAppointments = new Appointments(
                        apptIdProperty, titleProperty, descriptionProperty, locationProperty, typeProperty,
                        startProperty, endProperty, userIdProperty, customerIdProperty, contactIdProperty);

                // Add the customer to customers ArrayList
                appointments.add(currentAppointments);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public static ObservableList<Appointments> getAllApptsForComparison() throws SQLException {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM appointments";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet apptsResultSet = preparedStatement.executeQuery();

            while (apptsResultSet.next()) {
                System.out.println(apptsResultSet);

                // Get the information from the database
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

                // Convert all to Properties to display in the TableView
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


                // Create new Appointment using the data obtained from the database
                Appointments currAppointments = new Appointments(
                        apptIdProperty, titleProperty, descriptionProperty, locationProperty, typeProperty,
                        startProperty, endProperty, userIdProperty, customerIdProperty, contactIdProperty);

                // Add customer to the customers ArrayList
                appointments.add(currAppointments);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public static ObservableList<Appointments> getAllApptsForCustomer(int customerIdToFind)
    {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM appointments WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, customerIdToFind);

            ResultSet apptsResultSet = preparedStatement.executeQuery();

            while (apptsResultSet.next()) {
                System.out.println(apptsResultSet);

                // Get the information from the database
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

                // Convert all to Properties to display in the TableView
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


                // Create new Appointment using the data obtained from the database
                Appointments currentAppointments = new Appointments(
                        apptIdProperty, titleProperty, descriptionProperty, locationProperty, typeProperty,
                        startProperty, endProperty, userIdProperty, customerIdProperty, contactIdProperty);

                // Add customer to the customers ArrayList
                appointments.add(currentAppointments);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }


    /** Getter gets a list of all Contacts
     * @return 'ObservableList' of 'Contact'(s) from the database
     */
    public static ObservableList<String> getContacts() {
        ObservableList<String> contacts = FXCollections.observableArrayList();
        try {
            String sqlQuery = "Select * FROM client_schedule.contacts";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet contactsResultSet = preparedStatement.executeQuery();

            while (contactsResultSet.next()) {
                // Get the information from the database
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

    /** Getter gets a list of all Customers
     * @return 'ObservableList' of all Customers
     */
    public static ObservableList<String> getCustomers() {
        System.out.println("Get Customers");

        ObservableList<String> customerChoices = FXCollections.observableArrayList();
        // Get the data from the database and add it to the customers list
        try {
            // Make the SQL query
            String sqlQuery = "SELECT * FROM client_schedule.customers";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Get the data from the database
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

    /** Getter gets a list of Users
     * @return An 'ObservableList' containing each 'User'
     */
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

    /** Getter gets a customer's ID number and full name
     * @param customerIdNumber - customer's ID number
     * @return customer's ID number and full name concatenated
     */
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

    /** Returns the Customer_ID as a 'String' value
     * @param customerOption - a single customer
     * @return Customer_ID as a 'String'
     */
    public static int getCustomerIdString(String customerOption) {
        String customerIdString = customerOption.substring(0, customerOption.indexOf(" ", 0));
        return Integer.parseInt(customerIdString);
    }
}
