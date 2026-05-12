package com.pao.laboratory01;

import java.util.Scanner;

/**
 * Exercitiul 1
 *
 * Cititi de la tastatura un sir cu n elemente intregi.
 *
 * 1. Afisati elementele sirului in doua modalitati.
 * 2. Afisati media aritmetica a elementelor sirului.
 *
 */

public class MediaAritmetica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] array = new int[n];
        int suma = 0;

        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
            suma += array[i];
        }

        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }

        for (int num : array) {
            System.out.println(num);
        }

        double mediaAritmetica = (double) suma / n;
        System.out.println(mediaAritmetica);
    }
}
