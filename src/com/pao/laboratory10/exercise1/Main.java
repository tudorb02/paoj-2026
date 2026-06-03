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
                    removeFirst(coada, "Procesat: ");
                    break;
                case "POP":
                    removeFirst(coada, "Extras: ");
                    break;
                case "REMOVE_DEBIT":
                    int removedDebit = removeDebit(coada);
                    System.out.println("Eliminat " + removedDebit + " tranzactii DEBIT.");
                    break;
                case "REMOVE_BELOW":
                    double threshold = Double.parseDouble(scanner.next());
                    int removedBelow = removeBelow(coada, threshold);
                    System.out.printf(Locale.US, "Eliminat %d tranzactii sub %.2f RON.%n", removedBelow, threshold);
                    break;
                case "PRINT":
                    printAll(coada);
                    break;
                case "SIZE":
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                default:
                    break;
            }
        }
    }

    private static Tranzactie readTransaction(Scanner scanner) {
        int id = scanner.nextInt();
        double suma = Double.parseDouble(scanner.next());
        String data = scanner.next();
        TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
        return new Tranzactie(id, suma, data, tip);
    }

    private static void removeFirst(LinkedList<Tranzactie> coada, String prefix) {
        if (coada.isEmpty()) {
            System.out.println("Coada goala.");
        } else {
            System.out.println(prefix + coada.removeFirst());
        }
    }

    private static int removeDebit(LinkedList<Tranzactie> coada) {
        int count = 0;
        Iterator<Tranzactie> iterator = coada.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getTip() == TipTranzactie.DEBIT) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    private static int removeBelow(LinkedList<Tranzactie> coada, double threshold) {
        int count = 0;
        Iterator<Tranzactie> iterator = coada.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getSuma() < threshold) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    private static void printAll(List<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            System.out.println(tranzactie);
        }
    }
}
