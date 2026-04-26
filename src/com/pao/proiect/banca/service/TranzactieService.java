package com.pao.proiect.banca.service;

import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.model.ExtrasCont;
import com.pao.proiect.banca.model.IBAN;
import com.pao.proiect.banca.model.Tranzactie;
import com.pao.proiect.banca.service.serviceImpl.TranzactieServiceImpl;

import java.time.LocalDate;
import java.util.List;

// Operatiile de baza pentru istoricul tranzactiilor.
public interface TranzactieService {

    // Adauga o tranzactie in istoric.
    void inregistreaza(Tranzactie tranzactie);

    // Sterge o tranzactie din istoric.
    boolean sterge(Tranzactie tranzactie);

    // Listeaza toate tranzactiile.
    List<Tranzactie> listeazaToate();

    // Listeaza tranzactiile unui cont.
    List<Tranzactie> listeazaPentruCont(IBAN iban);

    // Genereaza extrasul unui cont pe un interval.
    ExtrasCont genereazaExtras(Cont cont, LocalDate start, LocalDate end);

    // Numarul total de tranzactii.
    int count();

    static TranzactieService getInstance() {
        return TranzactieServiceImpl.getInstance();
    }
}
