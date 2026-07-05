package controller;

import database.DatabaseQuery;
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

public class LoginController implements Initializable {
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

    private String errorMessage = "Invalid username and password combination! Please try again.";
    Alert invalidLoginAlert = new Alert(Alert.AlertType.ERROR, errorMessage);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        findUserLocation();
        translateLogin();
    }

    public String findUserLocation() {
        TimeZone userTimeZone = TimeZone.getDefault();
        String userZone = userTimeZone.getID();
        locationLabel.setText("Location: " + userZone);

        return userZone;
    }

    public void translateLogin() {
        Locale locale = Locale.getDefault();

        if (locale.getLanguage().toLowerCase().contains("fr") || locale.toString().toLowerCase().contains("fr")) {
            try (InputStream fileInputStream = new FileInputStream("src/language/frenchLoginScreen.properties")) {
                Properties frProperties = new Properties();
                frProperties.load(fileInputStream);

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
            }
            catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    public void loginButtonListener(ActionEvent actionEvent) throws SQLException {
        String usernameText = usernameTextField.getText();
        String passwordText = passwordPasswordField.getText();

        boolean verificationPassed = verifyCredentials(usernameText, passwordText);

        if (verificationPassed) {
            try {
                logLoginAttempt(createLoginAttempt(true));
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                e.getMessage();
            }

            Parent secondRoot = null;
            try {
                secondRoot = FXMLLoader.load(getClass().getResource("/view/viewCustomerRecords.fxml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            Scene secondScene = new Scene(secondRoot, 800, 360);
            Stage secondStage = new Stage();
            secondStage.setTitle("Customer Records");
            secondStage.setScene(secondScene);

            Stage loginStage = (Stage)loginButton.getScene().getWindow();
            loginStage.close();

            secondStage.show();
        }
        else {
            logLoginAttempt(createLoginAttempt(false));
            invalidLoginAlert.show();
        }
    }

    public boolean verifyCredentials(String username, String password) throws SQLException {
        String passwordInput = "";
        boolean passwordMatch  = false;

        try {
            Connection connection = JDBC.getConnection();
            String sqlQuery = "SELECT * FROM users WHERE User_Name = ?";

            DatabaseQuery.setPreparedStatement(connection, sqlQuery);
            PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();
            preparedStatement.setString(1, username);

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

    public String createLoginAttempt(boolean pass) {
        String userAttempt = usernameTextField.getText();
        LocalDateTime localDate = LocalDateTime.now();

        String currDate = localDate.toString().substring(0, localDate.toString().indexOf("T", 0));

        String currTime = localDate.toString().substring(localDate.toString().indexOf("T", 0) + 1);
        currTime = currTime.substring(0, 8);

        String successfulOrUnsuccessful = pass ? "Successful" : "Unsuccessful";

        return currDate + " || " + currTime + " || " + userAttempt + " || " + successfulOrUnsuccessful + System.lineSeparator( );
    }

    public void logLoginAttempt(String loginAttempt) {
        File loggerFile = new File("login_activity.txt");

        if (loggerFile.exists()) {
            try {
                FileWriter file = new FileWriter( "login_activity.txt", true );
                file.write(loginAttempt);
                file.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                loggerFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileWriter file = new FileWriter("login_activity.txt", true);
                String header = "Date   |   Time   |   Username   |   Login Success   " + System.lineSeparator() +
                        "=================================================" + System.lineSeparator();
                file.write(header);
                file.write(loginAttempt);
                file.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void exitButtonListener(ActionEvent actionEvent) {
        JDBC.closeConnection();
        Platform.exit();
    }
}