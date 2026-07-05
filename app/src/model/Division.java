package model;

public class Division {
    private int divisionId;
    private String division;
    private String date;
    private String author;
    private String lastModified;
    private String lastModifiedBy;
    private int countryId;

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

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivision() {
        return division;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public int getCountryId() {
        return countryId;
    }

    @Override
    public String toString() {
        return division;
    }
}
