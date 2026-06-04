package com.pao.proiect.banca.service;

import com.pao.proiect.banca.config.DatabaseConnection;
import com.pao.proiect.banca.exception.ContInexistentException;
import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import com.pao.proiect.banca.model.TipTranzactie;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class JdbcTransferService {

    private final DatabaseConnection databaseConnection;

    public JdbcTransferService() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public void transferaCuTranzactie(String ibanSursa, String ibanDestinatie, BigDecimal suma) {
        try (Connection connection = databaseConnection.getConnection()) {
            boolean initialAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                BigDecimal soldSursa = citesteSold(connection, ibanSursa);
                BigDecimal soldDestinatie = citesteSold(connection, ibanDestinatie);
                if (soldSursa.compareTo(suma) < 0) {
                    throw new FonduriInsuficienteException(ibanSursa, soldSursa, suma);
                }

                actualizeazaSold(connection, ibanSursa, soldSursa.subtract(suma));
                actualizeazaSold(connection, ibanDestinatie, soldDestinatie.add(suma));
                inregistreazaTransfer(connection, ibanSursa, ibanDestinatie, suma);

                connection.commit();
                AuditService.getInstance().logAction("transfer_jdbc_tranzactie");
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(initialAutoCommit);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Transferul JDBC nu a putut fi executat.", e);
        }
    }

    private BigDecimal citesteSold(Connection connection, String iban) throws SQLException {
        String sql = "SELECT sold FROM accounts WHERE iban = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, iban);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new ContInexistentException(iban);
                }
                return new BigDecimal(resultSet.getString("sold"));
            }
        }
    }

    private void actualizeazaSold(Connection connection, String iban, BigDecimal soldNou) throws SQLException {
        String sql = "UPDATE accounts SET sold = ? WHERE iban = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, soldNou.toPlainString());
            statement.setString(2, iban);
            statement.executeUpdate();
        }
    }

    private void inregistreazaTransfer(Connection connection, String ibanSursa,
                                       String ibanDestinatie, BigDecimal suma) throws SQLException {
        String sql = """
                INSERT INTO transactions(iban_sursa, iban_destinatie, suma, data_ora, tip)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ibanSursa);
            statement.setString(2, ibanDestinatie);
            statement.setString(3, suma.toPlainString());
            statement.setString(4, LocalDateTime.now().toString());
            statement.setString(5, TipTranzactie.TRANSFER.name());
            statement.executeUpdate();
        }
    }
}

