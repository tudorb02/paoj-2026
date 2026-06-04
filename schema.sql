PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS clients;

CREATE TABLE clients (
    cnp TEXT PRIMARY KEY,
    nume TEXT NOT NULL,
    prenume TEXT NOT NULL,
    email TEXT NOT NULL,
    telefon TEXT NOT NULL,
    data_inregistrare TEXT NOT NULL
);

CREATE TABLE accounts (
    iban TEXT PRIMARY KEY,
    cnp_client TEXT NOT NULL,
    tip_cont TEXT NOT NULL,
    sold TEXT NOT NULL,
    data_deschidere TEXT NOT NULL,
    comision_lunar TEXT,
    dobanda_anuala TEXT,
    FOREIGN KEY (cnp_client) REFERENCES clients(cnp)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE cards (
    numar_card TEXT PRIMARY KEY,
    iban TEXT NOT NULL,
    tip_card TEXT NOT NULL,
    cvv TEXT NOT NULL,
    data_expirare TEXT NOT NULL,
    blocat INTEGER NOT NULL DEFAULT 0,
    limita_credit TEXT,
    dobanda_anuala TEXT,
    FOREIGN KEY (iban) REFERENCES accounts(iban)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE transactions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    iban_sursa TEXT,
    iban_destinatie TEXT,
    suma TEXT NOT NULL,
    data_ora TEXT NOT NULL,
    tip TEXT NOT NULL,
    FOREIGN KEY (iban_sursa) REFERENCES accounts(iban)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (iban_destinatie) REFERENCES accounts(iban)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

