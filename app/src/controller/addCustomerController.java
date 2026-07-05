package controller;

import database.*;
import model.Country;
import model.Customer;
import model.Division;
import static controller.viewCustomersController.customerRecords;
import static main.Main.currId;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class addCustomerController implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField customerIDTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField zipCodeTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private ComboBox<Country> countryDropdown;
    @FXML
    private ComboBox<Division> divisionDropdown;


    /** Initialize the Add Customer Scene
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        randIdGenerator();
        fillCountryDropdown();
    }

    /** Auto generate new user ID number */
    public void randIdGenerator() {
        currId += 1;
        customerIDTextField.setText(String.valueOf(currId));
    }

    /** Populate the ComboBox that contains the choices for 'Country' */
    public void fillCountryDropdown() {
        // Retrieve list of countries from database
        ObservableList<Country> countryList = databaseCountries.getAllCountries();
        System.out.println(countryList);

        // Populate ComboBox
        countryDropdown.setItems(countryList);
        countryDropdown.setPromptText("Country location...");
    }

    /** Gets the value from the 'countryDropdown' and populates the 'DivisionDropdown' accordingly.
     * @param actionEvent Select choice from the countryComboBox
     */
    public void countryDropdownListener(ActionEvent actionEvent) {
        // Get selected value in the ComboBox
        Country countryDropdownInput = countryDropdown.getValue();
        // Get ID of Country
        int countryComboBoxInt = countryDropdownInput.getCountryId();
        // Populate the divisionComboBox with the corresponding territories
        filDivisionDropdown(countryComboBoxInt);
    }

    /** Populates the divisionComboBox based on the Country ID number of the selected choice in the countryComboBox
     * @param divisionInput The divisionInput of the Country ID
     */
    public void filDivisionDropdown(int divisionInput) {
        ObservableList<Division> divList = FXCollections.observableArrayList();

        // Based on ID number of the Country, obtain the required divisions from the database
        switch (divisionInput) {
            case 1:
                divList = databaseDivisions.getUSADivisions();
                System.out.println(divList);
                break;
            case 2:
                divList = databaseDivisions.getCanadaDivisions();
                System.out.println(divList);

                break;
            case 3:
                divList = databaseDivisions.getUKDivisions();
                System.out.println(divList);

                break;
            default:
                System.out.println("null");
                break;
        }

        // Populate the ComboBox
        divisionDropdown.setItems(divList);
        divisionDropdown.setPromptText("Choose Division...");

    }

    /** Closes out 'Add Customer' window
     * @param actionEvent Click of 'Cancel' button
     */
    public void cancelButtonListener(ActionEvent actionEvent) {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }

    /** Saves the information from the TextFields into the database as a new customer.
     * @param actionEvent Click of the 'Save' button.
     * @throws SQLException if SQL is misshapen.
     */
    public void saveButtonListener(ActionEvent actionEvent) throws SQLException {

        System.out.println("Save button clicked.");

        // Get values within the text fields
        StringProperty customerName = new SimpleStringProperty(customerNameTextField.getText());
        StringProperty customerAddress = new SimpleStringProperty(addressTextField.getText());
        StringProperty customerZipCode = new SimpleStringProperty(zipCodeTextField.getText());
        StringProperty customerPhoneNumber = new SimpleStringProperty(phoneNumberTextField.getText());
        StringProperty customerCountry = new SimpleStringProperty(countryDropdown.getValue().toString());
        StringProperty customerDivision = new SimpleStringProperty(divisionDropdown.getValue().toString());

        // Create IntegerProperties using the rest of the values
        IntegerProperty divisionIdProperty = new SimpleIntegerProperty(Integer.parseInt(databaseCustomerRecords.searchForDivisionId(customerDivision.getValue())));

        IntegerProperty countryIdProperty = new SimpleIntegerProperty(databaseCustomerRecords.searchForCountryId(divisionIdProperty));

        System.out.println("Division: " + customerDivision);
        System.out.println("Division ID String: " + divisionIdProperty.getValue());

        IntegerProperty currentIdProperty = new SimpleIntegerProperty(currId);

        System.out.println(customerName + " " + customerAddress + " " + customerZipCode + " " + customerPhoneNumber +
                " " + customerCountry + " " + customerDivision);


        // Assign the connection to a variable
        Connection connection = JDBC.getConnection();

        // SQL statement for inserting a new Customer into the database
        String sqlInsertQuery = "INSERT INTO client_schedule.customers(Customer_Name, Address, Postal_Code," +
                        "Phone, Division_ID) VALUES (?,?,?,?,?)";

        // Prepare the SQL statement and inserts the parameters into their respective indexes
        databaseQuery.setPreparedStatement(connection, sqlInsertQuery);
        PreparedStatement preparedStatement = databaseQuery.getPreparedStatement();

        // Set the Strings of the Insert Statement
        preparedStatement.setString(1, customerName.getValue());
        preparedStatement.setString(2, customerAddress.getValue());
        preparedStatement.setString(3, customerZipCode.getValue());
        preparedStatement.setString(4, customerPhoneNumber.getValue());
        preparedStatement.setString(5, String.valueOf(divisionIdProperty.getValue()));
        preparedStatement.execute();

        // Add the new customer to the ObservableList
        customerRecords.add(new Customer(currentIdProperty, customerName, customerAddress, customerZipCode,
                customerDivision, customerCountry, customerPhoneNumber, divisionIdProperty, countryIdProperty));

        // Close the window
        Stage stage = (Stage)saveButton.getScene().getWindow();
        stage.close();
    }
}