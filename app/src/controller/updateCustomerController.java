package controller;

import database.DatabaseCountries;
import database.DatabaseDivisions;
import database.DatabaseQuery;
import database.JDBC;
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

import static controller.ViewCustomersController.customerRecords;
import static database.DatabaseCustomerRecords.getAllCustomerRecords;
import static main.Main.chosenCustomer;

public class UpdateCustomerController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillCountryDropdown();
        if (chosenCustomer != null) {
            retrievePopulateCustomer();
        }
    }

    public void fillCountryDropdown() {
        ObservableList<Country> countryList = DatabaseCountries.getAllCountries();
        countryDropdown.setItems(countryList);
        countryDropdown.setPromptText("Choose a Country...");
        divisionDropdown.setPromptText("Choose a Division...");
    }

    public void retrievePopulateCustomer() {
        Customer chosenCustomerToUpdate = Main.chosenCustomer;
        if (chosenCustomerToUpdate != null) {
            customerIDTextField.setText(String.valueOf(chosenCustomerToUpdate.getCustomerId()));
            customerNameTextField.setText(String.valueOf(chosenCustomerToUpdate.getCustomerName()));
            addressTextField.setText(String.valueOf(chosenCustomerToUpdate.getAddress()));
            zipCodeTextField.setText(String.valueOf(chosenCustomerToUpdate.getPostalCode()));
            phoneNumberTextField.setText(String.valueOf(chosenCustomerToUpdate.getPhoneNumber()));

            int chosenCustomerCountryId = chosenCustomerToUpdate.getCountryId();
            for (Country country : countryDropdown.getItems()) {
                if (chosenCustomerCountryId == country.getCountryId()) {
                    countryDropdown.setValue(country);
                    countryComboInt = chosenCustomerCountryId;
                    break;
                }
            }
            populateDivisionComboBox(countryComboInt);

            int chosenCustomerDivisionId = chosenCustomerToUpdate.getDivisionId();

            for (Division division : divisionDropdown.getItems()) {
                if (chosenCustomerDivisionId == division.getDivisionId()) {
                    divisionDropdown.setValue(division);
                    break;
                }
            }
        }
    }

    public void populateDivisionComboBox(int countryComboInt) {
        ObservableList<Division> divisionsList = FXCollections.observableArrayList();

        switch (countryComboInt) {
            case 1:
                divisionsList = DatabaseDivisions.getUSADivisions();
                break;
            case 2:
                divisionsList = DatabaseDivisions.getCanadaDivisions();
                break;
            case 3:
                divisionsList = DatabaseDivisions.getUKDivisions();
                break;
            default:
                break;
        }

        divisionDropdown.setItems(divisionsList);
    }

    public void countryComboBoxListener(ActionEvent actionEvent) {
        Country countryComboValue = countryDropdown.getValue();
        int countryComboValueInt = countryComboValue.getCountryId();
        populateDivisionComboBox(countryComboValueInt);
    }

    public void cancelButtonListener(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void updateButtonListener(ActionEvent actionEvent) throws SQLException {
        try {
            Connection connection = JDBC.getConnection();
            String sqlUpdateQuery =
                    "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                    "Phone = ?, Division_ID= ? WHERE Customer_ID= ?";
            DatabaseQuery.setPreparedStatement(connection, sqlUpdateQuery);
            PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

            preparedStatement.setString(1, customerNameTextField.getText());
            preparedStatement.setString(2, addressTextField.getText());
            preparedStatement.setString(3, zipCodeTextField.getText());
            preparedStatement.setString(4, phoneNumberTextField.getText());

            String divisionComboBoxValue = divisionDropdown.getValue().toString();
            String divisionIdString = divisionComboBoxValue.substring(0, divisionComboBoxValue.indexOf(" ", 0));
            preparedStatement.setString(5, divisionIdString);
            preparedStatement.setString(6, customerIDTextField.getText());
            preparedStatement.execute();

            ObservableList<Customer> newCustomerList = FXCollections.observableArrayList(getAllCustomerRecords());

            customerRecords.setAll(newCustomerList);

            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
