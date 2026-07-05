package database;

import model.Division;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDivisions {

    public static ObservableList<Division> getUSADivisions() {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM client_schedule.first_level_divisions WHERE Country_ID=1";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                String divisionDate = resultSet.getString("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                String lastUpdate = resultSet.getString("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int countryId = resultSet.getInt("Country_ID");

                Division division = new Division(divisionId, divisionName, divisionDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
                divisionList.add(division);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return divisionList;
    }

    public static ObservableList<Division> getCanadaDivisions() {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM first_level_divisions WHERE Country_ID = 2";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                String date = resultSet.getString("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                String lastUpdate = resultSet.getString("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int countryId = resultSet.getInt("Country_ID");

                Division division = new Division(divisionId, divisionName, date, createdBy, lastUpdate, lastUpdatedBy, countryId);
                divisionList.add(division);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return divisionList;
    }

    public static ObservableList<Division> getUKDivisions() {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM first_level_divisions WHERE Country_ID = 3";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                String date = resultSet.getString("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                String lastUpdate = resultSet.getString("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int countryId = resultSet.getInt("Country_ID");

                Division division = new Division(divisionId, divisionName, date, createdBy, lastUpdate, lastUpdatedBy, countryId);
                divisionList.add(division);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return divisionList;
    }
}