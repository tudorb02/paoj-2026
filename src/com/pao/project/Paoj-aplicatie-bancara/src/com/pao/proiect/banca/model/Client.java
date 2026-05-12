package com.pao.proiect.banca.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// Reprezinta un client al bancii.
public class Client extends Persoana {

    private LocalDate dataInregistrare;
    private Set<Cont> conturi;

    public Client(String nume, String prenume, String cnp, String email, String telefon) {
        super(nume, prenume, cnp, email, telefon);
        this.dataInregistrare = LocalDate.now();
        this.conturi = new HashSet<>();
    }

    public Client(String nume, String prenume, String cnp, String email, String telefon,
                  LocalDate dataInregistrare) {
        super(nume, prenume, cnp, email, telefon);
        this.dataInregistrare = dataInregistrare;
        this.conturi = new HashSet<>();
    }

    @Override
    public String getRol() {
        return "Client";
    }

    public LocalDate getDataInregistrare() { return dataInregistrare; }
    public void setDataInregistrare(LocalDate dataInregistrare) { this.dataInregistrare = dataInregistrare; }

    // Intoarce conturile clientului fara a expune colectia interna.
    public Set<Cont> getConturi() { return Collections.unmodifiableSet(conturi); }

    public void adaugaCont(Cont cont) {
        this.conturi.add(cont);
    }

    public boolean stergeCont(Cont cont) {
        return this.conturi.remove(cont);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(cnp, client.cnp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnp);
    }

    @Override
    public String toString() {
        return "Client{" +
                "nume='" + nume + " " + prenume + '\'' +
                ", cnp='" + cnp + '\'' +
                ", email='" + email + '\'' +
                ", dataInregistrare=" + dataInregistrare +
                ", nrConturi=" + conturi.size() +
                '}';
    }
}
