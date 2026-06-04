package com.pao.laboratory07.exercise1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderState initialState = OrderState.valueOf(scanner.next());
        Order order = new Order(initialState);
        System.out.println("Initial order state: " + initialState);

        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "next":
                    if (order.nextState()) {
                        System.out.println("Order state updated to: " + order.getState());
                    } else {
                        System.out.println("Order is already in a final state.");
                    }
                    break;
                case "cancel":
                    if (order.cancel()) {
                        System.out.println("Order has been canceled.");
                    } else {
                        System.out.println("Cannot cancel a final state order.");
                    }
                    break;
                case "undo":
                    if (order.undoState()) {
                        System.out.println("Order state reverted to: " + order.getState());
                    } else {
                        System.out.println("Nu există stare anterioară pentru undo.");
                    }
                    break;
                case "QUIT":
                    System.out.println("User quit the program.");
                    return;
                default:
                    System.out.println("Unknown command: " + command);
            }
        }
    }
}
