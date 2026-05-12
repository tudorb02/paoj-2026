package com.pao.proiect.banca.model;

// Clasa de baza pentru persoanele din sistem.
public abstract class Persoana {

    protected String nume;
    protected String prenume;
    protected String cnp;
    protected String email;
    protected String telefon;

    protected Persoana(String nume, String prenume, String cnp, String email, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.cnp = cnp;
        this.email = email;
        this.telefon = telefon;
    }

    // Intoarce rolul persoanei din sistem.
    public abstract String getRol();

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getCnp() { return cnp; }
    public void setCnp(String cnp) { this.cnp = cnp; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    @Override
    public String toString() {
        return getRol() + "{" +
                "nume='" + nume + " " + prenume + '\'' +
                ", cnp='" + cnp + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                '}';
    }
}
