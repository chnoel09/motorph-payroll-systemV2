package com.mycompany.oop;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Properties DB_PROPERTIES = loadDatabaseProperties();

    private static final String URL = getConfigValue("DB_URL", "db.url");
    private static final String USER = getConfigValue("DB_USER", "db.user");
    private static final String PASSWORD = getConfigValue("DB_PASSWORD", "db.password");

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    private static String getConfigValue(String overrideKey, String propertyKey) {
        String systemValue = System.getProperty(overrideKey);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String environmentValue = System.getenv(overrideKey);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        return DB_PROPERTIES.getProperty(propertyKey, "");
    }

    private static Properties loadDatabaseProperties() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getResourceAsStream("/database.properties")) {

            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database.properties.", e);
        }

        return properties;
    }
}
