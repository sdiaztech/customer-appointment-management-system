package model;

/** This class is an object constructor used to create and get Contact objects */
public class Contact {
    private int contactId;
    private String contactName;
    private String contactEmail;

    /** Employee assigned to contact the 'Customer'
     * @param contactId - employee's identification number
     * @param contactName - employee's name
     * @param contactEmail - employee's email address
     */
    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /** Getter gets the 'contactId'
     * @return employee's identification number as an Int value
     */
    public int getContactId() {
        return contactId;
    }

    /** Getter gets the 'contactName'
     * @return employee's name as a String value
     */
    public String getContactName() {
        return contactName;
    }

    /** Getter gets the 'emailAddress'
     * @return employee's email address as a String value
     */
    public String getContactEmail() {
        return contactEmail;
    }
}