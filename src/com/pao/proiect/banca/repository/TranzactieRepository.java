package com.pao.proiect.banca.repository;

import com.pao.proiect.banca.config.DatabaseConnection;
import com.pao.proiect.banca.model.IBAN;
import com.pao.proiect.banca.model.TipTranzactie;
import com.pao.proiect.banca.model.Tranzactie;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TranzactieRepository implements Repository<Tranzactie, Long> {

    private final DatabaseConnection databaseConnection;

    public TranzactieRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    @Override
    public void save(Tranzactie entity) {
        String sql = """
                INSERT INTO transactions(id, iban_sursa, iban_destinatie, suma, data_ora, tip)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entity.getId());
            setIban(statement, 2, entity.getIbanSursa());
            setIban(statement, 3, entity.getIbanDestinatie());
            statement.setString(4, entity.getSuma().toPlainString());
            statement.setString(5, entity.getDataOra().toString());
            statement.setString(6, entity.getTip().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Tranzactia nu a putut fi salvata: " + entity.getId(), e);
        }
    }

    @Override
    public Optional<Tranzactie> findById(Long id) {
        String sql = """
                SELECT id, iban_sursa, iban_destinatie, suma, data_ora, tip
                FROM transactions
                WHERE id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapTranzactie(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Tranzactia nu a putut fi cautata: " + id, e);
        }
    }

    @Override
    public List<Tranzactie> findAll() {
        String sql = """
                SELECT id, iban_sursa, iban_destinatie, suma, data_ora, tip
                FROM transactions
                ORDER BY data_ora DESC, id DESC
                """;
        List<Tranzactie> transactions = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                transactions.add(mapTranzactie(resultSet));
            }
            return transactions;
        } catch (SQLException e) {
            throw new IllegalStateException("Tranzactiile nu au putut fi listate.", e);
        }
    }

    @Override
    public void update(Tranzactie entity) {
        String sql = """
                UPDATE transactions
                SET iban_sursa = ?, iban_destinatie = ?, suma = ?, data_ora = ?, tip = ?
                WHERE id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setIban(statement, 1, entity.getIbanSursa());
            setIban(statement, 2, entity.getIbanDestinatie());
            statement.setString(3, entity.getSuma().toPlainString());
            statement.setString(4, entity.getDataOra().toString());
            statement.setString(5, entity.getTip().name());
            statement.setLong(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Tranzactia nu a putut fi actualizata: " + entity.getId(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Tranzactia nu a putut fi stearsa: " + id, e);
        }
    }

    private void setIban(PreparedStatement statement, int index, IBAN iban) throws SQLException {
        if (iban == null) {
            statement.setString(index, null);
        } else {
            statement.setString(index, iban.getValoare());
        }
    }

    private Tranzactie mapTranzactie(ResultSet resultSet) throws SQLException {
        return new Tranzactie(
                resultSet.getLong("id"),
                ibanOrNull(resultSet.getString("iban_sursa")),
                ibanOrNull(resultSet.getString("iban_destinatie")),
                new BigDecimal(resultSet.getString("suma")),
                TipTranzactie.valueOf(resultSet.getString("tip")),
                LocalDateTime.parse(resultSet.getString("data_ora")));
    }

    private IBAN ibanOrNull(String value) {
        return value == null ? null : new IBAN(value);
    }
}

