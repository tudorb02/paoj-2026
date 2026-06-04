package com.pao.proiect.banca;

import com.pao.proiect.banca.config.DatabaseConnection;
import com.pao.proiect.banca.config.DatabaseInitializer;
import com.pao.proiect.banca.exception.CardBlocatException;
import com.pao.proiect.banca.exception.ContInexistentException;
import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import com.pao.proiect.banca.exception.IBANInvalidException;
import com.pao.proiect.banca.model.Card;
import com.pao.proiect.banca.model.CardCredit;
import com.pao.proiect.banca.model.CardDebit;
import com.pao.proiect.banca.model.Client;
import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.model.ContCurent;
import com.pao.proiect.banca.model.ContEconomii;
import com.pao.proiect.banca.model.ExtrasCont;
import com.pao.proiect.banca.model.IBAN;
import com.pao.proiect.banca.model.Tranzactie;
import com.pao.proiect.banca.repository.CardRepository;
import com.pao.proiect.banca.repository.ClientRepository;
import com.pao.proiect.banca.repository.ContRepository;
import com.pao.proiect.banca.repository.RaportRepository;
import com.pao.proiect.banca.repository.TranzactieRepository;
import com.pao.proiect.banca.service.AuditService;
import com.pao.proiect.banca.service.CardService;
import com.pao.proiect.banca.service.ClientService;
import com.pao.proiect.banca.service.ContService;
import com.pao.proiect.banca.service.JdbcTransferService;
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

        demonstreazaEtapaII(
                ion,
                maria,
                List.of(contIon, contEconomiiIon, contMaria),
                List.of(cardDebitIon, cardCreditMaria),
                tranzactieService);

        System.out.println("\n=== END ===");
    }

    private static void demonstreazaEtapaII(Client ion,
                                           Client maria,
                                           List<Cont> conturi,
                                           List<Card> carduri,
                                           TranzactieService tranzactieService) {
        header("Demonstratie Etapa II - JDBC, tranzactii, JOIN-uri si audit");

        DatabaseInitializer.getInstance().resetDatabase();
        ClientRepository clientRepository = new ClientRepository();
        ContRepository contRepository = new ContRepository();
        CardRepository cardRepository = new CardRepository();
        TranzactieRepository tranzactieRepository = new TranzactieRepository();
        RaportRepository raportRepository = new RaportRepository();

        clientRepository.save(ion);
        clientRepository.save(maria);
        for (Cont cont : conturi) {
            contRepository.save(cont);
        }
        for (Card card : carduri) {
            cardRepository.save(card);
        }
        for (Tranzactie tranzactie : tranzactieService.listeazaToate()) {
            tranzactieRepository.save(tranzactie);
        }

        System.out.println("  -> baza de date folosita: " + DatabaseConnection.getInstance().getDatabaseUrl());
        System.out.println("  -> clienti salvati in DB: " + clientRepository.findAll().size());
        System.out.println("  -> conturi salvate in DB: " + contRepository.findAll().size());
        System.out.println("  -> carduri salvate in DB: " + cardRepository.findAll().size());
        System.out.println("  -> tranzactii salvate in DB: " + tranzactieRepository.findAll().size());

        maria.setTelefon("0730000000");
        clientRepository.update(maria);
        String telefonMaria = clientRepository.findById(maria.getCnp())
                .map(Client::getTelefon)
                .orElse("-");
        System.out.println("  -> CRUD update client: telefon Maria in DB = " + telefonMaria);

        Client clientTemporar = new Client("Temporar", "Stergere", "1990101220011",
                "temp@example.com", "0700000000");
        clientRepository.save(clientTemporar);
        System.out.println("  -> CRUD save/find client temporar: "
                + clientRepository.findById(clientTemporar.getCnp()).isPresent());
        clientRepository.delete(clientTemporar.getCnp());
        System.out.println("  -> CRUD delete client temporar: "
                + clientRepository.findById(clientTemporar.getCnp()).isEmpty());

        JdbcTransferService jdbcTransferService = new JdbcTransferService();
        String ibanSursa = conturi.get(1).getIban().getValoare();
        String ibanDestinatie = conturi.get(2).getIban().getValoare();
        jdbcTransferService.transferaCuTranzactie(ibanSursa, ibanDestinatie, new BigDecimal("100.00"));
        System.out.println("  -> transfer JDBC cu commit intre " + ibanSursa + " si " + ibanDestinatie);
        System.out.println("     sold sursa in DB      : "
                + contRepository.findById(ibanSursa).map(Cont::getSold).orElse(BigDecimal.ZERO));
        System.out.println("     sold destinatie in DB : "
                + contRepository.findById(ibanDestinatie).map(Cont::getSold).orElse(BigDecimal.ZERO));

        System.out.println("  -> JOIN 1: clienti + numar conturi + sold total");
        raportRepository.clientiCuNumarConturiSiSoldTotal()
                .forEach(r -> System.out.println("     " + r));

        System.out.println("  -> JOIN 2: conturi + titular + numar carduri");
        raportRepository.conturiCuTitularSiNumarCarduri()
                .forEach(r -> System.out.println("     " + r));

        System.out.println("  -> JOIN 3: tranzactii + titular sursa/destinatie");
        raportRepository.tranzactiiCuTitulari()
                .forEach(r -> System.out.println("     " + r));

        System.out.println("  -> audit CSV: " + AuditService.getInstance().getAuditPath());
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
