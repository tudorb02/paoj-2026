package com.pao.proiect.banca.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Reprezinta un manager de sucursala.
public class Manager extends Angajat {

    private BigDecimal bonus;
    private List<Angajat> echipa;

    public Manager(String nume, String prenume, String cnp, String email, String telefon,
                   BigDecimal salariu, Sucursala sucursala, BigDecimal bonus) {
        super(nume, prenume, cnp, email, telefon, salariu, sucursala);
        this.bonus = bonus;
        this.echipa = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "Manager";
    }

    public BigDecimal getBonus() { return bonus; }
    public void setBonus(BigDecimal bonus) { this.bonus = bonus; }

    public List<Angajat> getEchipa() { return echipa; }

    public void adaugaInEchipa(Angajat a) {
        this.echipa.add(a);
    }

    public BigDecimal getSalariuTotal() {
        return salariu.add(bonus == null ? BigDecimal.ZERO : bonus);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "nume='" + nume + " " + prenume + '\'' +
                ", cnp='" + cnp + '\'' +
                ", salariuTotal=" + getSalariuTotal() +
                ", sucursala=" + (sucursala != null ? sucursala.getDenumire() : "-") +
                ", marimeEchipa=" + echipa.size() +
                '}';
    }
}
