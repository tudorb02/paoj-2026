package com.pao.proiect.banca.model;

import java.math.BigDecimal;

// Reprezinta un angajat al bancii.
public class Angajat extends Persoana {

    protected BigDecimal salariu;
    protected Sucursala sucursala;

    public Angajat(String nume, String prenume, String cnp, String email, String telefon,
                   BigDecimal salariu, Sucursala sucursala) {
        super(nume, prenume, cnp, email, telefon);
        this.salariu = salariu;
        this.sucursala = sucursala;
    }

    @Override
    public String getRol() {
        return "Angajat";
    }

    public BigDecimal getSalariu() { return salariu; }
    public void setSalariu(BigDecimal salariu) { this.salariu = salariu; }

    public Sucursala getSucursala() { return sucursala; }
    public void setSucursala(Sucursala sucursala) { this.sucursala = sucursala; }

    @Override
    public String toString() {
        return "Angajat{" +
                "nume='" + nume + " " + prenume + '\'' +
                ", cnp='" + cnp + '\'' +
                ", salariu=" + salariu +
                ", sucursala=" + (sucursala != null ? sucursala.getDenumire() : "-") +
                '}';
    }
}
