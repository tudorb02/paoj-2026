package com.pao.laboratory14.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Bonus — Alocare Automata de Sali pentru Evenimente
 * <p>
 * Problema clasica de interviu: date N evenimente cu intervale [start, end],
 * gaseste numarul minim de sali necesare si atribuie fiecare eveniment la o sala.
 * <p>
 * Doua variante demonstrate:
 * Varianta 1 — greedy simplu O(N^2): prima sala disponibila
 * Varianta 2 — PriorityQueue O(N log N): min-heap de ore de final
 */
public class Main {

    record Eveniment(String nume, int startMin, int endMin) {
    }

    /**
     * Converteste "HH:MM" in minute intregi de la miezul noptii.
     */
    private static int toMin(String hhmm) {
        String[] p = hhmm.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    /**
     * Converteste minute intregi inapoi in "HH:MM".
     */
    private static String toHHMM(int min) {
        return String.format("%02d:%02d", min / 60, min % 60);
    }

    public static void main(String[] args) {
        List<Eveniment> evenimente = List.of(
                new Eveniment("ConcertRock", toMin("09:00"), toMin("11:00")),
                new Eveniment("TechTalk", toMin("09:30"), toMin("10:30")),
                new Eveniment("GalaVIP", toMin("10:00"), toMin("12:00")),
                new Eveniment("WorkshopJava", toMin("11:00"), toMin("13:00")),
                new Eveniment("MeetAndGreet", toMin("11:30"), toMin("12:30")),
                new Eveniment("ComedyNight", toMin("12:30"), toMin("14:00")),
                new Eveniment("Opera", toMin("13:00"), toMin("15:00")),
                new Eveniment("AfterParty", toMin("14:30"), toMin("16:00"))
        );

        List<Eveniment> sorted = evenimente.stream()
                .sorted(Comparator.comparingInt(Eveniment::startMin))
                .toList();

        System.out.println("=== Varianta 1: Greedy O(N^2) ===");
        int greedyRooms = greedyAllocation(sorted);
        System.out.println("Sali folosite: " + greedyRooms);

        System.out.println("\n=== Varianta 2: PriorityQueue O(N log N) ===");
        int pqRooms = minimumRooms(sorted);
        System.out.println("Sali minime confirmate: " + pqRooms);
    }

    private static int greedyAllocation(List<Eveniment> events) {
        List<Integer> roomEnds = new ArrayList<>();

        for (Eveniment event : events) {
            int chosenRoom = -1;
            for (int i = 0; i < roomEnds.size(); i++) {
                if (roomEnds.get(i) <= event.startMin()) {
                    chosenRoom = i;
                    break;
                }
            }

            if (chosenRoom == -1) {
                roomEnds.add(event.endMin());
                chosenRoom = roomEnds.size() - 1;
            } else {
                roomEnds.set(chosenRoom, event.endMin());
            }

            System.out.printf("%-16s (%s - %s)  ->  Sala #%d%n",
                    event.nume(),
                    toHHMM(event.startMin()),
                    toHHMM(event.endMin()),
                    chosenRoom + 1);
        }

        return roomEnds.size();
    }

    private static int minimumRooms(List<Eveniment> events) {
        PriorityQueue<Integer> endTimes = new PriorityQueue<>();
        int maxRooms = 0;

        for (Eveniment event : events) {
            if (!endTimes.isEmpty() && endTimes.peek() <= event.startMin()) {
                endTimes.poll();
            }
            endTimes.offer(event.endMin());
            maxRooms = Math.max(maxRooms, endTimes.size());
        }

        return maxRooms;
    }
}
