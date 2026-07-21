package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseDivisions {
    private DatabaseDivisions() {
    }

    public static ObservableList<Division> getUSADivisions() {
        return getDivisionsByCountry(1);
    }

    public static ObservableList<Division> getCanadaDivisions() {
        return getDivisionsByCountry(2);
    }

    public static ObservableList<Division> getUKDivisions() {
        return getDivisionsByCountry(3);
    }

    private static ObservableList<Division> getDivisionsByCountry(int countryId) {
        ObservableList<Division> divisions = FXCollections.observableArrayList();
        String sql = "SELECT Division_ID, Division, Create_Date, Created_By, Last_Update, "
                + "Last_Updated_By, Country_ID FROM first_level_divisions WHERE Country_ID = ?";

        try (PreparedStatement statement = JDBC.getConnection().prepareStatement(sql)) {
            statement.setInt(1, countryId);
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    divisions.add(new Division(
                            results.getInt("Division_ID"),
                            results.getString("Division"),
                            results.getString("Create_Date"),
                            results.getString("Created_By"),
                            results.getString("Last_Update"),
                            results.getString("Last_Updated_By"),
                            results.getInt("Country_ID")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return divisions;
    }
}
