package com.pao.laboratory06.exercise1;

import java.util.Scanner;
import java.util.Locale;

public class Angajat implements Comparable<Angajat> {
    private String nume;
    private double salariu;

    public Angajat(String nume, double salariu) {
        this.nume = nume;
        this.salariu = salariu;
    }

    public static Angajat citeste(Scanner s) {
        String nume = s.next();
        double salariu = s.nextDouble();
        return new Angajat(nume, salariu);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s %.1f", nume, salariu);
    }

    public String getNume() {
        return nume;
    }

    public double getSalariu() {
        return salariu;
    }

    @Override
    public int compareTo(Angajat other) {
        return Double.compare(this.salariu, other.salariu);
    }
}
