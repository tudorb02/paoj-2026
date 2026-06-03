package com.pao.laboratory11.exercise3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collector;

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
                new Transaction(1, 1200.00, LocalDate.of(2026, 5, 1), "RO", "WEB"),
                new Transaction(2, 80.00, LocalDate.of(2026, 5, 2), "RO", "ATM"),
                new Transaction(3, 5000.00, LocalDate.of(2026, 5, 3), "NG", "CRYPTO"),
                new Transaction(4, 750.00, LocalDate.of(2026, 6, 4), "DE", "APP"),
                new Transaction(5, 2200.00, LocalDate.of(2026, 6, 5), "RO", "WEB"),
                new Transaction(6, 300.00, LocalDate.of(2026, 6, 6), "FR", "POS"),
                new Transaction(7, 4100.00, LocalDate.of(2026, 7, 7), "IR", "CRYPTO")
        );

        Snapshot snapshot = transactions.stream().collect(toSnapshot(3));

        System.out.println("=== Top tranzactii ===");
        snapshot.topTransactions().forEach(System.out::println);

        System.out.println("\n=== Count pe tara ===");
        snapshot.countByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        System.out.println("\n=== Count pe canal ===");
        snapshot.countByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        System.out.printf(Locale.US, "%nTotal suma: %.2f RON%n", snapshot.totalAmount());
    }

    private static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Accumulator {
            private final Map<String, Long> countByCountry = new HashMap<>();
            private final Map<String, Long> countByChannel = new HashMap<>();
            private final List<Transaction> all = new ArrayList<>();
            private double totalAmount;

            private void add(Transaction tx) {
                countByCountry.merge(tx.country(), 1L, Long::sum);
                countByChannel.merge(tx.channel(), 1L, Long::sum);
                all.add(tx);
                totalAmount += tx.amount();
            }

            private Accumulator combine(Accumulator other) {
                other.countByCountry.forEach((key, value) -> countByCountry.merge(key, value, Long::sum));
                other.countByChannel.forEach((key, value) -> countByChannel.merge(key, value, Long::sum));
                all.addAll(other.all);
                totalAmount += other.totalAmount;
                return this;
            }

            private Snapshot finish() {
                List<Transaction> top = all.stream()
                        .sorted(Comparator.comparingDouble(Transaction::amount).reversed()
                                .thenComparingInt(Transaction::id))
                        .limit(topN)
                        .toList();
                return new Snapshot(Map.copyOf(countByCountry), Map.copyOf(countByChannel), totalAmount, top);
            }
        }

        return Collector.of(Accumulator::new, Accumulator::add, Accumulator::combine, Accumulator::finish);
    }

    private record Transaction(int id, double amount, LocalDate date, String country, String channel) {
        @Override
        public String toString() {
            return String.format(Locale.US, "[%d] %s %s %s %.2f RON", id, date, country, channel, amount);
        }
    }

    private record Snapshot(
            Map<String, Long> countByCountry,
            Map<String, Long> countByChannel,
            double totalAmount,
            List<Transaction> topTransactions) {
    }
}
