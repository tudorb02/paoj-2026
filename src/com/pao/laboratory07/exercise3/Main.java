package com.pao.laboratory07.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = scanner.next();
            Comanda comanda = switch (tip) {
                case "STANDARD" -> new ComandaStandard(scanner.next(), Double.parseDouble(scanner.next()), scanner.next());
                case "DISCOUNTED" -> new ComandaRedusa(scanner.next(), Double.parseDouble(scanner.next()), scanner.nextInt(), scanner.next());
                case "GIFT" -> new ComandaGratuita(scanner.next(), scanner.next());
                default -> throw new IllegalArgumentException("Tip comandă invalid: " + tip);
            };
            comenzi.add(comanda);
            System.out.println(comanda.descriere());
        }

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "STATS":
                    printStats(comenzi);
                    break;
                case "FILTER":
                    printFilter(comenzi, Double.parseDouble(scanner.next()));
                    break;
                case "SORT":
                    printSort(comenzi);
                    break;
                case "SPECIAL":
                    printSpecial(comenzi);
                    break;
                case "QUIT":
                    return;
                default:
                    throw new IllegalArgumentException("Comandă invalidă: " + command);
            }
        }
    }

    private static void printStats(List<Comanda> comenzi) {
        System.out.println("\n--- STATS ---");
        Map<String, Double> averages = comenzi.stream()
                .collect(Collectors.groupingBy(
                        Comanda::getTip,
                        LinkedHashMap::new,
                        Collectors.averagingDouble(Comanda::pretFinal)
                ));

        for (String tip : List.of("STANDARD", "DISCOUNTED", "GIFT")) {
            if (averages.containsKey(tip)) {
                System.out.printf(Locale.US, "%s: medie = %.2f lei%n", tip, averages.get(tip));
            }
        }
    }

    private static void printFilter(List<Comanda> comenzi, double threshold) {
        System.out.printf(Locale.US, "%n--- FILTER (>= %.2f) ---%n", threshold);
        comenzi.stream()
                .filter(comanda -> comanda.pretFinal() >= threshold)
                .forEach(comanda -> System.out.println(comanda.descriereScurta()));
    }

    private static void printSort(List<Comanda> comenzi) {
        System.out.println("\n--- SORT (by client, then by pret) ---");
        comenzi.stream()
                .sorted(Comparator.comparing(Comanda::getClient).thenComparingDouble(Comanda::pretFinal))
                .forEach(comanda -> System.out.println(comanda.descriereScurta()));
    }

    private static void printSpecial(List<Comanda> comenzi) {
        System.out.println("\n--- SPECIAL (discount > 15%) ---");
        comenzi.stream()
                .filter(comanda -> comanda instanceof ComandaRedusa redusa && redusa.getDiscountProcent() > 15)
                .map(comanda -> (ComandaRedusa) comanda)
                .forEach(redusa -> System.out.println(redusa.descriereSpeciala()));
    }
}
