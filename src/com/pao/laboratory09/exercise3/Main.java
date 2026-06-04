package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CoadaTranzactii coada = new CoadaTranzactii();
        ProcessorThread processor = new ProcessorThread(coada);
        Thread processorThread = new Thread(processor);

        ATMThread atm1 = new ATMThread(coada, 1);
        ATMThread atm2 = new ATMThread(coada, 2);
        ATMThread atm3 = new ATMThread(coada, 3);

        processorThread.start();
        atm1.start();
        atm2.start();
        atm3.start();

        atm1.join();
        atm2.join();
        atm3.join();

        coada.inchide();
        processorThread.join();

        System.out.println("Toate tranzactiile procesate. Total: " + processor.getTotalProcesate());
    }
}
