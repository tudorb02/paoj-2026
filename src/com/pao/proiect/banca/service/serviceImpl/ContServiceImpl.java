package com.pao.proiect.banca.service.serviceImpl;

import com.pao.proiect.banca.exception.ContInexistentException;
import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.model.IBAN;
import com.pao.proiect.banca.model.TipTranzactie;
import com.pao.proiect.banca.model.Tranzactie;
import com.pao.proiect.banca.service.AuditService;
import com.pao.proiect.banca.service.ContService;
import com.pao.proiect.banca.service.TranzactieService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Implementarea in memorie pentru serviciul de conturi.
public class ContServiceImpl implements ContService {

    private static ContServiceImpl instance;

    // Conturile sunt indexate dupa IBAN.
    private final Map<String, Cont> conturiDupaIban = new HashMap<>();

    private ContServiceImpl() { }

    public static synchronized ContServiceImpl getInstance() {
        if (instance == null) {
            instance = new ContServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean adauga(Cont cont) {
        if (cont == null) throw new IllegalArgumentException("Contul nu poate fi null.");
        if (cont.getIban() == null) throw new IllegalArgumentException("Contul trebuie sa aiba IBAN.");
        String key = cont.getIban().getValoare();
        if (conturiDupaIban.containsKey(key)) {
            return false;
        }
        conturiDupaIban.put(key, cont);
        if (cont.getTitular() != null) {
            cont.getTitular().adaugaCont(cont);
        }
        AuditService.getInstance().logAction("deschide_cont");
        return true;
    }

    @Override
    public boolean sterge(String iban) {
        if (iban == null) return false;
        Cont sters = conturiDupaIban.remove(iban);
        if (sters != null && sters.getTitular() != null) {
            sters.getTitular().stergeCont(sters);
        }
        if (sters != null) {
            AuditService.getInstance().logAction("sterge_cont");
        }
        return sters != null;
    }

    @Override
    public Cont cautaDupaIban(String iban) {
        if (iban == null) throw new ContInexistentException("null");
        AuditService.getInstance().logAction("cauta_cont_dupa_iban");
        Cont cont = conturiDupaIban.get(iban);
        if (cont == null) {
            throw new ContInexistentException(iban);
        }
        return cont;
    }

    @Override
    public Cont cautaDupaIban(IBAN iban) {
        return cautaDupaIban(iban == null ? null : iban.getValoare());
    }

    @Override
    public List<Cont> listeazaToate() {
        return new ArrayList<>(conturiDupaIban.values());
    }

    @Override
    public List<Cont> listeazaSortatDupaSold() {
        List<Cont> rezultat = new ArrayList<>(conturiDupaIban.values());
        rezultat.sort(Comparator.comparing(Cont::getSold).reversed());
        return rezultat;
    }

    @Override
    public List<Cont> listeazaConturileClientului(String cnp) {
        List<Cont> rezultat = new ArrayList<>();
        if (cnp == null || cnp.isBlank()) {
            return rezultat;
        }
        for (Cont c : conturiDupaIban.values()) {
            if (c.getTitular() != null && cnp.equals(c.getTitular().getCnp())) {
                rezultat.add(c);
            }
        }
        return rezultat;
    }

    // Operatii financiare.

    @Override
    public void depune(String iban, BigDecimal suma) {
        Cont cont = cautaDupaIban(iban);
        cont.depune(suma);
        TranzactieService.getInstance().inregistreaza(
                new Tranzactie(null, cont.getIban(), suma, TipTranzactie.DEPOZIT));
        AuditService.getInstance().logAction("depune_bani");
    }

    @Override
    public void retrage(String iban, BigDecimal suma) {
        Cont cont = cautaDupaIban(iban);
        cont.retrage(suma);
        TranzactieService.getInstance().inregistreaza(
                new Tranzactie(cont.getIban(), null, suma, TipTranzactie.RETRAGERE));
        AuditService.getInstance().logAction("retrage_bani");
    }

    // Daca al doilea pas esueaza, suma se intoarce in contul sursa.
    @Override
    public void transfer(String ibanSursa, String ibanDestinatie, BigDecimal suma) {
        Cont sursa = cautaDupaIban(ibanSursa);
        Cont destinatie = cautaDupaIban(ibanDestinatie);

        sursa.retrage(suma);
        try {
            destinatie.depune(suma);
        } catch (RuntimeException e) {
            // Daca depunerea nu merge, refacem soldul sursei.
            sursa.depune(suma);
            throw e;
        }
        TranzactieService.getInstance().inregistreaza(
                new Tranzactie(sursa.getIban(), destinatie.getIban(), suma, TipTranzactie.TRANSFER));
        AuditService.getInstance().logAction("transfera_bani");
    }

    @Override
    public int count() {
        return conturiDupaIban.size();
    }
}
