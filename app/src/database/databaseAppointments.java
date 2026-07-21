package database;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public final class DatabaseAppointments {
    private static final String APPOINTMENT_COLUMNS =
            "Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID";

    private DatabaseAppointments() {
    }

    public static ObservableList<Appointment> getAllAppts() throws ParseException {
        return queryAppointments("SELECT " + APPOINTMENT_COLUMNS + " FROM appointments", null);
    }

    public static ObservableList<Appointment> getAllApptsForComparison() throws SQLException {
        return queryAppointments("SELECT " + APPOINTMENT_COLUMNS + " FROM appointments", null);
    }

    public static ObservableList<Appointment> getAllApptsForCustomer(int customerId) {
        return queryAppointments(
                "SELECT " + APPOINTMENT_COLUMNS + " FROM appointments WHERE Customer_ID = ?",
                customerId);
    }

    private static ObservableList<Appointment> queryAppointments(String sql, Integer customerId) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sql)) {
            if (customerId != null) {
                statement.setInt(1, customerId);
            }

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    appointments.add(mapAppointment(results));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    private static Appointment mapAppointment(ResultSet results) throws SQLException {
        return new Appointment(
                new SimpleIntegerProperty(results.getInt("Appointment_ID")),
                new SimpleStringProperty(results.getString("Title")),
                new SimpleStringProperty(results.getString("Description")),
                new SimpleStringProperty(results.getString("Location")),
                new SimpleStringProperty(results.getString("Type")),
                new SimpleStringProperty(results.getTimestamp("Start").toString()),
                new SimpleStringProperty(results.getTimestamp("End").toString()),
                new SimpleIntegerProperty(results.getInt("User_ID")),
                new SimpleIntegerProperty(results.getInt("Customer_ID")),
                new SimpleIntegerProperty(results.getInt("Contact_ID")));
    }

    public static ObservableList<String> getContacts() {
        return queryIdNameChoices(
                "SELECT Contact_ID, Contact_Name FROM contacts",
                "Contact_ID", "Contact_Name");
    }

    public static ObservableList<String> getCustomers() {
        return queryIdNameChoices(
                "SELECT Customer_ID, Customer_Name FROM customers",
                "Customer_ID", "Customer_Name");
    }

    public static ObservableList<String> getUsers() {
        return queryIdNameChoices(
                "SELECT User_ID, User_Name FROM users",
                "User_ID", "User_Name");
    }

    private static ObservableList<String> queryIdNameChoices(String sql, String idColumn, String nameColumn) {
        ObservableList<String> choices = FXCollections.observableArrayList();

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                choices.add(results.getInt(idColumn) + " " + results.getString(nameColumn));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return choices;
    }

    public static String getCustomerIdName(int customerId) {
        String customerName = "";
        String sql = "SELECT Customer_Name FROM customers WHERE Customer_ID = ?";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    customerName = results.getString("Customer_Name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerId + " " + customerName;
    }

    public static int getCustomerIdString(String customerOption) {
        return Integer.parseInt(customerOption.substring(0, customerOption.indexOf(' ')));
    }
}
