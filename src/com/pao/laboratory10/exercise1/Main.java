package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "ENQUEUE":
                    coada.addLast(readTransaction(scanner));
                    break;
                case "PUSH":
                    coada.addFirst(readTransaction(scanner));
                    break;
                case "DEQUEUE":
                    removeFirst(coada, "Procesat");
                    break;
                case "POP":
                    removeFirst(coada, "Extras");
                    break;
                case "REMOVE_DEBIT":
                    removeDebit(coada);
                    break;
                case "REMOVE_BELOW":
                    removeBelow(coada, Double.parseDouble(scanner.next()));
                    break;
                case "PRINT":
                    printAll(coada);
                    break;
                case "SIZE":
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                default:
                    throw new IllegalArgumentException("Comandă necunoscută: " + command);
            }
        }
    }

    private static Tranzactie readTransaction(Scanner scanner) {
        return new Tranzactie(
                scanner.nextInt(),
                Double.parseDouble(scanner.next()),
                scanner.next(),
                TipTranzactie.valueOf(scanner.next())
        );
    }

    private static void removeFirst(LinkedList<Tranzactie> coada, String prefix) {
        if (coada.isEmpty()) {
            System.out.println("Coada goala.");
            return;
        }
        System.out.println(prefix + ": " + coada.removeFirst());
    }

    private static void removeDebit(LinkedList<Tranzactie> coada) {
        int count = 0;
        Iterator<Tranzactie> iterator = coada.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getTip() == TipTranzactie.DEBIT) {
                iterator.remove();
                count++;
            }
        }
        System.out.println("Eliminat " + count + " tranzactii DEBIT.");
    }

    private static void removeBelow(LinkedList<Tranzactie> coada, double threshold) {
        int count = 0;
        Iterator<Tranzactie> iterator = coada.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getSuma() < threshold) {
                iterator.remove();
                count++;
            }
        }
        System.out.printf(Locale.US, "Eliminat %d tranzactii sub %.2f RON.%n", count, threshold);
    }

    private static void printAll(Collection<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            System.out.println(tranzactie);
        }
    }
}
