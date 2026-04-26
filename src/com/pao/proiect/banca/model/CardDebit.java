package com.pao.proiect.banca.model;

import java.time.LocalDate;

// Card de debit legat direct de soldul contului.
public class CardDebit extends Card {

    public CardDebit(String numarCard, String cvv, LocalDate dataExpirare, Cont contAtasat) {
        super(numarCard, cvv, dataExpirare, contAtasat);
    }

    @Override
    public String getTipCard() {
        return "CardDebit";
    }
}
