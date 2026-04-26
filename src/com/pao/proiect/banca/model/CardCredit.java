package com.pao.proiect.banca.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Card de credit cu limita si dobanda.
public class CardCredit extends Card {

    private BigDecimal limitaCredit;
    private BigDecimal dobandaAnuala;

    public CardCredit(String numarCard, String cvv, LocalDate dataExpirare, Cont contAtasat,
                      BigDecimal limitaCredit, BigDecimal dobandaAnuala) {
        super(numarCard, cvv, dataExpirare, contAtasat);
        this.limitaCredit = limitaCredit == null ? BigDecimal.ZERO : limitaCredit;
        this.dobandaAnuala = dobandaAnuala == null ? BigDecimal.ZERO : dobandaAnuala;
    }

    @Override
    public String getTipCard() {
        return "CardCredit";
    }

    public BigDecimal getLimitaCredit() { return limitaCredit; }
    public void setLimitaCredit(BigDecimal limitaCredit) { this.limitaCredit = limitaCredit; }

    public BigDecimal getDobandaAnuala() { return dobandaAnuala; }
    public void setDobandaAnuala(BigDecimal dobandaAnuala) { this.dobandaAnuala = dobandaAnuala; }
}
