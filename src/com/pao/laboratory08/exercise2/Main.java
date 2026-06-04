package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = readStudents();
        Scanner scanner = new Scanner(System.in);
        int prag = scanner.nextInt();

        List<Student> filtrati = new ArrayList<>();
        for (Student student : studenti) {
            if (student.getVarsta() >= prag) {
                filtrati.add(student);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rezultate.txt"))) {
            for (Student student : filtrati) {
                writer.write(student.toString());
                writer.newLine();
            }
        }

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        for (Student student : filtrati) {
            System.out.println(student);
        }
        System.out.println();
        System.out.println("Scris in: rezultate.txt");
    }

    private static List<Student> readStudents() throws IOException {
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
                        new com.pao.laboratory08.exercise1.Adresa(parts[2].trim(), parts[3].trim())
                ));
            }
        }
        return studenti;
    }
}
