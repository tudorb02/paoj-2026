package com.pao.laboratory14.exercise1;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Bilet> bilete = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            String eveniment = scanner.next();
            TipBilet tip = TipBilet.valueOf(scanner.next());
            double pret = Double.parseDouble(scanner.next());
            bilete.add(new Bilet(id, eveniment, tip, pret));
        }

        String command = scanner.next();
        RaportVanzari raport = bilete.stream().collect(RaportVanzariCollector.toRaportVanzari());
        printSimpleReport(raport);

        if ("RAPORT_COMPLET".equals(command)) {
            System.out.println("---");
            System.out.printf(Locale.US, "Total: %.2f RON%n", raport.getTotalGlobal());
            System.out.printf(Locale.US, "Medie: %.2f RON%n", raport.getMedieGlobala());
            System.out.println("Cel mai popular: " + raport.getTipCelMaiPopular());
        }
    }

    private static void printSimpleReport(RaportVanzari raport) {
        for (TipBilet tip : TipBilet.values()) {
            Long count = raport.getNumarPerTip().get(tip);
            if (count != null && count > 0) {
                double revenue = raport.getIncasariPerTip().getOrDefault(tip, 0.0);
                System.out.printf(Locale.US, "%s: count=%d incasari=%.2f RON%n", tip, count, revenue);
            }
        }
    }
}
