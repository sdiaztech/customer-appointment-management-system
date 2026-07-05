package main;


import database.JDBC;
import model.Appointments;
import model.Customer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;


public class Main extends Application {
    //The running count of the ID numbers of each 'Customer'
    public static int currId;
    //The running count of the ID numbers of each 'Appointment'
    public static int randNumber;
    //Customer currently selected in the customerRecordsTableView
    public static Customer chosenCustomer = null;
    //Appointment currently selected in the viewAppointmentsTableView
    public static Appointments selectedAppointments = null;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load( getClass().getResource("../view/login.fxml"));
        //primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root, 520, 400));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        JDBC.makeConnection();
        launch(args);
    }
}