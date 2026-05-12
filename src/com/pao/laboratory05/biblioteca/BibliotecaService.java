package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti;

    private BibliotecaService() {
        this.carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] copy = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, copy, 0, carti.length);
        copy[copy.length - 1] = carte;
        carti = copy;
        System.out.println("Carte adăugată: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        Carte[] copy = carti.clone();
        Arrays.sort(copy);
        printCarti(copy);
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator);
        printCarti(copy);
    }

    private void printCarti(Carte[] cartiDeAfisat) {
        for (int i = 0; i < cartiDeAfisat.length; i++) {
            System.out.println((i + 1) + ". " + cartiDeAfisat[i]);
        }
    }
}
