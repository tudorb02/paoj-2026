package com.pao.proiect.banca.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Cont de economii cu dobanda anuala.
public class ContEconomii extends Cont {

    private BigDecimal dobandaAnuala; // De exemplu 0.05 inseamna 5% pe an.

    public ContEconomii(IBAN iban, Client titular, BigDecimal dobandaAnuala) {
        super(iban, titular);
        this.dobandaAnuala = dobandaAnuala == null ? BigDecimal.ZERO : dobandaAnuala;
    }

    public ContEconomii(IBAN iban, Client titular, BigDecimal soldInitial,
                        java.time.LocalDate dataDeschidere, BigDecimal dobandaAnuala) {
        super(iban, titular, soldInitial, dataDeschidere);
        this.dobandaAnuala = dobandaAnuala == null ? BigDecimal.ZERO : dobandaAnuala;
    }

    @Override
    public String getTipCont() {
        return "ContEconomii";
    }

    @Override
    public BigDecimal calculeazaComisionLunar() {
        return BigDecimal.ZERO;
    }

    // Calculeaza dobanda lunara pe baza soldului curent.
    public BigDecimal calculeazaDobandaLunara() {
        return sold.multiply(dobandaAnuala)
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDobandaAnuala() { return dobandaAnuala; }
    public void setDobandaAnuala(BigDecimal dobandaAnuala) { this.dobandaAnuala = dobandaAnuala; }
}
