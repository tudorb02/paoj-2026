package com.pao.proiect.banca.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

// Reprezinta o tranzactie bancara.
public class Tranzactie implements Comparable<Tranzactie> {

    private static final AtomicLong ID_GEN = new AtomicLong(1);

    private final long id;
    private final IBAN ibanSursa;       // null pentru DEPOZIT
    private final IBAN ibanDestinatie;  // null pentru RETRAGERE
    private final BigDecimal suma;
    private final LocalDateTime dataOra;
    private final TipTranzactie tip;

    public Tranzactie(IBAN ibanSursa, IBAN ibanDestinatie, BigDecimal suma, TipTranzactie tip) {
        this.id = ID_GEN.getAndIncrement();
        this.ibanSursa = ibanSursa;
        this.ibanDestinatie = ibanDestinatie;
        this.suma = suma;
        this.tip = tip;
        this.dataOra = LocalDateTime.now();
    }

    // Constructor folosit cand vrem sa setam manual data.
    public Tranzactie(IBAN ibanSursa, IBAN ibanDestinatie, BigDecimal suma,
                      TipTranzactie tip, LocalDateTime dataOra) {
        this.id = ID_GEN.getAndIncrement();
        this.ibanSursa = ibanSursa;
        this.ibanDestinatie = ibanDestinatie;
        this.suma = suma;
        this.tip = tip;
        this.dataOra = dataOra == null ? LocalDateTime.now() : dataOra;
    }

    public long getId() { return id; }
    public IBAN getIbanSursa() { return ibanSursa; }
    public IBAN getIbanDestinatie() { return ibanDestinatie; }
    public BigDecimal getSuma() { return suma; }
    public LocalDateTime getDataOra() { return dataOra; }
    public TipTranzactie getTip() { return tip; }

    // Tranzactiile mai noi vin primele. Daca data e aceeasi, comparam dupa id.
    @Override
    public int compareTo(Tranzactie o) {
        int cmp = o.dataOra.compareTo(this.dataOra);
        if (cmp != 0) return cmp;
        return Long.compare(this.id, o.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tranzactie)) return false;
        Tranzactie that = (Tranzactie) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "id=" + id +
                ", tip=" + tip +
                ", suma=" + suma +
                ", sursa=" + (ibanSursa != null ? ibanSursa : "-") +
                ", destinatie=" + (ibanDestinatie != null ? ibanDestinatie : "-") +
                ", data=" + dataOra +
                '}';
    }
}
