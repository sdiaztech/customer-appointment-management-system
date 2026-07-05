package model;

/** This class is an object constructor used to create and get User objects */
public class Country {
    private int countryId;
    private String countryName;


    /** constructor for a 'Country' object
     * @param countryId the ID number of the 'Country'
     * @param countryName the 'name' of the 'Country'
     */
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /** Getter gets the ID number of the 'Country'
     * @return countryId as int value
     */
    public int getCountryId() {
        return countryId;
    }

    /** Getter gets the 'name' of the 'Country'
     * @return countryName as String value
     */
    public String getCountryName() {
        return countryName;
    }

    /** Getter gets Country name as a String value
     * @return countryName as a String
     * */
    @Override
    public String toString() {
        return (countryName);
    }
}