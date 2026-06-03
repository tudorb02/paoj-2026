package com.pao.proiect.banca.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Ruleaza schema.sql pentru o baza de date curata la demonstratie.
public final class DatabaseInitializer {

    private static DatabaseInitializer instance;

    private final DatabaseConnection databaseConnection;

    private DatabaseInitializer() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public static synchronized DatabaseInitializer getInstance() {
        if (instance == null) {
            instance = new DatabaseInitializer();
        }
        return instance;
    }

    public void resetDatabase() {
        String script = loadSchemaScript();
        try (Connection connection = databaseConnection.getConnection()) {
            for (String statementText : splitStatements(script)) {
                if (statementText.isBlank()) {
                    continue;
                }
                try (PreparedStatement statement = connection.prepareStatement(statementText)) {
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Schema bazei de date nu a putut fi aplicata.", e);
        }
    }

    private String loadSchemaScript() {
        Path[] candidates = {
                Path.of("schema.sql"),
                Path.of("Paoj-aplicatie-bancara", "schema.sql")
        };
        for (Path candidate : candidates) {
            if (!Files.exists(candidate)) {
                continue;
            }
            try {
                return Files.readString(candidate, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IllegalStateException("Nu pot citi schema SQL din " + candidate, e);
            }
        }

        try (InputStream input = DatabaseInitializer.class
                .getClassLoader()
                .getResourceAsStream("schema.sql")) {
            if (input == null) {
                throw new IllegalStateException("Nu exista schema.sql in proiect.");
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Nu pot citi schema.sql din classpath.", e);
        }
    }

    private String[] splitStatements(String script) {
        StringBuilder cleanedScript = new StringBuilder();
        for (String line : script.split("\\R")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                continue;
            }
            cleanedScript.append(line).append('\n');
        }
        return cleanedScript.toString().split(";");
    }
}

