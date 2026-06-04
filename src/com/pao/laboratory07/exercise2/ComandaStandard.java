package com.pao.laboratory07.exercise2;

import java.util.Locale;

public final class ComandaStandard extends Comanda {
    public ComandaStandard(String nume, double pret) {
        super(nume, pret);
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return String.format(Locale.US, "STANDARD: %s, pret: %.2f lei [%s]", nume, pretFinal(), state);
    }
}
