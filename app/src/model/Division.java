package model;

/**This class is an object constructor used to create and get Division objects*/
public class Division {
    private int divisionId;
    private String division;
    private String date;
    private String author;
    private String lastModified;
    private String lastModifiedBy;
    private int countryId;

    /** @param divisionId Int value - Division ID number
     * @param division String value - the name of the 'Division'
     * @param date String value - 'date' the entry was created
     * @param author String value - 'User' that created the 'Division'
     * @param lastModified String value - last time the 'Division' was updated
     * @param lastModifiedBy String value - last user that updated the 'Division'
     * @param countryId String value - ID number of the 'Country' that the 'Division' belongs to
     */
    public Division(int divisionId, String division, String date, String author, String lastModified,
                    String lastModifiedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.date = date;
        this.author = author;
        this.lastModified = lastModified;
        this.lastModifiedBy = lastModifiedBy;
        this.countryId = countryId;
    }

    /** gets Division ID
     * @return divisionID Integer value of the 'Division_ID'
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** gets Division
     * @return division String value of the 'Division' in the database
     */
    public String getDivision() {
        return division;
    }

    /** gets Date
     * @return date String value that the 'Division' was created
     */
    public String getDate() {
        return date;
    }

    /** gets Created By
     * @return author String value of 'User' who created the 'Division'
     */
    public String getAuthor() {
        return author;
    }

    /** gets Last Updated
     * @return lastUpdate String value - last time the 'Division' was updated
     */
    public String getLastModified() {
        return lastModified;
    }

    /** gets Last Updated By
     * @return lastUpdatedBy String value - last 'User' to update the 'Division'
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /** gets Country ID
     * @return countryId String value - ID number of the 'Country' that the 'Division' belongs to
     */
    public int getCountryId() {
        return countryId;
    }

    /** @return 'division' name as a String */
    @Override
    public String toString() {
        return divisionId + " " + division;
    }
}
