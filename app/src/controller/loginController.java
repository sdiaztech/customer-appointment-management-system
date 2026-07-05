package controller;

import database.databaseQuery;
import database.JDBC;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class loginController implements Initializable {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label loginInstructionsLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label loginTitleLabel;

    //Incorrect login attempt error message to display
    private String errorMessage = "Invalid username and password combination! Please try again.";
    Alert invalidLoginAlert = new Alert(Alert.AlertType.ERROR, errorMessage);

    /** Initialize the Login Form.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialize");
        // Obtain the user's system locale and translate if necessary
        findUserLocation();
        translateLogin();
    }

    /** Obtains the user's system locale and displays it on the Login form. */
    public String findUserLocation() {
        // Get the default TimeZone
        TimeZone userTimeZone = TimeZone.getDefault();
        // Get the ZoneID from the TimeZone
        String userZone = userTimeZone.getID();
        // Set the location on the UI
        locationLabel.setText("Location: " + userZone);

        return userZone;
    }

    /** Translates the Login Form if the user's set language is French. */
    public void translateLogin() {
        //Get user's default locale
        Locale locale = Locale.getDefault();

        if (locale.getLanguage().toLowerCase().contains("fr") || locale.toString().toLowerCase().contains("fr")) {
            // Try-with-resources for French properties
            try (InputStream fileInputStream = new FileInputStream("src/language/frenchLoginScreen.properties")) {
                // Create a new Properties Object
                Properties frProperties = new Properties();
                // Load the information from the French translation file
                frProperties.load(fileInputStream);

                // Translate label text
                loginButton.setText(frProperties.getProperty("loginButton"));
                exitButton.setText(frProperties.getProperty("exitButton"));
                loginErrorLabel.setText(frProperties.getProperty("secureLoginLabel"));
                loginTitleLabel.setText(frProperties.getProperty("title"));
                loginInstructionsLabel.setText(frProperties.getProperty("subheading"));
                locationLabel.setText(frProperties.getProperty("locationLabel") + findUserLocation());
                usernameLabel.setText(frProperties.getProperty("usernameLabel"));
                passwordLabel.setText(frProperties.getProperty("passwordLabel"));
                errorMessage = frProperties.getProperty("errorMessage");
                invalidLoginAlert.setContentText(errorMessage);

                System.out.println("French successful.");
            }
            catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }


    /** If username and password combination are correct, open the Customer Records Menu and close the Login window.
     * If username and password combination incorrect, display error message.
     * @param actionEvent Click of 'Login' button
     */
    public void loginButtonListener(ActionEvent actionEvent) throws SQLException {
        // Get the text from the Username and Password TextFields
        System.out.println("Login button clicked!");
        String usernameText = usernameTextField.getText();
        String passwordText = passwordPasswordField.getText();

        boolean verificationPassed = verifyCredentials(usernameText, passwordText);

        // Check if username and password combination are correct
        if (verificationPassed) {
            // Delay to simulate 'logging in' action
            try {
                logLoginAttempt(createLoginAttempt(true));
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                e.getMessage();
            }

            // Close out Login window and display Customer Records
            Parent secondRoot = null;
            try {
                secondRoot = FXMLLoader.load(getClass().getResource("/view/viewCustomerRecords.fxml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            // New Scene/Stage
            Scene secondScene = new Scene(secondRoot, 800, 360);
            Stage secondStage = new Stage();
            secondStage.setTitle("Customer Records");
            secondStage.setScene(secondScene);

            // Close 'Login' window
            Stage loginStage = (Stage)loginButton.getScene().getWindow();
            loginStage.close();

            // Open new window
            secondStage.show();
        }
        else {
            logLoginAttempt(createLoginAttempt(false));
            invalidLoginAlert.show();
        }
    }

    /** Confirm the input for username and password against the stored credentials.
     * @param username input from the Username TextField.
     * @param password input from the Password PasswordField.
     * @return true if it passes the credential comparison.
     * @throws SQLException if the SQL is misshapen.
     */
    public boolean verifyCredentials(String username, String password) throws SQLException {
        String passwordInput = "";
        boolean passwordMatch  = false;

        try {
            // Check the database for the username from user input
            Connection connection = JDBC.getConnection();
            String sqlQuery = "SELECT * FROM users WHERE User_Name = ?";

            // Create an injection-resistant PreparedStatement
            databaseQuery.setPreparedStatement(connection, sqlQuery);
            PreparedStatement preparedStatement = databaseQuery.getPreparedStatement();
            preparedStatement.setString(1, username);

            // Compare user input against the database
            ResultSet resultSet = preparedStatement.executeQuery( );
            while (resultSet.next()) {
                passwordInput = resultSet.getString("Password");

                if (passwordInput.equals(password)) {
                    passwordMatch = true;
                }
                else {
                    passwordMatch = false;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return passwordMatch;
    }

    /** Creates a login attempt string
     * @param pass either success or failure for the login attempt
     * @return 'String' login attempt to write in the log
     */
    public String createLoginAttempt(boolean pass) {
        String loginAttempt = "";
        String userAttempt = usernameTextField.getText();
        String successfulOrUnsuccessful = "";
        LocalDateTime localDate = LocalDateTime.now();

        // Extract the currDate from the LocalDateTime
        String currDate = localDate.toString().substring(0, localDate.toString().indexOf("T", 0));
        System.out.println("Date: " + currDate);

        // Extract the currTime from the LocalDateTime
        String currTime = localDate.toString().substring(localDate.toString().indexOf("T", 0) + 1);
        currTime = currTime.substring(0, 8);
        System.out.println("Time: " + currTime);

        // Set the result of the login loginAttempt
        if (pass) {
            successfulOrUnsuccessful = "Successful";
        }
        else {
            successfulOrUnsuccessful = "Unsuccessful";
        }

        // Create the String that is to be logged
        loginAttempt = currDate + " || " + currTime + " || " + userAttempt + " || " + successfulOrUnsuccessful + System.lineSeparator( );

        return loginAttempt;
    }

    /** Writes info about the login loginAttempt to the log
     * @param loginAttempt 'String' value written to the log
     */
    public void logLoginAttempt(String loginAttempt) {
        // Create a new file object for the activity loggerFile
        File loggerFile = new File("login_activity.txt");

        // If login_activity file exists
        if (loggerFile.exists()) {
            try {
                // Create a FileWriter object
                FileWriter file = new FileWriter( "login_activity.txt", true );
                // Write login loginAttempt and close the file
                file.write(loginAttempt);
                file.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If login_activity file does not exist
        else {
            // Create the file
            try {
                loggerFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // Create a FileWriter object
                FileWriter file = new FileWriter("login_activity.txt", true);
                // Write header
                String header = "Date   |   Time   |   Username   |   Login Success   " + System.lineSeparator() +
                        "=================================================" + System.lineSeparator();
                file.write(header);
                // Write login loginAttempt and close file
                file.write(loginAttempt);
                file.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Click of the 'Exit' button closes the database connection and application.
     * @param actionEvent Click of 'Exit' button
     */
    public void exitButtonListener(ActionEvent actionEvent) {
        JDBC.closeConnection();
        System.out.println("Exit button clicked.");
        Platform.exit();
    }
}