package com.pao.proiect.banca.model;

import com.pao.proiect.banca.exception.IBANInvalidException;

import java.util.Objects;
import java.util.regex.Pattern;

// Clasa imutabila pentru un IBAN romanesc.
public final class IBAN {

    private static final Pattern IBAN_RO_PATTERN =
            Pattern.compile("^RO\\d{2}[A-Z]{4}[A-Z0-9]{16}$");

    private final String valoare;

    public IBAN(String valoare) {
        if (valoare == null) {
            throw new IBANInvalidException("IBAN-ul nu poate fi null.");
        }
        String normalizat = valoare.replaceAll("\\s+", "").toUpperCase();
        if (!IBAN_RO_PATTERN.matcher(normalizat).matches()) {
            throw new IBANInvalidException(
                    "IBAN invalid: '" + valoare + "'. Format asteptat: RO## XXXX #################");
        }
        this.valoare = normalizat;
    }

    public String getValoare() {
        return valoare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IBAN)) return false;
        IBAN iban = (IBAN) o;
        return Objects.equals(valoare, iban.valoare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valoare);
    }

    @Override
    public String toString() {
        return valoare;
    }
}
