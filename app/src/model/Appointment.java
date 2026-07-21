package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Appointment {
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

    public Appointment(IntegerProperty appointmentIdProperty,
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

    public int getApptId() {
        return apptIdProperty().get();
    }

    public IntegerProperty apptIdProperty() {
        if (appointmentIdProperty == null) {
            appointmentIdProperty = new SimpleIntegerProperty(this, "appointmentId");
        }
        return appointmentIdProperty;
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public StringProperty titleProperty() {
        if (titleProperty == null) {
            titleProperty = new SimpleStringProperty(this, "title");
        }
        return titleProperty;
    }

    public String getDescription() {
        return descriptionProperty().get();
    }

    public StringProperty descriptionProperty() {
        if (descriptionProperty == null) {
            descriptionProperty = new SimpleStringProperty(this, "description");
        }
        return descriptionProperty;
    }

    public String getLocation() {
        return locationProperty().get();
    }

    public StringProperty locationProperty() {
        if (locationProperty == null) {
            locationProperty = new SimpleStringProperty(this, "location");
        }
        return locationProperty;
    }

    public String getType() {
        return typeProperty().get();
    }

    public StringProperty typeProperty() {
        if (typeProperty == null) {
            typeProperty = new SimpleStringProperty(this, "type");
        }
        return typeProperty;
    }

    public int getStartMonthInt() {
        return getStartDateTime().getMonthValue() - 1;
    }

    public int getStartWeek()
    {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return getStartDateTime().get(weekFields.weekOfWeekBasedYear());
    }

    private LocalDateTime getStartDateTime() {
        return Timestamp.valueOf(startTimeProperty().get()).toLocalDateTime();
    }

    public String getStartTime() {
        return startTimeProperty().get();
    }


    public StringProperty startTimeProperty() {
        if (startTimeProperty == null) {
            startTimeProperty = new SimpleStringProperty(this, "start");
        }
        return startTimeProperty;
    }

    public String getEndTime() {
        return endTimeProperty().get();
    }

    public StringProperty endTimeProperty() {
        if (endTimeProperty == null) {
            endTimeProperty = new SimpleStringProperty(this, "end");
        }
        return endTimeProperty;
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

    public int getUserId() {
        return userIdProperty().get();
    }

    public IntegerProperty userIdProperty() {
        if (userIdProperty == null) {
            userIdProperty = new SimpleIntegerProperty(this, "userId");
        }
        return userIdProperty;
    }

    public int getContactId()  {
        return contactIdProperty().get();
    }

    public IntegerProperty contactIdProperty() {
        if (contactIdProperty == null) {
            contactIdProperty = new SimpleIntegerProperty(this, "contactId");
        }
        return contactIdProperty;
    }
}
