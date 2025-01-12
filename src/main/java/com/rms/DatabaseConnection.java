package com.rms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Method to initialize and return the database connection
    public static Connection initializeDatabase() throws SQLException, ClassNotFoundException {
        // Load the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/yourDatabaseName"; // Replace with your database URL
        String username = "yourUsername"; // Replace with your DB username
        String password = "yourPassword"; // Replace with your DB password

        // Create and return the connection
        return DriverManager.getConnection(url, username, password);
    }
}
