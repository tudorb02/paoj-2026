package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            run();
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    private static void run() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        EvenimentRepository repository = new EvenimentRepository();
        repository.initSchema();

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "ADD": {
                    String nume = scanner.next();
                    String data = scanner.next();
                    int capacitate = scanner.nextInt();
                    TipBilet tip = TipBilet.valueOf(scanner.next());
                    Eveniment eveniment = new Eveniment(nume, data, capacitate, tip);
                    repository.save(eveniment);
                    System.out.println("Adaugat: [" + eveniment.getId() + "] " + eveniment.getNume());
                    break;
                }
                case "LIST":
                    for (Eveniment eveniment : repository.findAll()) {
                        System.out.println(eveniment);
                    }
                    break;
                case "DELETE": {
                    int id = scanner.nextInt();
                    int deleted = repository.deleteImpl(id);
                    System.out.println(deleted == 0 ? "Nu exista: " + id : "Sters: " + id);
                    break;
                }
                case "COUNT":
                    System.out.println("Total: " + repository.count());
                    break;
                default:
                    break;
            }
        }
    }
}
