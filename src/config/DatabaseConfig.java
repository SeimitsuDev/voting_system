package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    public static final String URL = "jdbc:mysql://localhost:3306/IPB_First";
    public static final String USER = "root";
    public static final String PASSWORD = "root1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String createCandidatesTable = "CREATE TABLE IF NOT EXISTS candidates (id INT PRIMARY KEY, name VARCHAR(255) NOT NULL, party VARCHAR(255) NOT NULL, votes INT DEFAULT 0);";

        String createVotersTable = "CREATE TABLE IF NOT EXISTS voters (id INT PRIMARY KEY, name VARCHAR(255) NOT NULL, has_voted BOOLEAN DEFAULT FALSE);";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createCandidatesTable);
            stmt.execute(createVotersTable);
            System.out.println("Database tables are ready.");
            
        } catch (SQLException e) {
            System.out.println("Error in initializing the database tabless: " + e.getMessage());
        }
    }
}