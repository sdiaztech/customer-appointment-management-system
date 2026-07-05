package model;

import javafx.beans.property.*;

/** This class is an object constructor used to create and get Customer objects */
public class Customer {
    private IntegerProperty customerIdProperty;
    private StringProperty customerNameProperty;
    private StringProperty addressProperty;
    private StringProperty postalCodeProperty;
    private StringProperty phoneNumberProperty;
    private StringProperty createDateProperty;
    private StringProperty authorProperty;
    private StringProperty lastUpdateProperty;
    private StringProperty lastUpdatedByProperty;
    private IntegerProperty divisionIdProperty;
    private StringProperty  divisionProperty;
    private IntegerProperty countryIdProperty;
    private StringProperty  countryProperty;


    /** Constructor that takes in properties to be shown in a TableView
     * @param customerIdProperty
     * @param customerNameProperty
     * @param addressProperty
     * @param postalCodeProperty
     * @param phoneNumberProperty
     * @param createDateProperty
     * @param authorProperty
     * @param lastUpdateProperty
     * @param lastUpdatedByProperty
     * @param divisionIdProperty
     * @param divisionProperty
     * @param countryIdProperty
     * @param countryProperty
     */
    public Customer(IntegerProperty customerIdProperty,
                    StringProperty customerNameProperty,
                    StringProperty addressProperty,
                    StringProperty postalCodeProperty,
                    StringProperty phoneNumberProperty,
                    StringProperty createDateProperty,
                    StringProperty authorProperty,
                    StringProperty lastUpdateProperty,
                    StringProperty lastUpdatedByProperty,
                    IntegerProperty divisionIdProperty,
                    StringProperty divisionProperty,
                    IntegerProperty countryIdProperty,
                    StringProperty countryProperty) {
        this.customerIdProperty = customerIdProperty;
        this.customerNameProperty = customerNameProperty;
        this.addressProperty = addressProperty;
        this.postalCodeProperty = postalCodeProperty;
        this.phoneNumberProperty = phoneNumberProperty;
        this.createDateProperty = createDateProperty;
        this.authorProperty = authorProperty;
        this.lastUpdateProperty = lastUpdateProperty;
        this.lastUpdatedByProperty = lastUpdatedByProperty;
        this.divisionIdProperty = divisionIdProperty;
        this.divisionProperty = divisionProperty;
        this.countryIdProperty = countryIdProperty;
        this.countryProperty = countryProperty;
    }

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

    /** Getter gets Customer ID
     * @return 'customerId'
     */
    public int getCustomerId() {
        return customerIdProperty().get();
    }

    /** Getter gets Customer ID as a property type
     * @return 'customerId' as a property
     */
    public IntegerProperty customerIdProperty() {
        if (customerIdProperty == null) {
            customerIdProperty = new SimpleIntegerProperty(this, "customerId");
        }
        return customerIdProperty;
    }

    /** Getter gets customer's name
     * @return 'customerName'
     */
    public String getCustomerName() {
        return customerNameProperty().get();
    }

    /** Getter gets customer's name as a property type
     * @return 'customerName' as a property
     */
    public StringProperty customerNameProperty() {
        if (customerNameProperty == null) {
            customerNameProperty = new SimpleStringProperty(this, "customerName");
        }
        return customerNameProperty;
    }

    /** Getter gets customer's address
     * @return 'address'
     */
    public String getAddress() {
        return addressProperty().get();
    }

    /** Getter gets customer's address as a property type
     * @return 'address' as a property
     */
    public StringProperty addressProperty() {
        if (addressProperty == null) {
            addressProperty = new SimpleStringProperty(this, "address");
        }
        return addressProperty;
    }

    /** Getter gets postal code as a property type
     * @return 'postalCode' as a property
     */
    public String getPostalCode() {
        return postalCodeProperty().get();
    }

    /** Getter gets postal code as a property type
     * @return 'postalCode' as a property
     */
    public StringProperty postalCodeProperty() {
        if (postalCodeProperty == null) {
            postalCodeProperty = new SimpleStringProperty(this, "postalCode");
        }
        return postalCodeProperty;
    }

    /** Getter gets customer's phone number
     * @return 'phoneNumber'
     */
    public String getPhoneNumber() {
        return phoneNumberProperty().get();
    }

    /** Getter gets customer's phone number as a property type
     * @return 'phoneNumber' as a property
     */
    public StringProperty phoneNumberProperty() {
        if (phoneNumberProperty == null) {
            phoneNumberProperty = new SimpleStringProperty(this, "phoneNumber") ;
        }
        return phoneNumberProperty;
    }

    /** Getter gets division ID
     * @return 'divisionId'
     */
    public int getDivisionId() {
        return divisionIdProperty().get();
    }

    /** Getter gets Division ID as a property type
     * @return 'divisionId' as a property
     */
    public IntegerProperty divisionIdProperty() {
        if (divisionIdProperty == null) {
            divisionIdProperty = new SimpleIntegerProperty(this, "divisionId");
        }
        return divisionIdProperty;
    }

    /** Getter gets division name
     * @return 'division'
     */
    public String getDivision() {
        return divisionProperty().get();
    }

    /** Getter gets division name as a property type
     * @return 'division' as a property
     */
    public StringProperty divisionProperty() {
        if (divisionProperty == null) {
            divisionProperty = new SimpleStringProperty(this, "division");
        }
        return divisionProperty;
    }

    /** Getter gets countryID as a property type
     * @return 'countryId' as a property type
     */
    public int getCountryId() {
        return countryIdProperty().get();
    }

    /** Getter gets Country ID as a property type
     * @return 'countryId' as a property
     */
    public IntegerProperty countryIdProperty() {
        if (countryIdProperty == null) {
            countryIdProperty = new SimpleIntegerProperty(this, "countryId");
        }
        return countryIdProperty;
    }

    /** Getter gets country name as a property type
     * @return 'country' as a property type
     */
    public String getCountry() {
        return countryProperty().get();
    }

    /** Getter gets country name as a property type
     * @return 'country' as a property
     */
    public StringProperty countryProperty() {
        if (countryProperty == null) {
            countryProperty = new SimpleStringProperty(this, "country");
        }
        return countryProperty;
    }

}