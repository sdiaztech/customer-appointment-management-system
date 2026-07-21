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
                String createDate = customersResultSet.getString("Create_Date");
                String createdBy = customersResultSet.getString("Created_By");
                String lastUpdate = customersResultSet.getString("Last_Update");
                String lastUpdatedBy = customersResultSet.getString("Last_Updated_By");

                IntegerProperty customerIdProperty = new SimpleIntegerProperty(customerId);
                StringProperty customerNameProperty = new SimpleStringProperty(customerName);
                StringProperty customerAddressProperty = new SimpleStringProperty(customerAddress);
                StringProperty customerZipCodeProperty = new SimpleStringProperty(customerZipCode);
                StringProperty customerPhoneNumberProperty = new SimpleStringProperty(customerPhoneNumber);
                IntegerProperty customerDivisionIdProperty = new SimpleIntegerProperty(customerDivisionId);
                StringProperty createDateProperty = new SimpleStringProperty(createDate);
                StringProperty createdByProperty = new SimpleStringProperty(createdBy);
                StringProperty lastUpdateProperty = new SimpleStringProperty(lastUpdate);
                StringProperty lastUpdatedByProperty = new SimpleStringProperty(lastUpdatedBy);

                String customerDivisionName = customersResultSet.getString("Division");
                StringProperty customerDivisionNameProperty = new SimpleStringProperty(customerDivisionName);

                int customerCountryId = customersResultSet.getInt("Country_ID");
                IntegerProperty customerCountryIdProperty = new SimpleIntegerProperty(customerCountryId);

                String customerCountryName = customersResultSet.getString("Country");
                StringProperty customerCountryNameProperty = new SimpleStringProperty(customerCountryName);

                Customer currentCustomer = new Customer(customerIdProperty, customerNameProperty, customerAddressProperty,
                        customerZipCodeProperty, customerPhoneNumberProperty, createDateProperty, createdByProperty,
                        lastUpdateProperty, lastUpdatedByProperty, customerDivisionIdProperty, customerDivisionNameProperty,
                        customerCountryIdProperty, customerCountryNameProperty);

                customers.add(currentCustomer);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public static String searchForDivisionName(int divisionId) throws SQLException {
        String divisionName = "";
        String sqlQuery = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, divisionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                divisionName = resultSet.getString("Division");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return divisionName;
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

    public static String searchForCountryName(int countryId) throws SQLException {
        String countryName = "";

        String sqlQuery = "SELECT Country FROM countries WHERE Country_ID = ?";
        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, countryId);
            try (ResultSet countriesResultSet = preparedStatement.executeQuery()) {
                if (countriesResultSet.next()) {
                    countryName = countriesResultSet.getString("Country");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return countryName;
    }

    public static String searchForDivisionId(String division) {
        return division.substring(0, division.indexOf(' '));
    }

}
