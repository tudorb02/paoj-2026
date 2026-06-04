package com.pao.proiect.banca.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

// Singleton responsabil de citirea configuratiei si deschiderea conexiunilor JDBC.
public final class DatabaseConnection {

    private static DatabaseConnection instance;

    private final Properties properties;

    private DatabaseConnection() {
        this.properties = loadProperties();
        loadDriverIfConfigured();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url", "jdbc:sqlite:bank.db");
        String user = properties.getProperty("db.user", "");
        String password = properties.getProperty("db.password", "");

        Connection connection;
        if (user.isBlank() && password.isBlank()) {
            connection = DriverManager.getConnection(url);
        } else {
            connection = DriverManager.getConnection(url, user, password);
        }
        configureConnection(connection, url);
        return connection;
    }

    public String getDatabaseUrl() {
        return properties.getProperty("db.url", "jdbc:sqlite:bank.db");
    }

    private void configureConnection(Connection connection, String url) throws SQLException {
        if (url.startsWith("jdbc:sqlite:")) {
            try (PreparedStatement statement = connection.prepareStatement("PRAGMA foreign_keys = ON")) {
                statement.execute();
            }
        }
    }

    private void loadDriverIfConfigured() {
        String driver = properties.getProperty("db.driver", "").trim();
        if (driver.isEmpty()) {
            return;
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Driverul JDBC nu a fost gasit: " + driver, e);
        }
    }

    private Properties loadProperties() {
        Properties result = new Properties();
        try (InputStream classpathInput = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (classpathInput != null) {
                result.load(classpathInput);
                return result;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nu pot citi db.properties din classpath.", e);
        }

        Path[] candidates = {
                Path.of("resources", "db.properties"),
                Path.of("Paoj-aplicatie-bancara", "resources", "db.properties")
        };
        for (Path candidate : candidates) {
            if (!Files.exists(candidate)) {
                continue;
            }
            try (InputStream input = Files.newInputStream(candidate)) {
                result.load(input);
                return result;
            } catch (IOException e) {
                throw new IllegalStateException("Nu pot citi configuratia DB din " + candidate, e);
            }
        }

        result.setProperty("db.driver", "org.sqlite.JDBC");
        result.setProperty("db.url", "jdbc:sqlite:bank.db");
        result.setProperty("db.user", "");
        result.setProperty("db.password", "");
        return result;
    }
}

