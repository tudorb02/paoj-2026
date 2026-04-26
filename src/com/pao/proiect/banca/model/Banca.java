package com.pao.proiect.banca.model;

import java.util.ArrayList;
import java.util.List;

// Reprezinta banca si lista ei de sucursale.
public class Banca {

    private String cui;
    private String denumire;
    private List<Sucursala> sucursale;

    public Banca(String cui, String denumire) {
        this.cui = cui;
        this.denumire = denumire;
        this.sucursale = new ArrayList<>();
    }

    public String getCui() { return cui; }
    public void setCui(String cui) { this.cui = cui; }

    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) { this.denumire = denumire; }

    public List<Sucursala> getSucursale() { return sucursale; }

    public void adaugaSucursala(Sucursala s) { this.sucursale.add(s); }

    @Override
    public String toString() {
        return "Banca{cui='" + cui + "', denumire='" + denumire + "', nrSucursale=" + sucursale.size() + "}";
    }
}
