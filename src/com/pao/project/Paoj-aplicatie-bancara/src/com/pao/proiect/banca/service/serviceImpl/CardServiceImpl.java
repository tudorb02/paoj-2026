package com.pao.proiect.banca.service.serviceImpl;

import com.pao.proiect.banca.model.Card;
import com.pao.proiect.banca.model.CardCredit;
import com.pao.proiect.banca.model.CardDebit;
import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.service.CardService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

// Implementarea in memorie pentru serviciul de carduri.
public class CardServiceImpl implements CardService {

    private static CardServiceImpl instance;

    private final Map<String, Card> carduriDupaNumar = new HashMap<>();

    private CardServiceImpl() { }

    public static synchronized CardServiceImpl getInstance() {
        if (instance == null) {
            instance = new CardServiceImpl();
        }
        return instance;
    }

    // Metode pentru emiterea cardurilor.

    @Override
    public CardDebit emiteCardDebit(Cont contAtasat) {
        if (contAtasat == null) throw new IllegalArgumentException("Contul atasat este obligatoriu.");
        String numar = genereazaNumarCard();
        String cvv = genereazaCvv();
        LocalDate expirare = LocalDate.now().plusYears(4);
        CardDebit card = new CardDebit(numar, cvv, expirare, contAtasat);
        carduriDupaNumar.put(numar, card);
        return card;
    }

    @Override
    public CardCredit emiteCardCredit(Cont contAtasat, BigDecimal limitaCredit, BigDecimal dobandaAnuala) {
        if (contAtasat == null) throw new IllegalArgumentException("Contul atasat este obligatoriu.");
        String numar = genereazaNumarCard();
        String cvv = genereazaCvv();
        LocalDate expirare = LocalDate.now().plusYears(4);
        CardCredit card = new CardCredit(numar, cvv, expirare, contAtasat, limitaCredit, dobandaAnuala);
        carduriDupaNumar.put(numar, card);
        return card;
    }



    @Override
    public boolean adauga(Card card) {
        if (card == null) throw new IllegalArgumentException("Cardul nu poate fi null.");
        if (carduriDupaNumar.containsKey(card.getNumarCard())) return false;
        carduriDupaNumar.put(card.getNumarCard(), card);
        return true;
    }

    @Override
    public boolean sterge(String numarCard) {
        if (numarCard == null) return false;
        return carduriDupaNumar.remove(numarCard) != null;
    }

    @Override
    public Optional<Card> cautaDupaNumar(String numarCard) {
        if (numarCard == null) return Optional.empty();
        return Optional.ofNullable(carduriDupaNumar.get(numarCard));
    }

    @Override
    public List<Card> listeazaToate() {
        return new ArrayList<>(carduriDupaNumar.values());
    }

    // Operatii specifice cardurilor.

    @Override
    public void blocheaza(String numarCard) {
        Card card = carduriDupaNumar.get(numarCard);
        if (card == null) {
            throw new IllegalArgumentException("Nu exista niciun card cu numarul: " + numarCard);
        }
        card.setBlocat(true);
    }

    @Override
    public void deblocheaza(String numarCard) {
        Card card = carduriDupaNumar.get(numarCard);
        if (card != null) card.setBlocat(false);
    }

    @Override
    public List<Card> listeazaCarduriCont(String iban) {
        List<Card> rezultat = new ArrayList<>();
        if (iban == null || iban.isBlank()) {
            return rezultat;
        }
        for (Card card : carduriDupaNumar.values()) {
            if (card.getContAtasat() != null
                    && card.getContAtasat().getIban() != null
                    && iban.equals(card.getContAtasat().getIban().getValoare())) {
                rezultat.add(card);
            }
        }
        return rezultat;
    }

    @Override
    public int count() { return carduriDupaNumar.size(); }

    // Metode ajutatoare.

    private static String genereazaNumarCard() {
        StringBuilder sb = new StringBuilder("4");
        for (int i = 0; i < 15; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return sb.toString();
    }

    private static String genereazaCvv() {
        return String.format("%03d", ThreadLocalRandom.current().nextInt(0, 1000));
    }
}
