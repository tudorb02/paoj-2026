package com.pao.proiect.banca;

import com.pao.proiect.banca.exception.CardBlocatException;
import com.pao.proiect.banca.exception.ContInexistentException;
import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import com.pao.proiect.banca.exception.IBANInvalidException;
import com.pao.proiect.banca.model.CardCredit;
import com.pao.proiect.banca.model.CardDebit;
import com.pao.proiect.banca.model.Client;
import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.model.ContCurent;
import com.pao.proiect.banca.model.ContEconomii;
import com.pao.proiect.banca.model.ExtrasCont;
import com.pao.proiect.banca.model.IBAN;
import com.pao.proiect.banca.model.Tranzactie;
import com.pao.proiect.banca.service.CardService;
import com.pao.proiect.banca.service.ClientService;
import com.pao.proiect.banca.service.ContService;
import com.pao.proiect.banca.service.TranzactieService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Ruleaza cele 10 actiuni din README si afiseaza rezultatul fiecareia.
// In plus, arata si cum sunt tratate exceptiile custom.
public class Main {

    public static void main(String[] args) {
        ClientService clientService = ClientService.getInstance();
        ContService contService = ContService.getInstance();
        CardService cardService = CardService.getInstance();
        TranzactieService tranzactieService = TranzactieService.getInstance();

        header("Aplicatie bancara - demonstratie Etapa I");

        // Actiunea 1: inregistreaza un client nou.
        action(1, "Inregistreaza un client nou");
        Client ion = new Client("Popescu", "Ion", "1900101220011",
                "ion.popescu@example.com", "0712345678");
        clientService.adauga(ion);

        Client maria = new Client("Ionescu", "Maria", "2920202210022",
                "maria.ionescu@example.com", "0723456789");
        clientService.adauga(maria);

        System.out.println("  -> clienti inregistrati: " + clientService.count());
        clientService.listeazaToti().forEach(c -> System.out.println("     " + c));

        // Actiunea 2: deschide conturi noi.
        action(2, "Deschide un cont nou pentru un client existent");
        ContCurent contIon = new ContCurent(
                new IBAN("RO49AAAA1B31007593840000"), ion, new BigDecimal("5.00"));
        ContEconomii contEconomiiIon = new ContEconomii(
                new IBAN("RO50AAAA1B31007593840001"), ion, new BigDecimal("0.05"));
        ContCurent contMaria = new ContCurent(
                new IBAN("RO51BBBB1B31007593840002"), maria, new BigDecimal("3.50"));

        contService.adauga(contIon);
        contService.adauga(contEconomiiIon);
        contService.adauga(contMaria);

        System.out.println("  -> conturi deschise: " + contService.count());
        contService.listeazaToate().forEach(c -> System.out.println("     " + c));

        // Actiunea 3: emite carduri pentru conturi.
        action(3, "Emite un card atasat unui cont");
        CardDebit cardDebitIon = cardService.emiteCardDebit(contIon);
        CardCredit cardCreditMaria = cardService.emiteCardCredit(
                contMaria, new BigDecimal("5000"), new BigDecimal("0.21"));

        System.out.println("  -> cardul de debit emis: " + cardDebitIon);
        System.out.println("  -> cardul de credit emis: " + cardCreditMaria);

        // Actiunea 4: depune bani in conturi.
        action(4, "Depune o suma intr-un cont");
        contService.depune(contIon.getIban().getValoare(), new BigDecimal("1500.00"));
        contService.depune(contEconomiiIon.getIban().getValoare(), new BigDecimal("3000.00"));
        contService.depune(contMaria.getIban().getValoare(), new BigDecimal("800.00"));

        System.out.println("  -> sold contIon         : " + contIon.getSold());
        System.out.println("  -> sold contEconomiiIon : " + contEconomiiIon.getSold());
        System.out.println("  -> sold contMaria       : " + contMaria.getSold());

        // Actiunea 5: retrage bani si arata cazul de fonduri insuficiente.
        action(5, "Retrage o suma dintr-un cont (incluziv un caz de fonduri insuficiente)");
        contService.retrage(contIon.getIban().getValoare(), new BigDecimal("200.00"));
        System.out.println("  -> sold contIon dupa retragere de 200 lei: " + contIon.getSold());

        try {
            contService.retrage(contMaria.getIban().getValoare(), new BigDecimal("999999.00"));
        } catch (FonduriInsuficienteException e) {
            System.out.println("  -> exceptie capturata: " + e.getMessage());
        }

        // Actiunea 6: transfera bani intre doua conturi.
        action(6, "Transfera bani intre doua conturi");
        contService.transfer(
                contIon.getIban().getValoare(),
                contMaria.getIban().getValoare(),
                new BigDecimal("250.00"));
        System.out.println("  -> sold contIon   : " + contIon.getSold());
        System.out.println("  -> sold contMaria : " + contMaria.getSold());

        // Actiunea 7: cauta un cont dupa IBAN.
        action(7, "Cauta un cont dupa IBAN");
        Cont gasit = contService.cautaDupaIban(contIon.getIban().getValoare());
        System.out.println("  -> cont gasit: " + gasit);

        try {
            contService.cautaDupaIban("RO00ZZZZ0000000000000000");
        } catch (ContInexistentException e) {
            System.out.println("  -> exceptie capturata: " + e.getMessage());
        }

        // Actiunea 8: listeaza istoricul tranzactiilor unui cont.
        action(8, "Listeaza istoricul tranzactiilor unui cont");
        List<Tranzactie> istoricIon = tranzactieService.listeazaPentruCont(contIon.getIban());
        System.out.println("  -> contIon are " + istoricIon.size() + " tranzactii (sortate descrescator dupa data):");
        istoricIon.forEach(t -> System.out.println("     " + t));

        // Actiunea 9: genereaza extras de cont.
        action(9, "Genereaza un extras de cont pentru un interval");
        LocalDate azi = LocalDate.now();
        ExtrasCont extras = tranzactieService.genereazaExtras(contIon, azi.minusDays(7), azi);
        System.out.println(extras);

        // Actiunea 10: blocheaza un card.
        action(10, "Blocheaza un card");
        cardService.blocheaza(cardDebitIon.getNumarCard());
        System.out.println("  -> card dupa blocare: " + cardDebitIon);

        try {
            cardDebitIon.asigurareCardActiv();
        } catch (CardBlocatException e) {
            System.out.println("  -> exceptie capturata: " + e.getMessage());
        }


        header("Demonstratii suplimentare");

        try {
            new IBAN("ABC123");
        } catch (IBANInvalidException e) {
            System.out.println("  -> IBANInvalidException: " + e.getMessage());
        }

        System.out.println("  -> Conturile lui Ion (lookup dupa CNP):");
        Optional<Client> ionLookup = clientService.cautaDupaCnp(ion.getCnp());
        ionLookup.ifPresent(client ->
                contService.listeazaConturileClientului(client.getCnp())
                        .forEach(c -> System.out.println("     " + c)));

        System.out.println("  -> Conturile sortate descrescator dupa sold:");
        contService.listeazaSortatDupaSold().forEach(c -> System.out.println("     " + c));

        System.out.println("  -> Cardurile lui Maria (lookup dupa IBAN):");
        cardService.listeazaCarduriCont(contMaria.getIban().getValoare())
                .forEach(c -> System.out.println("     " + c));

        System.out.println("\n=== END ===");
    }

    private static void header(String text) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(" " + text);
        System.out.println("==================================================");
    }

    private static void action(int n, String text) {
        System.out.println();
        System.out.println("--- Actiunea " + n + ": " + text + " ---");
    }
}
