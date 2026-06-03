# Aplicatie bancara

## 1. Definirea sistemului

Proiectul simuleaza operatiile de baza ale unei banci: administrarea clientilor,
deschiderea conturilor, emiterea cardurilor si procesarea tranzactiilor. In Etapa I
datele au fost tinute in memorie, iar in Etapa II a fost adaugat stratul JDBC pentru
persistenta intr-o baza de date SQLite.

### 1.1 Cele 10 actiuni posibile in sistem

1. Inregistreaza un client nou in banca
2. Deschide un cont nou pentru un client existent
3. Emite un card atasat unui cont
4. Depune o suma intr-un cont
5. Retrage o suma dintr-un cont
6. Transfera bani intre doua conturi
7. Cauta un cont dupa IBAN
8. Listeaza istoricul tranzactiilor unui cont
9. Genereaza un extras de cont pentru un interval de timp
10. Blocheaza un card

### 1.2 Tipuri de obiecte din domeniu

`Client`, `Angajat`, `Manager`, `Cont`, `ContCurent`, `ContEconomii`, `Card`, `CardDebit`, `CardCredit`, `Tranzactie`, `IBAN`, `ExtrasCont`, `Sucursala`, `Banca`

## 2. Etapa II - persistenta JDBC, tranzactii si audit

### 2.1 Baza de date si conexiune

- `schema.sql` se afla in radacina proiectului si creeaza tabelele `clients`, `accounts`, `cards`, `transactions`.
- Toate tabelele au cheie primara.
- Exista mai multe relatii `FOREIGN KEY`:
  - `accounts.cnp_client -> clients.cnp`
  - `cards.iban -> accounts.iban`
  - `transactions.iban_sursa -> accounts.iban`
  - `transactions.iban_destinatie -> accounts.iban`
- `resources/db.properties` contine configuratia JDBC pentru SQLite.
- `DatabaseConnection` este Singleton si citeste configuratia din `db.properties`.

### 2.2 Repository-uri CRUD

Interfata generica este `Repository<T, ID>` si contine:

```java
void save(T entity);
Optional<T> findById(ID id);
List<T> findAll();
void update(T entity);
void delete(ID id);
```

Repository-uri concrete implementate:

| Repository | Entitate | ID |
|---|---|---|
| `ClientRepository` | `Client` | `String cnp` |
| `ContRepository` | `Cont`, `ContCurent`, `ContEconomii` | `String iban` |
| `CardRepository` | `Card`, `CardDebit`, `CardCredit` | `String numarCard` |
| `TranzactieRepository` | `Tranzactie` | `Long id` |

Toate query-urile folosesc `PreparedStatement` si `try-with-resources`.

### 2.3 Tranzactie JDBC explicita

`JdbcTransferService.transferaCuTranzactie(...)` executa un transfer intre doua conturi direct in baza de date:

1. citeste soldul contului sursa si destinatie;
2. verifica fondurile;
3. actualizeaza soldul sursei;
4. actualizeaza soldul destinatiei;
5. insereaza tranzactia in tabela `transactions`;
6. face `commit` daca totul a mers bine sau `rollback` daca apare o eroare.

### 2.4 Interogari cu JOIN

`RaportRepository` expune 3 rapoarte cu JOIN, mapate in DTO-uri:

| Metoda | Ce combina | DTO |
|---|---|---|
| `clientiCuNumarConturiSiSoldTotal()` | `clients` + `accounts` | `ClientConturiReport` |
| `conturiCuTitularSiNumarCarduri()` | `accounts` + `clients` + `cards` | `ContCarduriReport` |
| `tranzactiiCuTitulari()` | `transactions` + `accounts` + `clients` | `TranzactieDetaliataReport` |

### 2.5 Audit CSV

`AuditService` este Singleton thread-safe si scrie in `audit.csv` in modul append.
Actiunile principale din Etapa I apeleaza auditul:

- `inregistreaza_client`
- `deschide_cont`
- `emite_card`
- `depune_bani`
- `retrage_bani`
- `transfera_bani`
- `cauta_cont_dupa_iban`
- `listeaza_istoric_tranzactii`
- `genereaza_extras_cont`
- `blocheaza_card`

## 3. Cum se compileaza si se ruleaza

Din radacina proiectului:

```bash
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:lib/*" com.pao.proiect.banca.Main
```

`Main.java` executa pe rand cele 10 actiuni din Etapa I, apoi ruleaza si demonstratia pentru Etapa II:

- reseteaza baza de date folosind `schema.sql`;
- salveaza clientii, conturile, cardurile si tranzactiile prin repository-uri;
- demonstreaza `save`, `findById`, `findAll`, `update`, `delete`;
- executa un transfer JDBC cu `commit` / `rollback`;
- afiseaza cele 3 rapoarte cu JOIN;
- scrie actiunile in `audit.csv`.

In IntelliJ IDEA exista configuratia de rulare `Proiect Banca Main`.

## 4. Cum se mapeaza cerintele pe cod

| Cerinta | Unde e implementata |
|---|---|
| README cu 10 actiuni si 8+ tipuri de obiecte | acest fisier |
| Cel putin 8 clase | pachetul `model` |
| `toString`, `equals`, `hashCode` in cel putin 2 clase | `Cont`, `Client` |
| Ierarhie de mostenire pe cel putin 2 niveluri | `Persoana -> Angajat -> Manager` |
| Clasa abstracta sau interfata | `Persoana`, `Cont`, `Card`, `Tranzactionabil` |
| Clasa imutabila | `IBAN` |
| Cel putin 2 exceptii custom | pachetul `exception` |
| Cel putin 2 colectii diferite, dintre care una sortata | `Map<String, Cont>`, `Map<String, Client>`, `TreeSet<Tranzactie>` |
| Cel putin un `Map` | `ClientServiceImpl`, `ContServiceImpl` |
| Cel putin 2 servicii Singleton cu operatii de tip CRUD | pachetul `service` si `serviceImpl` |
| Clasa `Main` pentru testare | `Main.java` |
| Organizare pe pachete | `model`, `service`, `serviceImpl`, `exception` |
| `schema.sql` + `db.properties` + `DatabaseConnection` | `schema.sql`, `resources/db.properties`, `config/DatabaseConnection.java` |
| `Repository<T, ID>` generic | `repository/Repository.java` |
| CRUD pentru 4 entitati | `ClientRepository`, `ContRepository`, `CardRepository`, `TranzactieRepository` |
| `PreparedStatement` + `try-with-resources` | toate clasele din `repository`, `DatabaseInitializer`, `JdbcTransferService` |
| Tranzactie JDBC explicita | `service/JdbcTransferService.java` |
| 3 query-uri cu JOIN | `repository/RaportRepository.java` |
| Audit CSV thread-safe | `service/AuditService.java` |
