package com.pao.proiect.banca.model;

import java.math.BigDecimal;

// Cont folosit pentru operatiile zilnice.
public class ContCurent extends Cont {

    private BigDecimal comisionLunar;

    public ContCurent(IBAN iban, Client titular, BigDecimal comisionLunar) {
        super(iban, titular);
        this.comisionLunar = comisionLunar == null ? BigDecimal.ZERO : comisionLunar;
    }

    public ContCurent(IBAN iban, Client titular, BigDecimal soldInitial,
                      java.time.LocalDate dataDeschidere, BigDecimal comisionLunar) {
        super(iban, titular, soldInitial, dataDeschidere);
        this.comisionLunar = comisionLunar == null ? BigDecimal.ZERO : comisionLunar;
    }

    @Override
    public String getTipCont() {
        return "ContCurent";
    }

    @Override
    public BigDecimal calculeazaComisionLunar() {
        return comisionLunar;
    }

    public BigDecimal getComisionLunar() { return comisionLunar; }
    public void setComisionLunar(BigDecimal comisionLunar) { this.comisionLunar = comisionLunar; }
}
