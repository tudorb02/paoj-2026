package com.pao.proiect.banca.repository;

import com.pao.proiect.banca.config.DatabaseConnection;
import com.pao.proiect.banca.model.Card;
import com.pao.proiect.banca.model.CardCredit;
import com.pao.proiect.banca.model.CardDebit;
import com.pao.proiect.banca.model.Cont;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Card, String> {

    private final DatabaseConnection databaseConnection;
    private final ContRepository contRepository;

    public CardRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
        this.contRepository = new ContRepository();
    }

    @Override
    public void save(Card entity) {
        String sql = """
                INSERT INTO cards(numar_card, iban, tip_card, cvv, data_expirare, blocat, limita_credit, dobanda_anuala)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, entity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cardul nu a putut fi salvat: " + entity.getNumarCard(), e);
        }
    }

    @Override
    public Optional<Card> findById(String numarCard) {
        String sql = """
                SELECT numar_card, iban, tip_card, cvv, data_expirare, blocat, limita_credit, dobanda_anuala
                FROM cards
                WHERE numar_card = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, numarCard);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapCard(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cardul nu a putut fi cautat: " + numarCard, e);
        }
    }

    @Override
    public List<Card> findAll() {
        String sql = """
                SELECT numar_card, iban, tip_card, cvv, data_expirare, blocat, limita_credit, dobanda_anuala
                FROM cards
                ORDER BY numar_card
                """;
        List<Card> cards = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                cards.add(mapCard(resultSet));
            }
            return cards;
        } catch (SQLException e) {
            throw new IllegalStateException("Cardurile nu au putut fi listate.", e);
        }
    }

    @Override
    public void update(Card entity) {
        String sql = """
                UPDATE cards
                SET iban = ?, tip_card = ?, cvv = ?, data_expirare = ?, blocat = ?, limita_credit = ?, dobanda_anuala = ?
                WHERE numar_card = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getContAtasat().getIban().getValoare());
            statement.setString(2, entity.getTipCard());
            statement.setString(3, entity.getCvv());
            statement.setString(4, entity.getDataExpirare().toString());
            statement.setInt(5, entity.isBlocat() ? 1 : 0);
            setCreditFields(statement, entity, 6, 7);
            statement.setString(8, entity.getNumarCard());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cardul nu a putut fi actualizat: " + entity.getNumarCard(), e);
        }
    }

    @Override
    public void delete(String numarCard) {
        String sql = "DELETE FROM cards WHERE numar_card = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, numarCard);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cardul nu a putut fi sters: " + numarCard, e);
        }
    }

    private void fillStatement(PreparedStatement statement, Card entity) throws SQLException {
        statement.setString(1, entity.getNumarCard());
        statement.setString(2, entity.getContAtasat().getIban().getValoare());
        statement.setString(3, entity.getTipCard());
        statement.setString(4, entity.getCvv());
        statement.setString(5, entity.getDataExpirare().toString());
        statement.setInt(6, entity.isBlocat() ? 1 : 0);
        setCreditFields(statement, entity, 7, 8);
    }

    private void setCreditFields(PreparedStatement statement, Card entity,
                                 int limitaIndex, int dobandaIndex) throws SQLException {
        if (entity instanceof CardCredit cardCredit) {
            statement.setString(limitaIndex, cardCredit.getLimitaCredit().toPlainString());
            statement.setString(dobandaIndex, cardCredit.getDobandaAnuala().toPlainString());
        } else {
            statement.setString(limitaIndex, null);
            statement.setString(dobandaIndex, null);
        }
    }

    private Card mapCard(ResultSet resultSet) throws SQLException {
        Cont cont = contRepository.findById(resultSet.getString("iban")).orElse(null);
        String tipCard = resultSet.getString("tip_card");
        Card card;
        if ("CardCredit".equals(tipCard)) {
            card = new CardCredit(
                    resultSet.getString("numar_card"),
                    resultSet.getString("cvv"),
                    LocalDate.parse(resultSet.getString("data_expirare")),
                    cont,
                    decimalOrZero(resultSet.getString("limita_credit")),
                    decimalOrZero(resultSet.getString("dobanda_anuala")));
        } else {
            card = new CardDebit(
                    resultSet.getString("numar_card"),
                    resultSet.getString("cvv"),
                    LocalDate.parse(resultSet.getString("data_expirare")),
                    cont);
        }
        card.setBlocat(resultSet.getInt("blocat") == 1);
        return card;
    }

    private BigDecimal decimalOrZero(String value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(value);
    }
}

