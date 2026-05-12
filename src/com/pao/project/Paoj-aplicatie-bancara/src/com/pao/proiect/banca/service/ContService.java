package com.pao.proiect.banca.service;

import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.model.IBAN;
import com.pao.proiect.banca.service.serviceImpl.ContServiceImpl;

import java.math.BigDecimal;
import java.util.List;

// Operatiile de baza pentru conturile bancare.
public interface ContService {

    // Adauga un cont nou.
    boolean adauga(Cont cont);

    // Sterge contul dupa IBAN.
    boolean sterge(String iban);

    // Cauta contul dupa IBAN.
    Cont cautaDupaIban(String iban);

    // Varianta care primeste direct obiectul IBAN.
    Cont cautaDupaIban(IBAN iban);

    // Listeaza toate conturile.
    List<Cont> listeazaToate();

    // Listeaza conturile sortate dupa sold.
    List<Cont> listeazaSortatDupaSold();

    // Listeaza conturile unui client.
    List<Cont> listeazaConturileClientului(String cnp);

    // Depune o suma in cont.
    void depune(String iban, BigDecimal suma);

    // Retrage o suma din cont.
    void retrage(String iban, BigDecimal suma);

    // Transfera bani intre doua conturi.
    void transfer(String ibanSursa, String ibanDestinatie, BigDecimal suma);

    // Numarul total de conturi.
    int count();

    static ContService getInstance() {
        return ContServiceImpl.getInstance();
    }
}
