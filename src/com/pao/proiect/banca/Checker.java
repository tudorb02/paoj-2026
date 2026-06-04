package com.pao.proiect.banca;

import com.pao.proiect.banca.config.DatabaseInitializer;
import com.pao.proiect.banca.model.Card;
import com.pao.proiect.banca.model.CardCredit;
import com.pao.proiect.banca.model.CardDebit;
import com.pao.proiect.banca.model.Client;
import com.pao.proiect.banca.model.Cont;
import com.pao.proiect.banca.model.ContCurent;
import com.pao.proiect.banca.model.ContEconomii;
import com.pao.proiect.banca.model.IBAN;
import com.pao.proiect.banca.model.TipTranzactie;
import com.pao.proiect.banca.model.Tranzactie;
import com.pao.proiect.banca.repository.CardRepository;
import com.pao.proiect.banca.repository.ClientRepository;
import com.pao.proiect.banca.repository.ContRepository;
import com.pao.proiect.banca.repository.RaportRepository;
import com.pao.proiect.banca.repository.TranzactieRepository;
import com.pao.proiect.banca.service.AuditService;
import com.pao.proiect.banca.service.JdbcTransferService;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class Checker {

    public static void main(String[] args) throws Exception {
        Path auditPath = Path.of("audit.csv");
        Files.deleteIfExists(auditPath);

        DatabaseInitializer.getInstance().resetDatabase();
        pass("schema.sql + DatabaseConnection");

        ClientRepository clientRepository = new ClientRepository();
        ContRepository contRepository = new ContRepository();
        CardRepository cardRepository = new CardRepository();
        TranzactieRepository tranzactieRepository = new TranzactieRepository();
        RaportRepository raportRepository = new RaportRepository();

        Client ion = new Client("Popescu", "Ion", "1900101220011",
                "ion.popescu@example.com", "0712345678", LocalDate.of(2026, 6, 1));
        Client maria = new Client("Ionescu", "Maria", "2920202210022",
                "maria.ionescu@example.com", "0723456789", LocalDate.of(2026, 6, 2));
        clientRepository.save(ion);
        clientRepository.save(maria);

        Cont contIon = new ContCurent(
                new IBAN("RO49AAAA1B31007593840000"),
                ion,
                new BigDecimal("1000.00"),
                LocalDate.of(2026, 6, 1),
                new BigDecimal("5.00"));
        Cont contMaria = new ContEconomii(
                new IBAN("RO50AAAA1B31007593840001"),
                maria,
                new BigDecimal("500.00"),
                LocalDate.of(2026, 6, 2),
                new BigDecimal("0.05"));
        contRepository.save(contIon);
        contRepository.save(contMaria);

        Card cardDebit = new CardDebit("4111111111111111", "123",
                LocalDate.of(2030, 6, 1), contIon);
        Card cardCredit = new CardCredit("4222222222222222", "456",
                LocalDate.of(2030, 6, 1), contMaria,
                new BigDecimal("3000.00"), new BigDecimal("0.19"));
        cardRepository.save(cardDebit);
        cardRepository.save(cardCredit);

        tranzactieRepository.save(new Tranzactie(1L, null, contIon.getIban(),
                new BigDecimal("1000.00"), TipTranzactie.DEPOZIT,
                java.time.LocalDateTime.of(2026, 6, 1, 10, 0)));
        tranzactieRepository.save(new Tranzactie(2L, null, contMaria.getIban(),
                new BigDecimal("500.00"), TipTranzactie.DEPOZIT,
                java.time.LocalDateTime.of(2026, 6, 2, 10, 0)));

        assertEquals(2, clientRepository.findAll().size(), "clienti salvati");
        assertEquals(2, contRepository.findAll().size(), "conturi salvate");
        assertEquals(2, cardRepository.findAll().size(), "carduri salvate");
        assertEquals(2, tranzactieRepository.findAll().size(), "tranzactii initiale salvate");
        pass("CRUD save/findAll pentru 4 repository-uri");

        maria.setTelefon("0730000000");
        clientRepository.update(maria);
        assertEquals("0730000000",
                clientRepository.findById(maria.getCnp()).orElseThrow().getTelefon(),
                "update client");

        Client temporar = new Client("Temporar", "Test", "1990101220011",
                "temp@example.com", "0700000000");
        clientRepository.save(temporar);
        assertTrue(clientRepository.findById(temporar.getCnp()).isPresent(), "findById client temporar");
        clientRepository.delete(temporar.getCnp());
        assertTrue(clientRepository.findById(temporar.getCnp()).isEmpty(), "delete client temporar");
        pass("CRUD update/delete");

        new JdbcTransferService().transferaCuTranzactie(
                contIon.getIban().getValoare(),
                contMaria.getIban().getValoare(),
                new BigDecimal("150.00"));
        assertEquals(new BigDecimal("850.00"),
                contRepository.findById(contIon.getIban().getValoare()).orElseThrow().getSold(),
                "sold sursa dupa transfer JDBC");
        assertEquals(new BigDecimal("650.00"),
                contRepository.findById(contMaria.getIban().getValoare()).orElseThrow().getSold(),
                "sold destinatie dupa transfer JDBC");
        assertEquals(3, tranzactieRepository.findAll().size(), "tranzactie inserata dupa transfer");
        pass("tranzactie JDBC explicita cu commit");

        assertEquals(2, raportRepository.clientiCuNumarConturiSiSoldTotal().size(), "JOIN clienti-conturi");
        assertEquals(2, raportRepository.conturiCuTitularSiNumarCarduri().size(), "JOIN conturi-carduri");
        assertEquals(3, raportRepository.tranzactiiCuTitulari().size(), "JOIN tranzactii-titulari");
        pass("3 query-uri JOIN");

        AuditService.getInstance().logAction("checker_proiect_etapa2");
        assertTrue(Files.exists(auditPath), "audit.csv exista");
        assertTrue(Files.readString(auditPath).contains("checker_proiect_etapa2"), "audit contine actiunea");
        pass("AuditService CSV thread-safe");

        System.out.println();
        System.out.println("TOTAL: 6/6 verificari trecute");
    }

    private static void pass(String message) {
        System.out.println("[PASS] " + message);
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("[FAIL] " + message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected instanceof BigDecimal expectedDecimal && actual instanceof BigDecimal actualDecimal) {
            if (expectedDecimal.compareTo(actualDecimal) != 0) {
                throw new AssertionError("[FAIL] " + message
                        + " | expected=" + expected + ", actual=" + actual);
            }
            return;
        }
        if (!expected.equals(actual)) {
            throw new AssertionError("[FAIL] " + message
                    + " | expected=" + expected + ", actual=" + actual);
        }
    }
}
