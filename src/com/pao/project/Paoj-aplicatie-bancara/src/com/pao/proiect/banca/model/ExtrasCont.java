package com.pao.proiect.banca.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Retine tranzactiile si totalurile pentru un extras de cont.
public class ExtrasCont {

    private final Cont cont;
    private final LocalDate start;
    private final LocalDate end;
    private final List<Tranzactie> tranzactii;
    private final BigDecimal totalIntrari;
    private final BigDecimal totalIesiri;

    public ExtrasCont(Cont cont, LocalDate start, LocalDate end, List<Tranzactie> tranzactii) {
        this.cont = cont;
        this.start = start;
        this.end = end;
        this.tranzactii = new ArrayList<>(tranzactii);

        BigDecimal intrari = BigDecimal.ZERO;
        BigDecimal iesiri = BigDecimal.ZERO;
        for (Tranzactie t : tranzactii) {
            boolean estiSursa = t.getIbanSursa() != null && t.getIbanSursa().equals(cont.getIban());
            boolean estiDestinatie = t.getIbanDestinatie() != null && t.getIbanDestinatie().equals(cont.getIban());
            if (estiDestinatie) intrari = intrari.add(t.getSuma());
            if (estiSursa)      iesiri  = iesiri.add(t.getSuma());
        }
        this.totalIntrari = intrari;
        this.totalIesiri  = iesiri;
    }

    public Cont getCont() { return cont; }
    public LocalDate getStart() { return start; }
    public LocalDate getEnd() { return end; }
    public List<Tranzactie> getTranzactii() { return tranzactii; }
    public BigDecimal getTotalIntrari() { return totalIntrari; }
    public BigDecimal getTotalIesiri() { return totalIesiri; }

    public BigDecimal getSoldFinal() { return cont.getSold(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtrasCont [iban=").append(cont.getIban())
                .append(", interval=").append(start).append(" -> ").append(end)
                .append(", nrTranzactii=").append(tranzactii.size())
                .append(", totalIntrari=").append(totalIntrari)
                .append(", totalIesiri=").append(totalIesiri)
                .append(", soldFinal=").append(getSoldFinal())
                .append("]\n");
        for (Tranzactie t : tranzactii) {
            sb.append("    ").append(t).append('\n');
        }
        return sb.toString();
    }
}
