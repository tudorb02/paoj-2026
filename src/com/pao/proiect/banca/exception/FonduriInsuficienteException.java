package com.pao.proiect.banca.exception;

// Apare cand se incearca retragerea sau transferul unei sume mai mari decat soldul.
public class FonduriInsuficienteException extends RuntimeException {

    public FonduriInsuficienteException(String message) {
        super(message);
    }

    public FonduriInsuficienteException(String iban, java.math.BigDecimal sold,
                                        java.math.BigDecimal sumaCeruta) {
        super("Fonduri insuficiente in contul " + iban
                + ": sold=" + sold + ", suma ceruta=" + sumaCeruta);
    }
}
