package com.pao.laboratory07.exercise2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Comanda> comenzi = new ArrayList<>();
        int nrStandard = 0;
        int nrDiscounted = 0;
        int nrGift = 0;
        double sumaStandard = 0.0;
        double sumaDiscounted = 0.0;

        for (int i = 0; i < n; i++) {
            String tip = sc.next();
            Comanda comanda = switch (tip) {
                case "STANDARD" -> new ComandaStandard(sc.next(), Double.parseDouble(sc.next()));
                case "DISCOUNTED" -> new ComandaRedusa(sc.next(), Double.parseDouble(sc.next()), sc.nextInt());
                case "GIFT" -> new ComandaGratuita(sc.next());
                default -> throw new IllegalArgumentException("Tip comandă invalid: " + tip);
            };

            comenzi.add(comanda);
            if (comanda instanceof ComandaStandard) {
                nrStandard++;
                sumaStandard += comanda.pretFinal();
            } else if (comanda instanceof ComandaRedusa) {
                nrDiscounted++;
                sumaDiscounted += comanda.pretFinal();
            } else if (comanda instanceof ComandaGratuita) {
                nrGift++;
            }
        }

        for (Comanda comanda : comenzi) {
            System.out.println(comanda.descriere());
        }

        System.out.println();
        System.out.println("Statistici:");
        if (nrStandard > 0) {
            System.out.printf(Locale.US, "STANDARD: suma = %.2f lei, numar = %d%n", sumaStandard, nrStandard);
        }
        if (nrDiscounted > 0) {
            System.out.printf(Locale.US, "DISCOUNTED: suma = %.2f lei, numar = %d%n", sumaDiscounted, nrDiscounted);
        }
        if (nrGift > 0) {
            System.out.printf(Locale.US, "GIFT: suma = 0.00 lei, numar = %d%n", nrGift);
        }
        System.out.printf(Locale.US, "Total platit: %.2f lei%n", sumaStandard + sumaDiscounted);
    }
}
