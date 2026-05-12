package com.pao.laboratory06.exercise2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator colaborator = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            colaborator.citeste(in);
            colaboratori.add(colaborator);
        }

        Comparator<Colaborator> byNetDesc = (a, b) ->
                Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual());

        for (TipColaborator tip : TipColaborator.values()) {
            colaboratori.stream()
                    .filter(c -> c.getTip() == tip)
                    .sorted(byNetDesc)
                    .forEach(Colaborator::afiseaza);
        }

        Colaborator max = colaboratori.stream()
                .max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual))
                .orElse(null);
        System.out.print("\nColaborator cu venit net maxim: ");
        if (max != null) {
            max.afiseaza();
        }

        System.out.println("\nColaboratori persoane juridice:");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .sorted(byNetDesc)
                .forEach(Colaborator::afiseaza);

        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);
        for (Colaborator colaborator : colaboratori) {
            TipColaborator tip = colaborator.getTip();
            suma.put(tip, suma.getOrDefault(tip, 0.0) + colaborator.calculeazaVenitNetAnual());
            numar.put(tip, numar.getOrDefault(tip, 0) + 1);
        }

        System.out.println("\nSume și număr colaboratori pe tip:");
        for (TipColaborator tip : TipColaborator.values()) {
            if (suma.containsKey(tip)) {
                System.out.printf(Locale.US, "%s: suma = %.2f lei, număr = %d%n",
                        tip, suma.get(tip), numar.get(tip));
            } else {
                System.out.println(tip + ": suma = nu lei, număr = null");
            }
        }
    }
}
