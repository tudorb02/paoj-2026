# Aplicatie bancara

## 1. Definirea sistemului

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

## 3. Cum se compileaza si se ruleaza

Din radacina proiectului:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.pao.proiect.banca.Main
```

`Main.java` executa pe rand cele 10 actiuni definite mai sus si afiseaza rezultatul fiecareia.

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
