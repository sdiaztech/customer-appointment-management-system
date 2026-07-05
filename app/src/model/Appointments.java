package model;

import javafx.beans.property.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**This class is an object constructor used to create and get Appointment objects*/
public class Appointments {
    private IntegerProperty appointmentIdProperty;
    private StringProperty  titleProperty;
    private StringProperty  descriptionProperty;
    private StringProperty  locationProperty;
    private StringProperty  typeProperty;
    private StringProperty startTimeProperty;
    private IntegerProperty userIdProperty;
    private StringProperty endTimeProperty;
    private IntegerProperty customerIdProperty;
    private IntegerProperty contactIdProperty;


    public Appointments(IntegerProperty appointmentIdProperty,
                        StringProperty titleProperty,
                        StringProperty descriptionProperty,
                        StringProperty locationProperty,
                        StringProperty typeProperty,
                        StringProperty startTimeProperty,
                        StringProperty endTimeProperty,
                        IntegerProperty userIdProperty,
                        IntegerProperty customerIdProperty,
                        IntegerProperty contactIdProperty) {
        this.appointmentIdProperty = appointmentIdProperty;
        this.titleProperty = titleProperty;
        this.descriptionProperty = descriptionProperty;
        this.locationProperty = locationProperty;
        this.typeProperty = typeProperty;
        this.startTimeProperty = startTimeProperty;
        this.userIdProperty  = userIdProperty;
        this.endTimeProperty = endTimeProperty;
        this.customerIdProperty = customerIdProperty;
        this.contactIdProperty = contactIdProperty;
    }


    /** Getter gets the 'appointmentId' as an 'int'
     * @return 'Appointment' id number
     */
    public int getApptId() {
        return apptIdProperty().get();
    }

    /** Getter gets the 'appointmentId' as a 'SimpleIntegerProperty'
     * @return 'Appointment' 'ID' number as a property
     */
    public IntegerProperty apptIdProperty() {
        if (appointmentIdProperty == null) {
            appointmentIdProperty = new SimpleIntegerProperty(this, "appointmentId");
        }
        return appointmentIdProperty;
    }

    /**  Getter gets the appointment title
     * @return 'Appointment' 'title as a 'String'
     */
    public String getTitle() {
        return titleProperty().get();
    }

    /** Getter gets the 'title' of the Appointment as a 'SimpleIntegerProperty'
     * @return 'title' of the 'Appointment' as a property
     */
    public StringProperty titleProperty() {
        if (titleProperty == null) {
            titleProperty = new SimpleStringProperty(this, "title");
        }
        return titleProperty;
    }

    /** Getter gets the 'description' of the 'Appointment'
     * @return 'description' of the 'Appointment' as a String
     */
    public String getDescription() {
        return descriptionProperty().get();
    }

    /** Getter gets 'descriptionProperty' as a 'SimpleStringProperty'
     * @return 'description' of the 'Appointment' as a property
     */
    public StringProperty descriptionProperty() {
        if (descriptionProperty == null) {
            descriptionProperty = new SimpleStringProperty(this, "description");
        }
        return descriptionProperty;
    }

    /**  Getter gets the appointment's location
     * @return 'location' of the 'Appointment'
     */
    public String getLocation() {
        return locationProperty().get();
    }

    /** Getter gets the appointment's location as a 'SimpleStringProperty'
     * @return 'location' of the 'Appointment' as a property
     */
    public StringProperty locationProperty() {
        if (locationProperty == null) {
            locationProperty = new SimpleStringProperty(this, "location");
        }
        return locationProperty;
    }

    /** Getter gets the appointment's type as String
     * @return 'type' of the 'Appointment' as a String
     */
    public String getType() {
        return typeProperty().get();
    }

    /** Getter gets the appointment's type as a property
     * @return 'type' of the 'Appointment' as a 'SimpleStringProperty'
     */
    public StringProperty typeProperty() {
        if (typeProperty == null) {
            typeProperty = new SimpleStringProperty(this, "type");
        }
        return typeProperty;
    }



