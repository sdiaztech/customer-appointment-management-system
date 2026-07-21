package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DatabaseQuery {
    private static PreparedStatement statement;

    private DatabaseQuery() {
    }

    public static void setPreparedStatement(Connection connection, String sqlQuery) throws SQLException {
        statement = connection.prepareStatement(sqlQuery);
    }

    public static PreparedStatement getPreparedStatement() {
        return statement;
    }
}
