package database;

import model.Country;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class DatabaseCountries {

    private DatabaseCountries() {
    }

    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        String sqlQuery = "SELECT Country_ID, Country FROM countries";
        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int countryId = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");
                Country country = new Country(countryId, countryName);
                countryList.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countryList;
    }
}
