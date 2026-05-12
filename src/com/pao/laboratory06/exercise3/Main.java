package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ana", "0711111111", 9000, 1500),
                new Inginer("Ionescu", "Mihai", "0722222222", 11000, 2200),
                new Inginer("Enache", "Elena", null, 8000, 900)
        };

        Arrays.sort(ingineri);
        System.out.println("=== Ingineri sortați natural (după nume) ===");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("\n=== Ingineri sortați după salariu descrescător ===");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        PlataOnline plata = ingineri[0];
        plata.autentificare("ana", "secret");
        System.out.println("\nSold înainte de plată: " + plata.consultareSold());
        System.out.println("Plată 250 reușită? " + plata.efectuarePlata(250));
        System.out.println("Sold după plată: " + plata.consultareSold());

        PersoanaJuridica firma = new PersoanaJuridica("Tech", "SRL", "0733333333", 10000);
        PlataOnlineSMS plataSms = firma;
        plataSms.autentificare("firma", "parola");
        System.out.println("\nSMS trimis? " + plataSms.trimiteSMS("Plata a fost confirmată."));
        System.out.println("SMS invalid trimis? " + plataSms.trimiteSMS(""));
        System.out.println("Mesaje salvate: " + firma.getSmsTrimise());

        PersoanaJuridica firmaFaraTelefon = new PersoanaJuridica("NoPhone", "SRL", "", 5000);
        System.out.println("SMS către client fără telefon? " + firmaFaraTelefon.trimiteSMS("Test"));

        System.out.println("\nTVA: " + ConstanteFinanciare.TVA.getValoare());

        try {
            plata.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }

        try {
            if (!(plata instanceof PlataOnlineSMS)) {
                throw new UnsupportedOperationException("Inginerul nu are capabilitate SMS");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Eroare SMS: " + e.getMessage());
        }
    }
}
