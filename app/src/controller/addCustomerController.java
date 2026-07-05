package controller;

import database.DatabaseCountries;
import database.DatabaseCustomerRecords;
import database.DatabaseDivisions;
import database.DatabaseQuery;
import database.JDBC;
import model.Country;
import model.Customer;
import model.Division;
import static controller.ViewCustomersController.customerRecords;
import static main.Main.currId;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class AddCustomerController implements Initializable {
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        randIdGenerator();
        fillCountryDropdown();
    }

    public void randIdGenerator() {
        currId += 1;
        customerIDTextField.setText(String.valueOf(currId));
    }

    public void fillCountryDropdown() {
        ObservableList<Country> countryList = DatabaseCountries.getAllCountries();

        countryDropdown.setItems(countryList);
        countryDropdown.setPromptText("Country location...");
    }

    public void countryDropdownListener(ActionEvent actionEvent) {
        Country countryDropdownInput = countryDropdown.getValue();
        int countryComboBoxInt = countryDropdownInput.getCountryId();
        filDivisionDropdown(countryComboBoxInt);
    }

    public void filDivisionDropdown(int divisionInput) {
        ObservableList<Division> divList = FXCollections.observableArrayList();

        switch (divisionInput) {
            case 1:
                divList = DatabaseDivisions.getUSADivisions();
                break;
            case 2:
                divList = DatabaseDivisions.getCanadaDivisions();
                break;
            case 3:
                divList = DatabaseDivisions.getUKDivisions();
                break;
            default:
                break;
        }

        divisionDropdown.setItems(divList);
        divisionDropdown.setPromptText("Choose Division...");

    }

    public void cancelButtonListener(ActionEvent actionEvent) {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }

    public void saveButtonListener(ActionEvent actionEvent) throws SQLException {

        StringProperty customerName = new SimpleStringProperty(customerNameTextField.getText());
        StringProperty customerAddress = new SimpleStringProperty(addressTextField.getText());
        StringProperty customerZipCode = new SimpleStringProperty(zipCodeTextField.getText());
        StringProperty customerPhoneNumber = new SimpleStringProperty(phoneNumberTextField.getText());
        StringProperty customerCountry = new SimpleStringProperty(countryDropdown.getValue().toString());
        StringProperty customerDivision = new SimpleStringProperty(divisionDropdown.getValue().toString());

        IntegerProperty divisionIdProperty = new SimpleIntegerProperty(Integer.parseInt(DatabaseCustomerRecords.searchForDivisionId(customerDivision.getValue())));

        IntegerProperty countryIdProperty = new SimpleIntegerProperty(DatabaseCustomerRecords.searchForCountryId(divisionIdProperty));

        IntegerProperty currentIdProperty = new SimpleIntegerProperty(currId);

        Connection connection = JDBC.getConnection();

        String sqlInsertQuery = "INSERT INTO client_schedule.customers(Customer_Name, Address, Postal_Code," +
                        "Phone, Division_ID) VALUES (?,?,?,?,?)";

        DatabaseQuery.setPreparedStatement(connection, sqlInsertQuery);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, customerName.getValue());
        preparedStatement.setString(2, customerAddress.getValue());
        preparedStatement.setString(3, customerZipCode.getValue());
        preparedStatement.setString(4, customerPhoneNumber.getValue());
        preparedStatement.setString(5, String.valueOf(divisionIdProperty.getValue()));
        preparedStatement.execute();

        customerRecords.add(new Customer(currentIdProperty, customerName, customerAddress, customerZipCode,
                customerDivision, customerCountry, customerPhoneNumber, divisionIdProperty, countryIdProperty));

        Stage stage = (Stage)saveButton.getScene().getWindow();
        stage.close();
    }
}