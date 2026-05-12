package com.pao.proiect.banca.exception;

// Apare cand un IBAN nu respecta formatul cerut.
public class IBANInvalidException extends RuntimeException {

    public IBANInvalidException(String message) {
        super(message);
    }
}
