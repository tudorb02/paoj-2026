package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private static final int CAPACITATE = 5;
    private Queue<Tranzactie> tranzactii = new LinkedList<>();
    private boolean inchisa;

    public synchronized void adauga(Tranzactie tranzactie, int atmId) throws InterruptedException {
        while (tranzactii.size() == CAPACITATE) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }
        tranzactii.add(tranzactie);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (tranzactii.isEmpty() && !inchisa) {
            wait();
        }
        if (tranzactii.isEmpty()) {
            return null;
        }
        Tranzactie tranzactie = tranzactii.poll();
        notifyAll();
        return tranzactie;
    }

    public synchronized void inchide() {
        inchisa = true;
        notifyAll();
    }
}
