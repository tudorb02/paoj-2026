package com.pao.laboratory07.exercise3;

import java.util.Locale;

public final class ComandaRedusa extends Comanda {
    private int discountProcent;

    public ComandaRedusa(String nume, double pret, int discountProcent, String client) {
        super(nume, client, pret);
        this.discountProcent = discountProcent;
    }

    public int getDiscountProcent() {
        return discountProcent;
    }

    @Override
    public String getTip() {
        return "DISCOUNTED";
    }

    @Override
    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }

    @Override
    public String descriere() {
        return String.format(Locale.US, "DISCOUNTED: %s, pret: %.2f lei (-%d%%) [%s] - client: %s",
                nume, pretFinal(), discountProcent, state, client);
    }

    @Override
    public String descriereScurta() {
        return String.format(Locale.US, "DISCOUNTED: %s, pret: %.2f lei - client: %s", nume, pretFinal(), client);
    }

    public String descriereSpeciala() {
        return String.format(Locale.US, "DISCOUNTED: %s, pret: %.2f lei (-%d%%) - client: %s",
                nume, pretFinal(), discountProcent, client);
    }
}
