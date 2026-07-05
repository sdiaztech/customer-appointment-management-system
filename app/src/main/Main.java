package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import database.JDBC;
import model.Appointment;
import model.Customer;

import java.sql.SQLException;


public class Main extends Application {
    public static int currId;
    public static int randNumber;
    public static Customer chosenCustomer = null;
    public static Appointment selectedAppointment = null;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        primaryStage.setScene(new Scene(root, 520, 400));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        JDBC.makeConnection();
        launch(args);
    }
}