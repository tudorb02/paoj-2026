package com.pao.laboratory13.exercise1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProtocolEngine engine = new ProtocolEngine();

        if (!scanner.hasNextInt()) {
            return;
        }

        int commandCount = scanner.nextInt();
        scanner.nextLine();

        int processed = 0;
        while (processed < commandCount && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }

            String response = engine.execute(line);
            if (response != null) {
                System.out.println(response);
                processed++;
            }
        }
    }
}
