package com.pao.proiect.banca.dto;

import java.math.BigDecimal;

public class ClientConturiReport {

    private final String cnp;
    private final String numeComplet;
    private final int numarConturi;
    private final BigDecimal soldTotal;

    public ClientConturiReport(String cnp, String numeComplet, int numarConturi, BigDecimal soldTotal) {
        this.cnp = cnp;
        this.numeComplet = numeComplet;
        this.numarConturi = numarConturi;
        this.soldTotal = soldTotal;
    }

    @Override
    public String toString() {
        return "ClientConturiReport{" +
                "cnp='" + cnp + '\'' +
                ", numeComplet='" + numeComplet + '\'' +
                ", numarConturi=" + numarConturi +
                ", soldTotal=" + soldTotal +
                '}';
    }
}

