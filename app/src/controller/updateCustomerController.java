package controller;

import database.*;
import model.Country;
import model.Customer;
import model.Division;
import main.Main;

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

import static controller.viewCustomersController.customerRecords;
import static database.databaseCustomerRecords.getAllCustomerRecords;
import static main.Main.chosenCustomer;

/** Controller for the Update Customer 'Scene' */
public class updateCustomerController implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button updateButton;
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

    private int countryComboInt = 0;


    /** Initializes the 'Update Customer Scene'
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Update customer initialized");
        fillCountryDropdown();
        if (chosenCustomer != null) {
            System.out.println(chosenCustomer);
            retrievePopulateCustomer();
        }
    }

    /** Populates the 'ComboBox' that contains the choices for 'Country' */
    public void fillCountryDropdown() {
        // Get the list of countries from the database
        ObservableList<Country> countryList = databaseCountries.getAllCountries();
        System.out.println(countryList.toString());
        System.out.println(countryList);

        // Populate the ComboBox
        countryDropdown.setItems(countryList);
        countryDropdown.setPromptText("Choose a Country...");
        divisionDropdown.setPromptText("Choose a Division...");

    }

    /** Retrieves the selected 'Customer'(s) data from the database and populates the Update Customer Form */
    public void retrievePopulateCustomer() {
        Customer chosenCustomerToUpdate = Main.chosenCustomer;
        if (chosenCustomerToUpdate != null) {
            customerIDTextField.setText(String.valueOf(chosenCustomerToUpdate.getCustomerId()));
            customerNameTextField.setText(String.valueOf(chosenCustomerToUpdate.getCustomerName()));
            addressTextField.setText(String.valueOf(chosenCustomerToUpdate.getAddress()));
            zipCodeTextField.setText(String.valueOf(chosenCustomerToUpdate.getPostalCode()));
            phoneNumberTextField.setText(String.valueOf(chosenCustomerToUpdate.getPhoneNumber()));

            // Get the countryId of the selected Customer
            int chosenCustomerCountryId = chosenCustomerToUpdate.getCountryId();
            for (Country country : countryDropdown.getItems()) {
                System.out.println(countryDropdown.getItems());
                System.out.println("Country: " + country);
                if (chosenCustomerCountryId == country.getCountryId()) {
                    countryDropdown.setValue(country);
                    countryComboInt = chosenCustomerCountryId;
                    break;
                }
            }
            populateDivisionComboBox(countryComboInt);

            // Get the divisionId of the selected Customer
            int chosenCustomerDivisionId = chosenCustomerToUpdate.getDivisionId();
            System.out.println("Division Id: " + chosenCustomerDivisionId);

            for (Division division : divisionDropdown.getItems()) {

                System.out.println("Division: " + division);
                if (chosenCustomerDivisionId == division.getDivisionId()) {
                    divisionDropdown.setValue(division);
                    break;
                }
            }

        }
    }

    /** Populates the divisionComboBox based on the Country ID number of the selected choice in the countryComboBox
     * @param countryComboInt The integer that is associated with the 'Country' selected in the Country ComboBox
     */
    public void populateDivisionComboBox(int countryComboInt) {
        ObservableList<Division> divisionsList = FXCollections.observableArrayList();

        // Based the ID number of the Country selected in the first ComboBox, obtain the required divisions from the database
        switch (countryComboInt) {
            case 1:
                divisionsList = databaseDivisions.getUSADivisions();
                System.out.println(divisionsList);
                break;
            case 2:
                divisionsList = databaseDivisions.getCanadaDivisions();
                System.out.println(divisionsList);

                break;
            case 3:
                divisionsList = databaseDivisions.getUKDivisions();
                System.out.println(divisionsList);
                break;
            default:
                System.out.println("null");
                break;
        }

        // Populate the ComboBox
        System.out.println(divisionsList);
        divisionDropdown.setItems(divisionsList);
    }

    /** Gets the value from the countryComboBox and populates the DivisionComboBox accordingly.
     * @param actionEvent User selects a choice from the countryComboBox
     */
    public void countryComboBoxListener(ActionEvent actionEvent) {
        // Get the selected value in the ComboBox
        Country countryComboValue = countryDropdown.getValue();
        // Get Id
        int countryComboValueInt = countryComboValue.getCountryId();
        // Populate the divisionComboBox with the corresponding territories
        populateDivisionComboBox(countryComboValueInt);
    }


    /** Closes out the 'Update Customer' window
     * @param actionEvent User click on the 'Cancel' button
     */
    public void cancelButtonListener(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /** Saves the information from the Text Fields into the database as a new customer.
     * @param actionEvent Click of the 'Save' button
     * @throws SQLException if SQL is malformed.
     */
    public void updateButtonListener(ActionEvent actionEvent) throws SQLException {
        System.out.println("Update Button Clicked.");

        try {
            // Make the connection
            Connection connection = JDBC.getConnection();
            // Update SQL statement
            String sqlUpdateQuery =
                    "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                    "Phone = ?, Division_ID= ? WHERE Customer_ID= ?";
            // Prepare the statement
            databaseQuery.setPreparedStatement(connection, sqlUpdateQuery);
            PreparedStatement preparedStatement = databaseQuery.getPreparedStatement();

            // Set the string values in the statement
            preparedStatement.setString(1, customerNameTextField.getText());
            preparedStatement.setString(2, addressTextField.getText());
            preparedStatement.setString(3, zipCodeTextField.getText());
            preparedStatement.setString(4, phoneNumberTextField.getText());

            // Get the value from the divisionComboBox and take only the DivisionID
            String divisionComboBoxValue = divisionDropdown.getValue().toString();
            String divisionIdString = divisionComboBoxValue.substring(0, divisionComboBoxValue.indexOf(" ", 0));
            preparedStatement.setString(5, divisionIdString);
            preparedStatement.setString(6, customerIDTextField.getText());
            preparedStatement.execute();

            System.out.println("Customer Name: " + customerNameTextField.getText());
            System.out.println("Address: " + addressTextField.getText());
            System.out.println("Postal Code" + zipCodeTextField.getText());
            System.out.println(customerRecords.indexOf(customerIDTextField.getText()));
            System.out.println("DivisionIdString" + divisionIdString);

            // Create a new ObservableList containing the updated record
            ObservableList<Customer> newCustomerList = FXCollections.observableArrayList(getAllCustomerRecords());

            // Update the ObservableList to update the table
            customerRecords.setAll(newCustomerList);
            System.out.println("Successfully updated!");

            // Close the window
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}