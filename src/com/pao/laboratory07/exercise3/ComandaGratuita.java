package com.pao.laboratory07.exercise3;

import java.util.Locale;

public final class ComandaGratuita extends Comanda {
    public ComandaGratuita(String nume, String client) {
        super(nume, client, 0.0);
    }

    @Override
    public String getTip() {
        return "GIFT";
    }

    @Override
    public double pretFinal() {
        return 0.0;
    }

    @Override
    public String descriere() {
        return String.format(Locale.US, "GIFT: %s, gratuit [%s] - client: %s", nume, state, client);
    }

    @Override
    public String descriereScurta() {
        return "GIFT: " + nume + ", gratuit - client: " + client;
    }
}
