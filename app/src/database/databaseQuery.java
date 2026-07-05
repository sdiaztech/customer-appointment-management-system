package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Constructor class with the purpose of calling queries to the database */
public class databaseQuery {
    private static PreparedStatement statement;

    /** Setter sets the Statement object */
    public static void setPreparedStatement(Connection connection, String sqlQuery) throws SQLException {
        // Create a statement via the connection
        statement = connection.prepareStatement(sqlQuery);
    }

    /** Getter gets the Statement object */
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }
}