package com.pao.proiect.banca.service.serviceImpl;

import com.pao.proiect.banca.model.Client;
import com.pao.proiect.banca.service.AuditService;
import com.pao.proiect.banca.service.ClientService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Implementarea in memorie pentru serviciul de clienti.
public class ClientServiceImpl implements ClientService {

    private static ClientServiceImpl instance;

    private final Map<String, Client> clientiDupaCnp = new HashMap<>();

    private ClientServiceImpl() {
        // Constructor privat pentru Singleton.
    }

    public static synchronized ClientServiceImpl getInstance() {
        if (instance == null) {
            instance = new ClientServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean adauga(Client client) {
        if (client == null) throw new IllegalArgumentException("Clientul nu poate fi null.");
        if (client.getCnp() == null || client.getCnp().isBlank()) {
            throw new IllegalArgumentException("CNP-ul clientului este obligatoriu.");
        }
        if (clientiDupaCnp.containsKey(client.getCnp())) {
            return false;
        }
        clientiDupaCnp.put(client.getCnp(), client);
        AuditService.getInstance().logAction("inregistreaza_client");
        return true;
    }

    @Override
    public boolean sterge(String cnp) {
        if (cnp == null) return false;
        boolean removed = clientiDupaCnp.remove(cnp) != null;
        if (removed) {
            AuditService.getInstance().logAction("sterge_client");
        }
        return removed;
    }

    @Override
    public Optional<Client> cautaDupaCnp(String cnp) {
        if (cnp == null) return Optional.empty();
        AuditService.getInstance().logAction("cauta_client_dupa_cnp");
        return Optional.ofNullable(clientiDupaCnp.get(cnp));
    }

    @Override
    public List<Client> listeazaToti() {
        return new ArrayList<>(clientiDupaCnp.values());
    }

    @Override
    public int count() {
        return clientiDupaCnp.size();
    }
}
