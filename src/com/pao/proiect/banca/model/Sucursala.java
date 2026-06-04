package com.pao.proiect.banca.model;

import java.util.Objects;

// Reprezinta o sucursala a bancii.
public class Sucursala {

    private int id;
    private String denumire;
    private String adresa;

    public Sucursala(int id, String denumire, String adresa) {
        this.id = id;
        this.denumire = denumire;
        this.adresa = adresa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) { this.denumire = denumire; }

    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sucursala)) return false;
        Sucursala that = (Sucursala) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Sucursala{id=" + id + ", denumire='" + denumire + "', adresa='" + adresa + "'}";
    }
}
