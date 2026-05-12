package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            tranzactii.add(new Tranzactie(
                    scanner.nextInt(),
                    Double.parseDouble(scanner.next()),
                    scanner.next(),
                    TipTranzactie.valueOf(scanner.next())
            ));
        }

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "UNIQUE_IDS":
                    uniqueIds(tranzactii);
                    break;
                case "MONTHLY_REPORT":
                    monthlyReport(tranzactii);
                    break;
                case "TOP":
                    top(tranzactii, scanner.nextInt());
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
                    minMax(tranzactii);
                    break;
                case "CME_DEMO":
                    cmeDemo(tranzactii);
                    break;
                default:
                    throw new IllegalArgumentException("Comandă necunoscută: " + command);
            }
        }
    }

    private static void uniqueIds(List<Tranzactie> tranzactii) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        for (Tranzactie tranzactie : tranzactii) {
            ids.add(tranzactie.getId());
        }
        System.out.println("IDs unice (" + ids.size() + "): " + ids);
    }

    private static void monthlyReport(List<Tranzactie> tranzactii) {
        TreeMap<String, double[]> report = new TreeMap<>();
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
            System.out.printf(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                    entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
        }
    }

    private static void top(List<Tranzactie> tranzactii, int n) {
        List<Tranzactie> copy = new ArrayList<>(tranzactii);
        copy.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
        System.out.println("Top " + n + ":");
        for (int i = 0; i < Math.min(n, copy.size()); i++) {
            System.out.println(copy.get(i));
        }
    }

    private static void minMax(List<Tranzactie> tranzactii) {
        Comparator<Tranzactie> comparator = Comparator.comparingDouble(Tranzactie::getSuma);
        System.out.println("MIN: " + Collections.min(tranzactii, comparator));
        System.out.println("MAX: " + Collections.max(tranzactii, comparator));
    }

    private static void cmeDemo(List<Tranzactie> tranzactii) {
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
