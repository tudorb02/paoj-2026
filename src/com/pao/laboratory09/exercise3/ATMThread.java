package com.pao.laboratory09.exercise3;

public class ATMThread extends Thread {
    private CoadaTranzactii coada;
    private int atmId;

    public ATMThread(CoadaTranzactii coada, int atmId) {
        this.coada = coada;
        this.atmId = atmId;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 4; i++) {
            try {
                int id = atmId * 100 + i;
                Tranzactie tranzactie = new Tranzactie(id, 100.0 * i + atmId, "2024-05-01");
                System.out.println("[ATM-" + atmId + "] trimite: " + tranzactie);
                coada.adauga(tranzactie, atmId);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
