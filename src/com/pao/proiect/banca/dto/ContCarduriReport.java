package com.pao.proiect.banca.dto;

import java.math.BigDecimal;

public class ContCarduriReport {

    private final String iban;
    private final String tipCont;
    private final String titular;
    private final BigDecimal sold;
    private final int numarCarduri;

    public ContCarduriReport(String iban, String tipCont, String titular,
                             BigDecimal sold, int numarCarduri) {
        this.iban = iban;
        this.tipCont = tipCont;
        this.titular = titular;
        this.sold = sold;
        this.numarCarduri = numarCarduri;
    }

    @Override
    public String toString() {
        return "ContCarduriReport{" +
                "iban='" + iban + '\'' +
                ", tipCont='" + tipCont + '\'' +
                ", titular='" + titular + '\'' +
                ", sold=" + sold +
                ", numarCarduri=" + numarCarduri +
                '}';
    }
}

