package com.pao.laboratory14.exercise1;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collector;

public final class RaportVanzariCollector {
    private RaportVanzariCollector() {
    }

    public static Collector<Bilet, ?, RaportVanzari> toRaportVanzari() {
        return Collector.of(
                Accumulator::new,
                Accumulator::add,
                Accumulator::combine,
                Accumulator::finish
        );
    }

    private static final class Accumulator {
        private final Map<TipBilet, Long> countByType = new EnumMap<>(TipBilet.class);
        private final Map<TipBilet, Double> revenueByType = new EnumMap<>(TipBilet.class);
        private long totalCount;
        private double totalRevenue;

        private void add(Bilet bilet) {
            TipBilet tip = bilet.getTip();
            countByType.put(tip, countByType.getOrDefault(tip, 0L) + 1);
            revenueByType.put(tip, revenueByType.getOrDefault(tip, 0.0) + bilet.getPret());
            totalCount++;
            totalRevenue += bilet.getPret();
        }

        private Accumulator combine(Accumulator other) {
            for (TipBilet tip : TipBilet.values()) {
                long count = other.countByType.getOrDefault(tip, 0L);
                double revenue = other.revenueByType.getOrDefault(tip, 0.0);
                if (count > 0) {
                    countByType.put(tip, countByType.getOrDefault(tip, 0L) + count);
                    revenueByType.put(tip, revenueByType.getOrDefault(tip, 0.0) + revenue);
                }
            }
            totalCount += other.totalCount;
            totalRevenue += other.totalRevenue;
            return this;
        }

        private RaportVanzari finish() {
            TipBilet mostPopular = null;
            long bestCount = -1;
            for (TipBilet tip : TipBilet.values()) {
                long count = countByType.getOrDefault(tip, 0L);
                if (count > bestCount) {
                    bestCount = count;
                    mostPopular = tip;
                }
            }

            double average = totalCount == 0 ? 0.0 : totalRevenue / totalCount;
            return new RaportVanzari(countByType, revenueByType, totalRevenue, average, mostPopular);
        }
    }
}
