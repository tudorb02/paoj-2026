package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected String client;
    protected double pret;
    protected OrderState state;

    public Comanda(String nume, String client, double pret) {
        this.nume = nume;
        this.client = client;
        this.pret = pret;
        this.state = OrderState.PLACED;
    }

    public String getNume() {
        return nume;
    }

    public String getClient() {
        return client;
    }

    public abstract String getTip();

    public abstract double pretFinal();

    public abstract String descriere();

    public abstract String descriereScurta();
}
