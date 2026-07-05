Title: Customer Appointment Scheduler
----------------------------------------------------------
Author: Samuel Diaz
Email: sdiaz64@wgu.edu
Version: 1.0
Date: July 1, 2023

----------------------------------------------------------
Description

Customer Appointment Scheduler is a JavaFX desktop application that
allows users to manage customers and appointments through a MySQL
database. The application supports creating, updating, deleting,
and viewing customer and appointment records, as well as generating
appointment reports.

----------------------------------------------------------
Requirements

- Java Development Kit (JDK) 17
- JavaFX SDK 17
- MySQL Connector/J 8.0.33
- MySQL Server with the client_schedule database
- Visual Studio Code (or another Java IDE)

----------------------------------------------------------
Running the Application

1. Clone the repository.
2. Download the JavaFX SDK 17.
3. Copy the required JavaFX libraries into:

   lib/javafx/

4. Place the MySQL Connector/J JAR into:

   lib/mysql-connector-j-8.0.33.jar

5. Ensure the database connection settings in:

   app/src/database/JDBC.java

   match your local MySQL configuration.

6. Compile and run the application using the provided launch
   configuration or the following command:

   java --module-path lib/javafx \
        --add-modules javafx.controls,javafx.fxml \
        -cp "out:lib/mysql-connector-j-8.0.33.jar" \
        main.Main

----------------------------------------------------------
Additional Report

Displays all appointments for a selected user by listing each
appointment ID and its associated title.

----------------------------------------------------------
Development Environment

IDE: Visual Studio Code
JDK: OpenJDK 17
JavaFX: SDK 17
MySQL Connector/J: 8.0.33