package com.pao.proiect.banca.exception;

// Apare cand se incearca o operatie pe un card blocat.
public class CardBlocatException extends RuntimeException {

    public CardBlocatException(String numarCard) {
        super("Cardul " + numarCard + " este blocat. Operatia nu poate fi efectuata.");
    }
}
