package com.pao.laboratory09.exercise3;

public class ProcessorThread implements Runnable {
    private CoadaTranzactii coada;
    private int totalProcesate;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Tranzactie tranzactie = coada.extrage();
                if (tranzactie == null) {
                    return;
                }
                Thread.sleep(80);
                totalProcesate++;
                System.out.println("[Processor] Factura #" + tranzactie.id() + " - "
                        + tranzactie.suma() + " RON | " + tranzactie.data());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public int getTotalProcesate() {
        return totalProcesate;
    }
}
