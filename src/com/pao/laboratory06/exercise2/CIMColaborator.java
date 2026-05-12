package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        this.tip = TipColaborator.CIM;
    }

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        bonus = in.hasNext("DA|NU") && in.next().equals("DA");
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        return bonus ? net * 1.10 : net;
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }
}
