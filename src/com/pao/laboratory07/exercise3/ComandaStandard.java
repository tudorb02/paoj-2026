package com.pao.laboratory07.exercise3;

import java.util.Locale;

public final class ComandaStandard extends Comanda {
    public ComandaStandard(String nume, double pret, String client) {
        super(nume, client, pret);
    }

    @Override
    public String getTip() {
        return "STANDARD";
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return String.format(Locale.US, "STANDARD: %s, pret: %.2f lei [%s] - client: %s",
                nume, pretFinal(), state, client);
    }

    @Override
    public String descriereScurta() {
        return String.format(Locale.US, "STANDARD: %s, pret: %.2f lei - client: %s", nume, pretFinal(), client);
    }
}
