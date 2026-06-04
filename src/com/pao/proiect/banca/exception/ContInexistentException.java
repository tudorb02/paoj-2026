package com.pao.proiect.banca.exception;

// Apare cand se cauta sau se foloseste un cont care nu exista.
public class ContInexistentException extends RuntimeException {

    public ContInexistentException(String iban) {
        super("Nu exista niciun cont cu IBAN-ul: " + iban);
    }
}
