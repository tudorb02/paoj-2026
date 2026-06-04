package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private static final double SALARIU_MINIM_BRUT = 4050.0;
    private double cheltuieliLunare;

    public PFAColaborator() {
        this.tip = TipColaborator.PFA;
    }

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;
        double impozit = venitNet * 0.10;
        double cass = SALARIU_MINIM_BRUT * 12 * 0.10;
        double cas = SALARIU_MINIM_BRUT * 24 * 0.25;
        return venitNet - impozit - cass - cas;
    }
}
