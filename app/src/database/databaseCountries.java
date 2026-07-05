package database;

import model.Country;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class databaseCountries {

    /** Getter gets a list of Countries
     * @return 'ObservableList' containing each 'Country'
     */
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM countries";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int countryId = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");
                Country country = new Country(countryId, countryName);
                countryList.add(country);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return countryList;
    }

    public static void checkDateConversion() {
        System.out.println("CREATE DATE TEST");
        try {
            String sqlQuery = "SELECT Create_Date FROM countries";
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("Create_Date");
                System.out.println("Create date: " + timestamp.toLocalDateTime().toString());
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}