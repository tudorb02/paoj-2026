package com.pao.proiect.banca.service.serviceImpl;

import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.model.ExtrasCont;
import com.pao.proiect.banca.model.IBAN;
import com.pao.proiect.banca.model.Tranzactie;
import com.pao.proiect.banca.service.TranzactieService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

// Implementarea in memorie pentru istoricul tranzactiilor.
public class TranzactieServiceImpl implements TranzactieService {

    private static TranzactieServiceImpl instance;

    // TreeSet pastreaza tranzactiile ordonate.
    private final TreeSet<Tranzactie> istoric = new TreeSet<>();

    private TranzactieServiceImpl() { }

    public static synchronized TranzactieServiceImpl getInstance() {
        if (instance == null) {
            instance = new TranzactieServiceImpl();
        }
        return instance;
    }

    @Override
    public void inregistreaza(Tranzactie tranzactie) {
        if (tranzactie == null) throw new IllegalArgumentException("Tranzactia nu poate fi null.");
        istoric.add(tranzactie);
    }

    @Override
    public boolean sterge(Tranzactie tranzactie) {
        return istoric.remove(tranzactie);
    }

    @Override
    public List<Tranzactie> listeazaToate() {
        return new ArrayList<>(istoric);
    }

    @Override
    public List<Tranzactie> listeazaPentruCont(IBAN iban) {
        if (iban == null) return List.of();
        List<Tranzactie> rezultat = new ArrayList<>();
        for (Tranzactie t : istoric) {
            if (iban.equals(t.getIbanSursa()) || iban.equals(t.getIbanDestinatie())) {
                rezultat.add(t);
            }
        }
        return rezultat;
    }

    @Override
    public ExtrasCont genereazaExtras(Cont cont, LocalDate start, LocalDate end) {
        if (cont == null) throw new IllegalArgumentException("Contul este obligatoriu pentru extras.");
        if (start == null || end == null) throw new IllegalArgumentException("Intervalul este obligatoriu.");
        if (end.isBefore(start)) throw new IllegalArgumentException("end nu poate fi inainte de start.");

        IBAN ibanCont = cont.getIban();
        List<Tranzactie> tranzactiiInInterval = new ArrayList<>();
        for (Tranzactie t : istoric) {
            if (!ibanCont.equals(t.getIbanSursa()) && !ibanCont.equals(t.getIbanDestinatie())) continue;
            LocalDate dataT = t.getDataOra().toLocalDate();
            if (dataT.isBefore(start) || dataT.isAfter(end)) continue;
            tranzactiiInInterval.add(t);
        }
        return new ExtrasCont(cont, start, end, tranzactiiInInterval);
    }

    @Override
    public int count() { return istoric.size(); }
}
