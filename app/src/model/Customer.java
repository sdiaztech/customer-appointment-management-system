package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    private IntegerProperty customerIdProperty;
    private StringProperty customerNameProperty;
    private StringProperty addressProperty;
    private StringProperty postalCodeProperty;
    private StringProperty phoneNumberProperty;
    private IntegerProperty divisionIdProperty;
    private StringProperty divisionProperty;
    private IntegerProperty countryIdProperty;
    private StringProperty countryProperty;

    public Customer(IntegerProperty customerIdProperty,
                    StringProperty customerNameProperty,
                    StringProperty addressProperty,
                    StringProperty postalCodeProperty,
                    StringProperty divisionProperty,
                    StringProperty countryProperty,
                    StringProperty phoneNumberProperty,
                    IntegerProperty divisionIdProperty,
                    IntegerProperty countryIdProperty) {
        this.customerIdProperty = customerIdProperty;
        this.customerNameProperty = customerNameProperty;
        this.addressProperty = addressProperty;
        this.postalCodeProperty = postalCodeProperty;
        this.phoneNumberProperty = phoneNumberProperty;
        this.divisionProperty = divisionProperty;
        this.countryProperty = countryProperty;
        this.divisionIdProperty = divisionIdProperty;
        this.countryIdProperty = countryIdProperty;
    }

    public int getCustomerId() {
        return customerIdProperty().get();
    }

    public IntegerProperty customerIdProperty() {
        if (customerIdProperty == null) {
            customerIdProperty = new SimpleIntegerProperty(this, "customerId");
        }
        return customerIdProperty;
    }

    public String getCustomerName() {
        return customerNameProperty().get();
    }

    public StringProperty customerNameProperty() {
        if (customerNameProperty == null) {
            customerNameProperty = new SimpleStringProperty(this, "customerName");
        }
        return customerNameProperty;
    }

    public String getAddress() {
        return addressProperty().get();
    }

    public StringProperty addressProperty() {
        if (addressProperty == null) {
            addressProperty = new SimpleStringProperty(this, "address");
        }
        return addressProperty;
    }

    public String getPostalCode() {
        return postalCodeProperty().get();
    }

    public StringProperty postalCodeProperty() {
        if (postalCodeProperty == null) {
            postalCodeProperty = new SimpleStringProperty(this, "postalCode");
        }
        return postalCodeProperty;
    }

    public String getPhoneNumber() {
        return phoneNumberProperty().get();
    }

    public StringProperty phoneNumberProperty() {
        if (phoneNumberProperty == null) {
            phoneNumberProperty = new SimpleStringProperty(this, "phoneNumber");
        }
        return phoneNumberProperty;
    }

    public int getDivisionId() {
        return divisionIdProperty().get();
    }

    public IntegerProperty divisionIdProperty() {
        if (divisionIdProperty == null) {
            divisionIdProperty = new SimpleIntegerProperty(this, "divisionId");
        }
        return divisionIdProperty;
    }

    public String getDivision() {
        return divisionProperty().get();
    }

    public StringProperty divisionProperty() {
        if (divisionProperty == null) {
            divisionProperty = new SimpleStringProperty(this, "division");
        }
        return divisionProperty;
    }

    public int getCountryId() {
        return countryIdProperty().get();
    }

    public IntegerProperty countryIdProperty() {
        if (countryIdProperty == null) {
            countryIdProperty = new SimpleIntegerProperty(this, "countryId");
        }
        return countryIdProperty;
    }

    public String getCountry() {
        return countryProperty().get();
    }

    public StringProperty countryProperty() {
        if (countryProperty == null) {
            countryProperty = new SimpleStringProperty(this, "country");
        }
        return countryProperty;
    }

}
