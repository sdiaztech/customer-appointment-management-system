package model;

public class Division {
    private int divisionId;
    private String division;

    public Division(int divisionId, String division) {
        this.divisionId = divisionId;
        this.division = division;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivision() {
        return division;
    }

    @Override
    public String toString() {
        return division;
    }
}
