package com.pao.laboratory06.exercise2;

import java.util.Locale;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;
    protected TipColaborator tip;

    public abstract double calculeazaVenitNetAnual();

    public TipColaborator getTip() {
        return tip;
    }

    @Override
    public String tipContract() {
        return tip.name();
    }

    @Override
    public void afiseaza() {
        System.out.printf(Locale.US, "%s: %s %s, venit net anual: %.2f lei%n",
                tip, nume, prenume, calculeazaVenitNetAnual());
    }
}
