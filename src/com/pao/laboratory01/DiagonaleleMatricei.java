package com.pao.laboratory01;

import java.util.Locale;
import java.util.Scanner;

/**
 * Exercitiul 2
 *
 * Cititi de la tastatura o matrice de n ori n elemente REALE.
 *
 * 1. Afisati matricea in consola.
 * 2. Afisati suma elementelor de pe diagonala principala
 *    si produsul elementelor de pe diagonala secundara.
 *
 */

public class DiagonaleleMatricei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        int n = scanner.nextInt();
        double[][] matrice = new double[n][n];
        double sumaDiagonalaPrincipala = 0.0;
        double produsDiagonalaSecundara = 1.0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrice[i][j] = scanner.nextDouble();

                if (i == j) {
                    sumaDiagonalaPrincipala += matrice[i][j];
                }

                if (i + j == n - 1) {
                    produsDiagonalaSecundara *= matrice[i][j];
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matrice[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println(sumaDiagonalaPrincipala);
        System.out.println(produsDiagonalaSecundara);
    }
}
