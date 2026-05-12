package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;

    private AngajatService() {
        this.angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat angajat) {
        Angajat[] copy = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, copy, 0, angajati.length);
        copy[copy.length - 1] = angajat;
        angajati = copy;
        System.out.println("Angajat adăugat: " + angajat.getNume());
    }

    public void printAll() {
        for (int i = 0; i < angajati.length; i++) {
            System.out.println((i + 1) + ". " + angajati[i]);
        }
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        boolean found = false;
        System.out.println("--- Angajați din " + numeDept + " ---");
        for (Angajat angajat : angajati) {
            if (angajat.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(angajat);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}
