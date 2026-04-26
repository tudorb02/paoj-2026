package com.pao.proiect.banca.model;

import com.pao.proiect.banca.exception.CardBlocatException;

import java.time.LocalDate;
import java.util.Objects;

// Clasa de baza pentru toate tipurile de card.
public abstract class Card {

    protected String numarCard;
    protected String cvv;
    protected LocalDate dataExpirare;
    protected boolean blocat;
    protected Cont contAtasat;

    protected Card(String numarCard, String cvv, LocalDate dataExpirare, Cont contAtasat) {
        this.numarCard = numarCard;
        this.cvv = cvv;
        this.dataExpirare = dataExpirare;
        this.contAtasat = contAtasat;
        this.blocat = false;
    }

    public abstract String getTipCard();

    public void asigurareCardActiv() {
        if (blocat) {
            throw new CardBlocatException(numarCard);
        }
        if (dataExpirare != null && dataExpirare.isBefore(LocalDate.now())) {
            throw new CardBlocatException(numarCard + " (expirat la " + dataExpirare + ")");
        }
    }

    public String getNumarCard() { return numarCard; }
    public void setNumarCard(String numarCard) { this.numarCard = numarCard; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public LocalDate getDataExpirare() { return dataExpirare; }
    public void setDataExpirare(LocalDate dataExpirare) { this.dataExpirare = dataExpirare; }

    public boolean isBlocat() { return blocat; }
    public void setBlocat(boolean blocat) { this.blocat = blocat; }

    public Cont getContAtasat() { return contAtasat; }
    public void setContAtasat(Cont contAtasat) { this.contAtasat = contAtasat; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(numarCard, card.numarCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numarCard);
    }

    @Override
    public String toString() {
        return getTipCard() + "{" +
                "numar='" + maskedCardNumber() + '\'' +
                ", expira=" + dataExpirare +
                ", blocat=" + blocat +
                ", iban=" + (contAtasat != null ? contAtasat.getIban() : "-") +
                '}';
    }

    private String maskedCardNumber() {
        if (numarCard == null || numarCard.length() < 4) return "****";
        return "****-****-****-" + numarCard.substring(numarCard.length() - 4);
    }
}
