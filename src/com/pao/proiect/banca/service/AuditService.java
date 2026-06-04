package com.pao.proiect.banca.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

// Scrie actiunile aplicatiei in audit.csv in mod thread-safe.
public final class AuditService {

    private static AuditService instance;

    private final ReentrantLock lock = new ReentrantLock();
    private final Path auditPath = Path.of("audit.csv");

    private AuditService() { }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void logAction(String actionName) {
        if (actionName == null || actionName.isBlank()) {
            throw new IllegalArgumentException("Numele actiunii de audit este obligatoriu.");
        }

        lock.lock();
        try {
            boolean needsHeader = !Files.exists(auditPath) || Files.size(auditPath) == 0;
            try (BufferedWriter writer = Files.newBufferedWriter(
                    auditPath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                if (needsHeader) {
                    writer.write("nume_actiune,timestamp");
                    writer.newLine();
                }
                writer.write(actionName.replace(',', '_'));
                writer.write(",");
                writer.write(LocalDateTime.now().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nu pot scrie in audit.csv.", e);
        } finally {
            lock.unlock();
        }
    }

    public Path getAuditPath() {
        return auditPath.toAbsolutePath();
    }
}

