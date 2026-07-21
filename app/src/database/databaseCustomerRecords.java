package database;

import model.Customer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseCustomerRecords {

    private DatabaseCustomerRecords() {
    }

    public static ObservableList<Customer> getAllCustomerRecords() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String sqlQuery = "SELECT c.*, d.Division, co.Country_ID, co.Country "
                + "FROM customers c "
                + "JOIN first_level_divisions d ON c.Division_ID = d.Division_ID "
                + "JOIN countries co ON d.Country_ID = co.Country_ID";
        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
             ResultSet customersResultSet = preparedStatement.executeQuery()) {
            while (customersResultSet.next()) {
                int customerId = customersResultSet.getInt("Customer_ID");
                String customerName = customersResultSet.getString("Customer_Name");
                String customerAddress = customersResultSet.getString("Address");
                String customerZipCode = customersResultSet.getString("Postal_Code");
                String customerPhoneNumber = customersResultSet.getString("Phone");
                int customerDivisionId = customersResultSet.getInt("Division_ID");

                IntegerProperty customerIdProperty = new SimpleIntegerProperty(customerId);
                StringProperty customerNameProperty = new SimpleStringProperty(customerName);
                StringProperty customerAddressProperty = new SimpleStringProperty(customerAddress);
                StringProperty customerZipCodeProperty = new SimpleStringProperty(customerZipCode);
                StringProperty customerPhoneNumberProperty = new SimpleStringProperty(customerPhoneNumber);
                IntegerProperty customerDivisionIdProperty = new SimpleIntegerProperty(customerDivisionId);

                String customerDivisionName = customersResultSet.getString("Division");
                StringProperty customerDivisionNameProperty = new SimpleStringProperty(customerDivisionName);

                int customerCountryId = customersResultSet.getInt("Country_ID");
                IntegerProperty customerCountryIdProperty = new SimpleIntegerProperty(customerCountryId);

                String customerCountryName = customersResultSet.getString("Country");
                StringProperty customerCountryNameProperty = new SimpleStringProperty(customerCountryName);

                Customer currentCustomer = new Customer(customerIdProperty, customerNameProperty, customerAddressProperty,
                        customerZipCodeProperty, customerDivisionNameProperty, customerCountryNameProperty,
                        customerPhoneNumberProperty, customerDivisionIdProperty, customerCountryIdProperty);

                customers.add(currentCustomer);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public static int searchForCountryId(IntegerProperty divisionIdProperty) throws SQLException {
        int countryId  = 0;
        int divisionId = divisionIdProperty.getValue();

        String sql = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?";
        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, divisionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    countryId = resultSet.getInt("Country_ID");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return countryId;
    }

    public static String searchForDivisionId(String division) {
        return division.substring(0, division.indexOf(' '));
    }

}
