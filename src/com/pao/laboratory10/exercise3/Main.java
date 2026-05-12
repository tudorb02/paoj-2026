package com.pao.laboratory10.exercise3;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<TranzactieDemo> tranzactii = List.of(
                new TranzactieDemo(1, 1500.00, "2024-01-15", "CREDIT", "CONT_A"),
                new TranzactieDemo(2, 750.50, "2024-01-22", "DEBIT", "CONT_B"),
                new TranzactieDemo(3, 200.00, "2024-02-05", "CREDIT", "CONT_A"),
                new TranzactieDemo(4, 1200.00, "2024-02-18", "DEBIT", "CONT_C"),
                new TranzactieDemo(5, 500.00, "2024-03-10", "CREDIT", "CONT_D"),
                new TranzactieDemo(6, 300.00, "2024-03-22", "DEBIT", "CONT_B"),
                new TranzactieDemo(7, 900.00, "2024-01-30", "CREDIT", "CONT_A"),
                new TranzactieDemo(8, 85.00, "2024-02-21", "DEBIT", "CONT_E"),
                new TranzactieDemo(9, 2300.00, "2024-03-28", "CREDIT", "CONT_F"),
                new TranzactieDemo(10, 640.00, "2024-04-03", "DEBIT", "CONT_A")
        );

        System.out.println("=== 1. Tranzactii CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.tip().equals("CREDIT"))
                .forEach(System.out::println);

        System.out.println("\n=== 2. Total procesat ===");
        double total = tranzactii.stream().mapToDouble(TranzactieDemo::suma).sum();
        System.out.printf(Locale.US, "Total procesat: %.2f RON%n", total);

        System.out.println("\n=== 3. Total pe luna ===");
        Map<String, Double> totalPeLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.data().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(TranzactieDemo::suma)
                ));
        totalPeLuna.forEach((luna, suma) -> System.out.printf(Locale.US, "%s: %.2f RON%n", luna, suma));

        System.out.println("\n=== 4. Top 3 tranzactii ===");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(TranzactieDemo::suma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n=== 5. Conturi sursa unice ===");
        List<String> conturi = tranzactii.stream()
                .map(TranzactieDemo::contSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturi);

        System.out.println("\n=== 6. Suma medie ===");
        double medie = tranzactii.stream().mapToDouble(TranzactieDemo::suma).average().orElse(0.0);
        System.out.printf(Locale.US, "Suma medie: %.2f RON%n", medie);

        System.out.println("\n=== 7. Extrase lunare ===");
        Map<String, List<TranzactieDemo>> peLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(t -> t.data().substring(0, 7), TreeMap::new, Collectors.toList()));
        peLuna.forEach((luna, lista) -> {
            double suma = lista.stream().mapToDouble(TranzactieDemo::suma).sum();
            System.out.printf(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lista.size(), suma);
        });
    }

    private record TranzactieDemo(int id, double suma, String data, String tip, String contSursa) {
        @Override
        public String toString() {
            return String.format(Locale.US, "[%d] %s %s: %.2f RON | %s", id, data, tip, suma, contSursa);
        }
    }
}
