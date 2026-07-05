package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String database = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + database + "?connectionTimeZone = " + "SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static String password = "Passw0rd!";
    private static Connection connection = null;
    private static PreparedStatement preparedStatement;
    public static void makeConnection() {
        try {
            Class.forName(driver);
            // password = Details.getPassword();
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
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
            connection.close();
            System.out.println("Connection closed!");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void makePreparedStatement(String sqlStatement, Connection conn) throws SQLException {
        if (conn != null) {
            preparedStatement = conn.prepareStatement(sqlStatement);
        }
        else {
            System.out.println("Prepared Statement Creation Failed!");
        }
    }
    public static PreparedStatement getPreparedStatement() throws SQLException {
        if (preparedStatement == null) {
            System.out.println("Null reference to Prepared Statement");
            return null;
        }
        return preparedStatement;
    }
}
