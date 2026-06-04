package com.pao.laboratory07.exercise2;

import java.util.Locale;

public final class ComandaGratuita extends Comanda {
    public ComandaGratuita(String nume) {
        super(nume, 0.0);
    }

    @Override
    public double pretFinal() {
        return 0.0;
    }

    @Override
    public String descriere() {
        return String.format(Locale.US, "GIFT: %s, gratuit [%s]", nume, state);
    }
}
