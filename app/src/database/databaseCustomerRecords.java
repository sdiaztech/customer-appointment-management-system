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

/** Helper functions to access Customer Records */
public class databaseCustomerRecords {

    /** Retrieves all customer records from the customer table.
     * @return ArrayList containing all the customers from the database.
     * @throws SQLException if the SQL is misshapen.
     */
    public static ObservableList<Customer> getAllCustomerRecords() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        // Retrieve all stored customers from the database
        try {
            String sqlQuery = "SELECT * FROM customers";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet customersResultSet = preparedStatement.executeQuery();

            while (customersResultSet.next()) {
                // Get the information from the database
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

                // Convert all to Properties to display in the TableView
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

                // Use the Division ID to look up the name of the Division
                String customerDivisionName = searchForDivisionName(customerDivisionId);
                StringProperty customerDivisionNameProperty = new SimpleStringProperty(customerDivisionName);

                // Use Division name to look up the Country ID
                int customerCountryId = searchForCountryId(customerDivisionIdProperty);
                IntegerProperty customerCountryIdProperty = new SimpleIntegerProperty(customerCountryId);

                // Use Country ID to look up Country name
                String customerCountryName = searchForCountryName(customerCountryId);
                StringProperty customerCountryNameProperty = new SimpleStringProperty(customerCountryName);

                System.out.println("Customer info: " + customerId + " " + customerName + " " + customerAddress +
                        " " + customerZipCode + " " + customerPhoneNumber + " " + customerDivisionId + " " +
                        customerDivisionName + " " + customerCountryId + " " + customerCountryName );


                // Create new Customer using data obtained from the database
                Customer currentCustomer = new Customer(customerIdProperty, customerNameProperty, customerAddressProperty,
                        customerZipCodeProperty, customerPhoneNumberProperty, createDateProperty, createdByProperty,
                        lastUpdateProperty, lastUpdatedByProperty, customerDivisionIdProperty, customerDivisionNameProperty,
                        customerCountryIdProperty, customerCountryNameProperty);

                // Add the customer to the customers ArrayList
                customers.add(currentCustomer);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    /** Uses a Division ID to look up the name of the Division in the first_level_divisions table.
     * @param divisionId The ID number of the Division
     * @return Returns the name of the Division
     */
    public static String searchForDivisionName(int divisionId) throws SQLException {
        String divisionName = "";
        try {
            String sqlQuery = "SELECT * FROM first_level_divisions WHERE Division_ID =" + divisionId;
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                divisionName = resultSet.getString("Division");
                System.out.println(divisionName);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return divisionName;
    }

    /** Uses a Division ID to look up the ID number of the Country that it belongs to in the first_level_divisions table.
     * @param divisionIdProperty The ID number of the Division as an IntegerProperty
     * @return ID number of the Country that the Division belongs to.
     */
    public static int searchForCountryId(IntegerProperty divisionIdProperty) throws SQLException {
        int countryId  = 0;
        int divisionId = divisionIdProperty.getValue();

        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Division_ID =" + divisionId;
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                countryId = resultSet.getInt("Country_ID");
                System.out.println("CountryID: " + countryId);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return countryId;
    }

    /** Looks up the Country name by using corresponding Country ID number
     * @param countryId - Country's ID number
     * @return name of the Country designated to the countryId.
     * @throws SQLException if the SQL is misshapen.
     */
    public static String searchForCountryName(int countryId) throws SQLException {
        String countryName = "";

        try {
            String sqlQuery = "SELECT * FROM countries WHERE Country_ID=" + countryId;
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet countriesResultSet = preparedStatement.executeQuery();

            while (countriesResultSet.next()) {
                 countryName = countriesResultSet.getString("Country");
                System.out.println(countryName);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return countryName;
    }

    /** Takes in the 'division' from the 'DivisionComboBox' and returns only the numerical value of the 'Division_ID'
     * @param division - the 'division' selected in the 'DivisionComboBox'
     * @return 'Division_ID' as a String value
     */
    public static String searchForDivisionId(String division) {
        System.out.println("Division: " + division);
        // Remove all characters past the ID number
        String divisionIdString = division.toString().substring(0, division.toString().indexOf(" ", 0));
        System.out.println("divisionIdString: " + divisionIdString);
        return divisionIdString;
    }

}