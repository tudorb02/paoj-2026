package com.pao.laboratory05.audit;

import java.util.Scanner;

/**
 * Exercise 4 (Bonus) — Audit Log
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 4 (Bonus) — Audit"
 *
 * Extinde soluția de la Exercise 3 cu un sistem de audit bazat pe record.
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați (cu Audit) =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează audit log");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.print("Nume: ");
                    String nume = scanner.next();
                    System.out.print("Departament (nume): ");
                    String numeDepartament = scanner.next();
                    System.out.print("Departament (locatie): ");
                    String locatieDepartament = scanner.next();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();
                    service.addAngajat(new Angajat(nume, new Departament(numeDepartament, locatieDepartament), salariu));
                    break;
                case 2:
                    System.out.println("--- Angajați după salariu (descrescător) ---");
                    service.listBySalary();
                    break;
                case 3:
                    System.out.print("Departament: ");
                    String departament = scanner.next();
                    service.findByDepartament(departament);
                    break;
                case 4:
                    service.printAuditLog();
                    break;
                case 0:
                    System.out.println("La revedere!");
                    return;
                default:
                    System.out.println("Opțiune invalidă.");
            }
        }
    }
}
