package com.pao.proiect.banca.repository;

import com.pao.proiect.banca.config.DatabaseConnection;
import com.pao.proiect.banca.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {

    private final DatabaseConnection databaseConnection;

    public ClientRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    @Override
    public void save(Client entity) {
        String sql = """
                INSERT INTO clients(cnp, nume, prenume, email, telefon, data_inregistrare)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, entity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Clientul nu a putut fi salvat: " + entity.getCnp(), e);
        }
    }

    @Override
    public Optional<Client> findById(String cnp) {
        String sql = """
                SELECT cnp, nume, prenume, email, telefon, data_inregistrare
                FROM clients
                WHERE cnp = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cnp);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapClient(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Clientul nu a putut fi cautat: " + cnp, e);
        }
    }

    @Override
    public List<Client> findAll() {
        String sql = """
                SELECT cnp, nume, prenume, email, telefon, data_inregistrare
                FROM clients
                ORDER BY nume, prenume
                """;
        List<Client> clients = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clients.add(mapClient(resultSet));
            }
            return clients;
        } catch (SQLException e) {
            throw new IllegalStateException("Clientii nu au putut fi listati.", e);
        }
    }

    @Override
    public void update(Client entity) {
        String sql = """
                UPDATE clients
                SET nume = ?, prenume = ?, email = ?, telefon = ?, data_inregistrare = ?
                WHERE cnp = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getPrenume());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getTelefon());
            statement.setString(5, entity.getDataInregistrare().toString());
            statement.setString(6, entity.getCnp());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Clientul nu a putut fi actualizat: " + entity.getCnp(), e);
        }
    }

    @Override
    public void delete(String cnp) {
        String sql = "DELETE FROM clients WHERE cnp = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cnp);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Clientul nu a putut fi sters: " + cnp, e);
        }
    }

    private void fillStatement(PreparedStatement statement, Client entity) throws SQLException {
        statement.setString(1, entity.getCnp());
        statement.setString(2, entity.getNume());
        statement.setString(3, entity.getPrenume());
        statement.setString(4, entity.getEmail());
        statement.setString(5, entity.getTelefon());
        statement.setString(6, entity.getDataInregistrare().toString());
    }

    private Client mapClient(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getString("nume"),
                resultSet.getString("prenume"),
                resultSet.getString("cnp"),
                resultSet.getString("email"),
                resultSet.getString("telefon"),
                LocalDate.parse(resultSet.getString("data_inregistrare")));
    }
}

