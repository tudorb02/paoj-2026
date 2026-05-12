package com.pao.proiect.banca.model;

import com.pao.proiect.banca.exception.FonduriInsuficienteException;

import java.math.BigDecimal;

public interface Tranzactionabil {

    // Adauga o suma valida la sold.
    void depune(BigDecimal suma);

    // Scade o suma valida din sold.
    void retrage(BigDecimal suma);
}
