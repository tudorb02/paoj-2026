package com.pao.proiect.banca.repository;

import com.pao.proiect.banca.config.DatabaseConnection;
import com.pao.proiect.banca.dto.ClientConturiReport;
import com.pao.proiect.banca.dto.ContCarduriReport;
import com.pao.proiect.banca.dto.TranzactieDetaliataReport;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RaportRepository {

    private final DatabaseConnection databaseConnection;

    public RaportRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    public List<ClientConturiReport> clientiCuNumarConturiSiSoldTotal() {
        String sql = """
                SELECT c.cnp,
                       c.nume || ' ' || c.prenume AS nume_complet,
                       COUNT(a.iban) AS numar_conturi,
                       COALESCE(SUM(CAST(a.sold AS NUMERIC)), 0) AS sold_total
                FROM clients c
                LEFT JOIN accounts a ON a.cnp_client = c.cnp
                GROUP BY c.cnp, c.nume, c.prenume
                ORDER BY c.nume, c.prenume
                """;
        List<ClientConturiReport> reports = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reports.add(new ClientConturiReport(
                        resultSet.getString("cnp"),
                        resultSet.getString("nume_complet"),
                        resultSet.getInt("numar_conturi"),
                        decimalOrZero(resultSet.getBigDecimal("sold_total"))));
            }
            return reports;
        } catch (SQLException e) {
            throw new IllegalStateException("Raportul clientilor cu conturi nu a putut fi generat.", e);
        }
    }

    public List<ContCarduriReport> conturiCuTitularSiNumarCarduri() {
        String sql = """
                SELECT a.iban,
                       a.tip_cont,
                       c.nume || ' ' || c.prenume AS titular,
                       a.sold,
                       COUNT(cd.numar_card) AS numar_carduri
                FROM accounts a
                JOIN clients c ON c.cnp = a.cnp_client
                LEFT JOIN cards cd ON cd.iban = a.iban
                GROUP BY a.iban, a.tip_cont, c.nume, c.prenume, a.sold
                ORDER BY titular, a.iban
                """;
        List<ContCarduriReport> reports = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reports.add(new ContCarduriReport(
                        resultSet.getString("iban"),
                        resultSet.getString("tip_cont"),
                        resultSet.getString("titular"),
                        new BigDecimal(resultSet.getString("sold")),
                        resultSet.getInt("numar_carduri")));
            }
            return reports;
        } catch (SQLException e) {
            throw new IllegalStateException("Raportul conturilor cu carduri nu a putut fi generat.", e);
        }
    }

    public List<TranzactieDetaliataReport> tranzactiiCuTitulari() {
        String sql = """
                SELECT t.id,
                       t.tip,
                       t.suma,
                       t.data_ora,
                       t.iban_sursa,
                       cs.nume || ' ' || cs.prenume AS titular_sursa,
                       t.iban_destinatie,
                       cd.nume || ' ' || cd.prenume AS titular_destinatie
                FROM transactions t
                LEFT JOIN accounts asrc ON asrc.iban = t.iban_sursa
                LEFT JOIN clients cs ON cs.cnp = asrc.cnp_client
                LEFT JOIN accounts adst ON adst.iban = t.iban_destinatie
                LEFT JOIN clients cd ON cd.cnp = adst.cnp_client
                ORDER BY t.data_ora DESC, t.id DESC
                """;
        List<TranzactieDetaliataReport> reports = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reports.add(new TranzactieDetaliataReport(
                        resultSet.getLong("id"),
                        resultSet.getString("tip"),
                        new BigDecimal(resultSet.getString("suma")),
                        LocalDateTime.parse(resultSet.getString("data_ora")),
                        resultSet.getString("iban_sursa"),
                        resultSet.getString("titular_sursa"),
                        resultSet.getString("iban_destinatie"),
                        resultSet.getString("titular_destinatie")));
            }
            return reports;
        } catch (SQLException e) {
            throw new IllegalStateException("Raportul tranzactiilor detaliate nu a putut fi generat.", e);
        }
    }

    private BigDecimal decimalOrZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}

