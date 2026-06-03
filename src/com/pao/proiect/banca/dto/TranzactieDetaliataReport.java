package com.pao.proiect.banca.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TranzactieDetaliataReport {

    private final long id;
    private final String tip;
    private final BigDecimal suma;
    private final LocalDateTime dataOra;
    private final String ibanSursa;
    private final String titularSursa;
    private final String ibanDestinatie;
    private final String titularDestinatie;

    public TranzactieDetaliataReport(long id, String tip, BigDecimal suma, LocalDateTime dataOra,
                                     String ibanSursa, String titularSursa,
                                     String ibanDestinatie, String titularDestinatie) {
        this.id = id;
        this.tip = tip;
        this.suma = suma;
        this.dataOra = dataOra;
        this.ibanSursa = ibanSursa;
        this.titularSursa = titularSursa;
        this.ibanDestinatie = ibanDestinatie;
        this.titularDestinatie = titularDestinatie;
    }

    @Override
    public String toString() {
        return "TranzactieDetaliataReport{" +
                "id=" + id +
                ", tip='" + tip + '\'' +
                ", suma=" + suma +
                ", dataOra=" + dataOra +
                ", ibanSursa='" + (ibanSursa == null ? "-" : ibanSursa) + '\'' +
                ", titularSursa='" + (titularSursa == null ? "-" : titularSursa) + '\'' +
                ", ibanDestinatie='" + (ibanDestinatie == null ? "-" : ibanDestinatie) + '\'' +
                ", titularDestinatie='" + (titularDestinatie == null ? "-" : titularDestinatie) + '\'' +
                '}';
    }
}

