package com.pao.proiect.banca.service;

import com.pao.proiect.banca.model.Card;
import com.pao.proiect.banca.model.CardCredit;
import com.pao.proiect.banca.model.CardDebit;
import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.service.serviceImpl.CardServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// Operatiile de baza pentru carduri.
public interface CardService {

    // Emite un card de debit.
    CardDebit emiteCardDebit(Cont contAtasat);

    // Emite un card de credit.
    CardCredit emiteCardCredit(Cont contAtasat, BigDecimal limitaCredit, BigDecimal dobandaAnuala);

    // Adauga manual un card existent.
    boolean adauga(Card card);

    // Sterge cardul dupa numar.
    boolean sterge(String numarCard);

    // Cauta un card dupa numar.
    Optional<Card> cautaDupaNumar(String numarCard);

    // Listeaza toate cardurile.
    List<Card> listeazaToate();

    // Blocheaza un card.
    void blocheaza(String numarCard);

    // Deblocheaza un card.
    void deblocheaza(String numarCard);

    // Listeaza cardurile unui cont.
    List<Card> listeazaCarduriCont(String iban);

    // Numarul total de carduri.
    int count();

    static CardService getInstance() {
        return CardServiceImpl.getInstance();
    }
}
