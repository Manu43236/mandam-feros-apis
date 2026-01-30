package com.api.feros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectionTest {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/feros_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Manu@512";

    public static void main(String[] args) {
        System.out.println("🔌 Attempting to connect to MySQL database...\n");
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully");

            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Database connection established successfully\n");

            // Get database metadata
            System.out.println("📊 Database Information:");
            System.out.println("   Database: " + connection.getCatalog());
            System.out.println("   URL: " + DB_URL);
            System.out.println("   User: " + DB_USER);
            System.out.println();

            // Query to get all tables in the database
            statement = connection.createStatement();
            String query = "SHOW TABLES";
            resultSet = statement.executeQuery(query);

            // Collect table names
            List<String> tables = new ArrayList<>();
            while (resultSet.next()) {
                tables.add(resultSet.getString(1));
            }

            // Display results
            if (tables.isEmpty()) {
                System.out.println("⚠️  No tables found in database 'feros_db'");
                System.out.println("   The database exists but is empty.");
            } else {
                System.out.println("📋 Tables in database 'feros_db' (" + tables.size() + " total):");
                System.out.println("   " + "=".repeat(50));
                for (int i = 0; i < tables.size(); i++) {
                    System.out.printf("   %2d. %s%n", (i + 1), tables.get(i));
                }
                System.out.println("   " + "=".repeat(50));
            }

            System.out.println("\n✅ Database connection test completed successfully!");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            System.err.println("   Make sure mysql-connector-j is in your dependencies.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Database connection failed!");
            System.err.println("   Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) {
                    connection.close();
                    System.out.println("\n🔌 Database connection closed.");
                }
            } catch (Exception e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
