package com.pao.proiect.banca.service;

import com.pao.proiect.banca.model.Client;
import com.pao.proiect.banca.service.serviceImpl.ClientServiceImpl;

import java.util.List;
import java.util.Optional;

// Operatiile de baza pentru lucrul cu clientii.
public interface ClientService {

    // Adauga un client nou.
    boolean adauga(Client client);

    // Sterge clientul dupa CNP.
    boolean sterge(String cnp);

    // Cauta clientul dupa CNP.
    Optional<Client> cautaDupaCnp(String cnp);

    // Listeaza toti clientii.
    List<Client> listeazaToti();

    // Numarul total de clienti.
    int count();

    static ClientService getInstance() {
        return ClientServiceImpl.getInstance();
    }
}
