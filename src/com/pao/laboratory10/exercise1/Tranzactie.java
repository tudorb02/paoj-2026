package com.pao.laboratory10.exercise1;

import java.util.Locale;

public class Tranzactie {
    private final int id;
    private final double suma;
    private final String data;
    private final TipTranzactie tip;

    public Tranzactie(int id, double suma, String data, TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public double getSuma() {
        return suma;
    }

    public String getData() {
        return data;
    }

    public TipTranzactie getTip() {
        return tip;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "[%d] %s %s: %.2f RON", id, data, tip, suma);
    }
}
