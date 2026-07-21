package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JDBC {
    private static final String JDBC_URL = "jdbc:mysql://localhost/client_schedule?connectionTimeZone=SERVER";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER_NAME = "sqlUser";
    private static final String PASSWORD = "Passw0rd!";
    private static Connection connection = null;
    private JDBC() {
    }

    public static void makeConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);
            System.out.println("Connection successful!");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Error:" + e.getMessage());
        }
        catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
    public static Connection getConnection() {
        return connection;
    }
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed!");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
