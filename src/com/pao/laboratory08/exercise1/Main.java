package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = readStudents();
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNext()) {
            return;
        }

        String command = scanner.next();
        switch (command) {
            case "PRINT":
                for (Student student : studenti) {
                    System.out.println(student);
                }
                break;
            case "SHALLOW":
                cloneAndPrint(studenti, scanner.next(), false);
                break;
            case "DEEP":
                cloneAndPrint(studenti, scanner.next(), true);
                break;
            default:
                throw new IllegalArgumentException("Comandă necunoscută: " + command);
        }
    }

    public static List<Student> readStudents() throws IOException {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",");
                studenti.add(new Student(
                        parts[0].trim(),
                        Integer.parseInt(parts[1].trim()),
                        new Adresa(parts[2].trim(), parts[3].trim())
                ));
            }
        }
        return studenti;
    }

    private static void cloneAndPrint(List<Student> studenti, String nume, boolean deep) throws CloneNotSupportedException {
        Student original = findByName(studenti, nume);
        Student clona = deep ? original.deepClone() : original.shallowClone();
        clona.getAdresa().setOras("MODIFICAT");
        System.out.println("Original: " + original);
        System.out.println("Clona: " + clona);
    }

    private static Student findByName(List<Student> studenti, String nume) {
        for (Student student : studenti) {
            if (student.getNume().equals(nume)) {
                return student;
            }
        }
        throw new IllegalArgumentException("Student inexistent: " + nume);
    }
}
