package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Tranzactie tranzactie = new Tranzactie(
                    scanner.nextInt(),
                    Double.parseDouble(scanner.next()),
                    scanner.next(),
                    scanner.next(),
                    scanner.next(),
                    TipTranzactie.valueOf(scanner.next())
            );
            tranzactie.setNote("procesat");
            tranzactii.add(tranzactie);
        }

        new File("output").mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            out.writeObject(tranzactii);
        }

        List<Tranzactie> restored;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            restored = (List<Tranzactie>) in.readObject();
        }

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "LIST":
                    printAll(restored);
                    break;
                case "FILTER":
                    filterByMonth(restored, scanner.next());
                    break;
                case "NOTE":
                    printNote(restored, scanner.nextInt());
                    break;
                default:
                    throw new IllegalArgumentException("Comandă necunoscută: " + command);
            }
        }
    }

    private static void printAll(List<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            System.out.println(tranzactie);
        }
    }

    private static void filterByMonth(List<Tranzactie> tranzactii, String month) {
        boolean found = false;
        for (Tranzactie tranzactie : tranzactii) {
            if (tranzactie.getData().startsWith(month)) {
                System.out.println(tranzactie);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Niciun rezultat.");
        }
    }

    private static void printNote(List<Tranzactie> tranzactii, int id) {
        for (Tranzactie tranzactie : tranzactii) {
            if (tranzactie.getId() == id) {
                System.out.println("NOTE[" + id + "]: " + tranzactie.getNote());
                return;
            }
        }
        System.out.println("NOTE[" + id + "]: not found");
    }
}
