package com.pao.proiect.banca;

import com.pao.proiect.banca.config.DatabaseInitializer;
import com.pao.proiect.banca.model.Client;
import com.pao.proiect.banca.repository.ClientRepository;
import com.pao.proiect.banca.service.AuditService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class CommandMain {

    private final ClientRepository clientRepository = new ClientRepository();
    private final AuditService auditService = AuditService.getInstance();

    public static void main(String[] args) {
        new CommandMain().run();
    }

    private void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                if ("EXIT".equalsIgnoreCase(line)) {
                    System.out.println("Bye.");
                    return;
                }
                execute(line);
            }
        }
    }

    private void execute(String line) {
        String[] tokens = line.split("\\s+");
        String command = tokens[0].toUpperCase();
        try {
            switch (command) {
                case "HELP" -> printHelp();
                case "RESET_DB" -> resetDatabase();
                case "ADD_CLIENT" -> addClient(tokens);
                case "LIST_CLIENTS" -> listClients();
                case "FIND_CLIENT" -> findClient(tokens);
                case "UPDATE_CLIENT" -> updateClient(tokens);
                case "DELETE_CLIENT" -> deleteClient(tokens);
                case "AUDIT" -> audit(tokens);
                default -> System.out.println("Comanda necunoscuta: " + tokens[0]);
            }
        } catch (RuntimeException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private void printHelp() {
        System.out.println("Comenzi disponibile:");
        System.out.println("RESET_DB");
        System.out.println("ADD_CLIENT cnp nume prenume email telefon [yyyy-MM-dd]");
        System.out.println("LIST_CLIENTS");
        System.out.println("FIND_CLIENT cnp");
        System.out.println("UPDATE_CLIENT cnp nume prenume email telefon [yyyy-MM-dd]");
        System.out.println("DELETE_CLIENT cnp");
        System.out.println("AUDIT nume_actiune");
        System.out.println("EXIT");
    }

    private void resetDatabase() {
        DatabaseInitializer.getInstance().resetDatabase();
        auditService.logAction("reset_database");
        System.out.println("Baza de date a fost resetata.");
    }

    private void addClient(String[] tokens) {
        requireArgs(tokens, 6, 7);
        Client client = buildClient(tokens);
        clientRepository.save(client);
        auditService.logAction("adauga_client");
        System.out.println("Client adaugat: " + client.getCnp());
    }

    private void listClients() {
        clientRepository.findAll().forEach(client -> System.out.printf(
                "%s | %s %s | %s | %s | %s%n",
                client.getCnp(),
                client.getNume(),
                client.getPrenume(),
                client.getEmail(),
                client.getTelefon(),
                client.getDataInregistrare()));
    }

    private void findClient(String[] tokens) {
        requireArgs(tokens, 2, 2);
        clientRepository.findById(tokens[1])
                .ifPresentOrElse(
                        client -> System.out.printf(
                                "%s | %s %s | %s | %s | %s%n",
                                client.getCnp(),
                                client.getNume(),
                                client.getPrenume(),
                                client.getEmail(),
                                client.getTelefon(),
                                client.getDataInregistrare()),
                        () -> System.out.println("Clientul nu exista: " + tokens[1]));
    }

    private void updateClient(String[] tokens) {
        requireArgs(tokens, 6, 7);
        Client client = buildClient(tokens);
        if (clientRepository.findById(client.getCnp()).isEmpty()) {
            System.out.println("Clientul nu exista: " + client.getCnp());
            return;
        }
        clientRepository.update(client);
        auditService.logAction("actualizeaza_client");
        System.out.println("Client actualizat: " + client.getCnp());
    }

    private void deleteClient(String[] tokens) {
        requireArgs(tokens, 2, 2);
        String cnp = tokens[1];
        if (clientRepository.findById(cnp).isEmpty()) {
            System.out.println("Clientul nu exista: " + cnp);
            return;
        }
        clientRepository.delete(cnp);
        auditService.logAction("sterge_client");
        System.out.println("Client sters: " + cnp);
    }

    private void audit(String[] tokens) {
        requireArgs(tokens, 2, 2);
        auditService.logAction(tokens[1]);
        System.out.println("Audit scris: " + tokens[1]);
    }

    private Client buildClient(String[] tokens) {
        LocalDate dataInregistrare = tokens.length == 7
                ? LocalDate.parse(tokens[6])
                : LocalDate.now();
        return new Client(tokens[2], tokens[3], tokens[1], tokens[4], tokens[5], dataInregistrare);
    }

    private void requireArgs(String[] tokens, int min, int max) {
        if (tokens.length < min || tokens.length > max) {
            throw new IllegalArgumentException("Numar gresit de argumente pentru "
                    + tokens[0] + ": " + Arrays.toString(tokens));
        }
    }
}
