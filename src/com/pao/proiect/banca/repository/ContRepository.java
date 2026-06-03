package com.pao.proiect.banca.repository;

import com.pao.proiect.banca.config.DatabaseConnection;
import com.pao.proiect.banca.model.Client;
import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.model.ContCurent;
import com.pao.proiect.banca.model.ContEconomii;
import com.pao.proiect.banca.model.IBAN;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContRepository implements Repository<Cont, String> {

    private final DatabaseConnection databaseConnection;
    private final ClientRepository clientRepository;

    public ContRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
        this.clientRepository = new ClientRepository();
    }

    @Override
    public void save(Cont entity) {
        String sql = """
                INSERT INTO accounts(iban, cnp_client, tip_cont, sold, data_deschidere, comision_lunar, dobanda_anuala)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, entity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Contul nu a putut fi salvat: " + entity.getIban(), e);
        }
    }

    @Override
    public Optional<Cont> findById(String iban) {
        String sql = """
                SELECT iban, cnp_client, tip_cont, sold, data_deschidere, comision_lunar, dobanda_anuala
                FROM accounts
                WHERE iban = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, iban);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapCont(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Contul nu a putut fi cautat: " + iban, e);
        }
    }

    @Override
    public List<Cont> findAll() {
        String sql = """
                SELECT iban, cnp_client, tip_cont, sold, data_deschidere, comision_lunar, dobanda_anuala
                FROM accounts
                ORDER BY iban
                """;
        List<Cont> accounts = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                accounts.add(mapCont(resultSet));
            }
            return accounts;
        } catch (SQLException e) {
            throw new IllegalStateException("Conturile nu au putut fi listate.", e);
        }
    }

    @Override
    public void update(Cont entity) {
        String sql = """
                UPDATE accounts
                SET cnp_client = ?, tip_cont = ?, sold = ?, data_deschidere = ?, comision_lunar = ?, dobanda_anuala = ?
                WHERE iban = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getTitular().getCnp());
            statement.setString(2, entity.getTipCont());
            statement.setString(3, entity.getSold().toPlainString());
            statement.setString(4, entity.getDataDeschidere().toString());
            setSpecificFields(statement, entity, 5, 6);
            statement.setString(7, entity.getIban().getValoare());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Contul nu a putut fi actualizat: " + entity.getIban(), e);
        }
    }

    @Override
    public void delete(String iban) {
        String sql = "DELETE FROM accounts WHERE iban = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, iban);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Contul nu a putut fi sters: " + iban, e);
        }
    }

    private void fillStatement(PreparedStatement statement, Cont entity) throws SQLException {
        statement.setString(1, entity.getIban().getValoare());
        statement.setString(2, entity.getTitular().getCnp());
        statement.setString(3, entity.getTipCont());
        statement.setString(4, entity.getSold().toPlainString());
        statement.setString(5, entity.getDataDeschidere().toString());
        setSpecificFields(statement, entity, 6, 7);
    }

    private void setSpecificFields(PreparedStatement statement, Cont entity,
                                   int comisionIndex, int dobandaIndex) throws SQLException {
        if (entity instanceof ContCurent contCurent) {
            statement.setString(comisionIndex, contCurent.getComisionLunar().toPlainString());
            statement.setString(dobandaIndex, null);
        } else if (entity instanceof ContEconomii contEconomii) {
            statement.setString(comisionIndex, null);
            statement.setString(dobandaIndex, contEconomii.getDobandaAnuala().toPlainString());
        } else {
            statement.setString(comisionIndex, null);
            statement.setString(dobandaIndex, null);
        }
    }

    private Cont mapCont(ResultSet resultSet) throws SQLException {
        Client titular = clientRepository.findById(resultSet.getString("cnp_client")).orElse(null);
        IBAN iban = new IBAN(resultSet.getString("iban"));
        BigDecimal sold = decimal(resultSet.getString("sold"));
        LocalDate dataDeschidere = LocalDate.parse(resultSet.getString("data_deschidere"));
        String tipCont = resultSet.getString("tip_cont");

        Cont cont;
        if ("ContEconomii".equals(tipCont)) {
            cont = new ContEconomii(
                    iban,
                    titular,
                    sold,
                    dataDeschidere,
                    decimalOrZero(resultSet.getString("dobanda_anuala")));
        } else {
            cont = new ContCurent(
                    iban,
                    titular,
                    sold,
                    dataDeschidere,
                    decimalOrZero(resultSet.getString("comision_lunar")));
        }
        if (titular != null) {
            titular.adaugaCont(cont);
        }
        return cont;
    }

    private BigDecimal decimal(String value) {
        return new BigDecimal(value);
    }

    private BigDecimal decimalOrZero(String value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(value);
    }
}

