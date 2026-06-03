package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = Double.parseDouble(scanner.next());
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
            tranzactii.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "UNIQUE_IDS":
                    printUniqueIds(tranzactii);
                    break;
                case "MONTHLY_REPORT":
                    printMonthlyReport(tranzactii);
                    break;
                case "TOP":
                    printTop(tranzactii, scanner.nextInt());
                    break;
                case "SORT_ASC":
                    tranzactii.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    printAll(tranzactii);
                    break;
                case "SORT_DESC":
                    tranzactii.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    printAll(tranzactii);
                    break;
                case "REVERSE":
                    Collections.reverse(tranzactii);
                    printAll(tranzactii);
                    break;
                case "MIN_MAX":
                    printMinMax(tranzactii);
                    break;
                case "CME_DEMO":
                    demoConcurrentModification(tranzactii);
                    break;
                default:
                    break;
            }
        }
    }

    private static void printUniqueIds(List<Tranzactie> tranzactii) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        for (Tranzactie tranzactie : tranzactii) {
            ids.add(tranzactie.getId());
        }
        System.out.println("IDs unice (" + ids.size() + "): " + ids);
    }

    private static void printMonthlyReport(List<Tranzactie> tranzactii) {
        Map<String, double[]> report = new TreeMap<>();
        for (Tranzactie tranzactie : tranzactii) {
            String month = tranzactie.getData().substring(0, 7);
            double[] sums = report.computeIfAbsent(month, key -> new double[2]);
            if (tranzactie.getTip() == TipTranzactie.CREDIT) {
                sums[0] += tranzactie.getSuma();
            } else {
                sums[1] += tranzactie.getSuma();
            }
        }

        for (Map.Entry<String, double[]> entry : report.entrySet()) {
            double[] sums = entry.getValue();
            System.out.printf(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON%n", entry.getKey(), sums[0], sums[1]);
        }
    }

    private static void printTop(List<Tranzactie> tranzactii, int n) {
        List<Tranzactie> copy = new ArrayList<>(tranzactii);
        copy.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
        int limit = Math.min(n, copy.size());
        System.out.println("Top " + n + ":");
        printAll(copy.subList(0, limit));
    }

    private static void printMinMax(List<Tranzactie> tranzactii) {
        if (tranzactii.isEmpty()) {
            return;
        }

        Comparator<Tranzactie> byAmount = Comparator.comparingDouble(Tranzactie::getSuma);
        System.out.println("MIN: " + Collections.min(tranzactii, byAmount));
        System.out.println("MAX: " + Collections.max(tranzactii, byAmount));
    }

    private static void demoConcurrentModification(List<Tranzactie> tranzactii) {
        try {
            for (Tranzactie tranzactie : tranzactii) {
                tranzactii.remove(tranzactie);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
        }
    }

    private static void printAll(List<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            System.out.println(tranzactie);
        }
    }
}