    /** Getter gets the starting month of the appointment
     * @return month 'int' equivalent of the month of the 'Appointment' start date(e.g. January = 0)
     */
    public int getStartMonthInt() {
        // Get the startProperty
        String startTimestampString = startTimeProperty().get();
        // Create a DateFormat to be parsed
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

        int month = 0;

        try {
            // Create a new Date Object from the Timestamp String using the dateFormat
            Date parsedDate = dateFormat.parse(startTimestampString);
            // Get an instance of the Calendar object
            Calendar calendar = Calendar.getInstance();
            // Set the time of the Calendar object to the Date
            calendar.setTime(parsedDate);
            // Get the int equivalent of the month
            month = calendar.get(Calendar.MONTH);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Month: " + month);
        return month;
    }

    /** Getter gets the starting week of the appointment as an int
     * @return week number of a scheduled 'Appointment' as an int value
     */
    public int getStartWeek()
    {
        // Get the start property
        String startTimestampString = startTimeProperty().get();
        // Create a dateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

        int week = 0;

        try
        {
            // Create a new Date Object from the Timestamp String using the dateFormat
            Date parsedDate = dateFormat.parse(startTimestampString);
            // Get an instance of the Calendar Object
            Calendar calendar = Calendar.getInstance();
            // Set the time of the Calendar object to the Date
            calendar.setTime(parsedDate);
            // Get the week number of the Date set on the Calendar object
            week = calendar.get(Calendar.WEEK_OF_YEAR);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Week: " + week);
        return week;
    }


    /** Getter gets the start time of the Appointment as a String
     * @return 'start' time of the 'Appointment'
     */
    public String getStartTime() {
        return startTimeProperty().get();
    }

    /** Getter gets the start time as a property
     * @return 'start' time as a 'SimpleStringProperty' of the 'Appointment'
     */
    public StringProperty startTimeProperty() {
        if (startTimeProperty == null) {
            startTimeProperty = new SimpleStringProperty(this, "start");
        }
        return startTimeProperty;
    }

    /** Getter gets the end time of the appointment as a string value
     * @return 'end' time of the 'Appointment' as a 'String'
     */
    public String getEndTime() {
        return endTimeProperty().get();
    }

    /** Getter gets the time of an appointment as a property
     * @return 'end' time as a 'SimpleStringProperty' of the 'Appointment'
     */
    public StringProperty endTimeProperty() {
        if (endTimeProperty == null) {
            endTimeProperty = new SimpleStringProperty(this, "end");
        }
        return endTimeProperty;
    }

    /** Getter gets the customer ID associated with an appointment as an int value
     * @return 'customerId' of the 'Appointment' as an int value
     */
    public int getCustomerId() {
        return customerIdProperty().get();
    }

    /** Getter gets the customer ID associated with an appointment as a property
     * @return 'customerId'  as a 'SimpleIntegerProperty' of the 'Appointment'
     */
    public IntegerProperty customerIdProperty() {
        if (customerIdProperty == null) {
            customerIdProperty = new SimpleIntegerProperty(this, "customerId");
        }
        return customerIdProperty;
    }

    /** Getter gets the user ID of the scheduler as a String value
     * @return 'userId' of the person that scheduled the 'Appointment'
     */
    public int getUserId() {
        return userIdProperty().get();
    }

    /** Getter gets the user ID of the scheduler as a property
     * @return 'userId' as a 'SimpleIntegerProperty' of the person that scheduled the 'Appointment'
     */
    public IntegerProperty userIdProperty() {
        if (userIdProperty == null) {
            userIdProperty = new SimpleIntegerProperty(this, "userId");
        }
        return userIdProperty;
    }

    /** Getter gets the contact ID of the employee as an int value
     * @return 'contactId' of the employee assigned to the 'Appointment'
     */
    public int getContactId()  {
        return contactIdProperty().get();
    }

    /** Getter gets the contact ID of the employee as a property value
     * @return 'contactId' as a 'SimpleIntegerProperty' of the employee assigned to the 'Appointment'
     */
    public IntegerProperty contactIdProperty() {
        if (contactIdProperty == null) {
            contactIdProperty = new SimpleIntegerProperty(this, "contactId");
        }
        return contactIdProperty;
    }
}