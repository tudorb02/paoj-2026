package com.pao.proiect.banca.model;

import com.pao.proiect.banca.exception.FonduriInsuficienteException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

// Clasa de baza pentru toate tipurile de cont.
public abstract class Cont implements Tranzactionabil {

    protected IBAN iban;
    protected BigDecimal sold;
    protected LocalDate dataDeschidere;
    protected Client titular;

    protected Cont(IBAN iban, Client titular) {
        this.iban = iban;
        this.titular = titular;
        this.sold = BigDecimal.ZERO;
        this.dataDeschidere = LocalDate.now();
    }

    protected Cont(IBAN iban, Client titular, BigDecimal soldInitial, LocalDate dataDeschidere) {
        this.iban = iban;
        this.titular = titular;
        this.sold = soldInitial == null ? BigDecimal.ZERO : soldInitial;
        this.dataDeschidere = dataDeschidere == null ? LocalDate.now() : dataDeschidere;
    }

    public abstract String getTipCont();

    public abstract BigDecimal calculeazaComisionLunar();

    @Override
    public void depune(BigDecimal suma) {
        validareSuma(suma);
        this.sold = this.sold.add(suma);
    }

    @Override
    public void retrage(BigDecimal suma) {
        validareSuma(suma);
        if (this.sold.compareTo(suma) < 0) {
            throw new FonduriInsuficienteException(iban.getValoare(), this.sold, suma);
        }
        this.sold = this.sold.subtract(suma);
    }

    private static void validareSuma(BigDecimal suma) {
        if (suma == null) {
            throw new IllegalArgumentException("Suma nu poate fi null.");
        }
        if (suma.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Suma trebuie sa fie strict pozitiva. Primit: " + suma);
        }
    }

    public IBAN getIban() { return iban; }

    public BigDecimal getSold() { return sold; }

    public LocalDate getDataDeschidere() { return dataDeschidere; }

    public Client getTitular() { return titular; }

    public void setTitular(Client titular) { this.titular = titular; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cont)) return false;
        Cont cont = (Cont) o;
        return Objects.equals(iban, cont.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    @Override
    public String toString() {
        return getTipCont() + "{" +
                "iban=" + iban +
                ", sold=" + sold +
                ", titular=" + (titular != null ? titular.getNume() + " " + titular.getPrenume() : "-") +
                ", dataDeschidere=" + dataDeschidere +
                '}';
    }
}
