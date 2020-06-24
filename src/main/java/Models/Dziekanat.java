package Models;

import GUI.DziekanatUI;
import Services.BazaDanych;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasa reprezentująca użytkownika z dziekanatu.
 * <p>Dziekanat ma pełną władzę nad pozostałymi użytkownikami oraz ma największy dostęp do bazy danych.</p>
 * <p>Dziekanat może dodawać i usuwać studentów, prowadzących oraz przedmioty.</p>
 */
public class Dziekanat extends Osoba {
    public Dziekanat(String imienazwisko) {
        super(imienazwisko);
    }

    /**
     * Loguje użytkownika klasy Dziekanat.
     * @param resultSet Rezultat z bazy danych, zawierający dane logującego się użytkownika.
     * @return Klasa Dziekanat, reprezentująca zalogowanego użytkownika
     * @throws SQLException generyczny błąd SQL.
     */
    public static Dziekanat createDziekanat(ResultSet resultSet) throws SQLException {
        Dziekanat dziekanat = new Dziekanat(resultSet.getString("imienazwisko"));
        dziekanat.setId(resultSet.getInt("id"));
        return dziekanat;
    }

    /**
     * Dodaje nowego studenta do bazy danych.
     * @param s Dodawany student
     * @param haslo Hasło dostępu studenta
     * @throws InvalidKeySpecException wyjątek zwracający nieprawidłowość klucza szyfrującego. Nie powinien wystąpić
     * @throws SQLException generyczny błąd SQL
     * @throws NoSuchAlgorithmException wyjątek zwracający niedostępność algorytmu szyfrującego. Nie powinien wystąpić
     */
    public void addStudent(Student s, char[] haslo) throws InvalidKeySpecException, SQLException, NoSuchAlgorithmException {
        BazaDanych.bazaDanych.addStudent(s, haslo);
    }


    /**
     * Dodaje nowego prowadzącego do bazy danych.
     * @param p Dodawany prowadzący
     * @param haslo Hasło dostępu prowadzącego
     * @throws InvalidKeySpecException wyjątek zwracający nieprawidłowość klucza szyfrującego. Nie powinien wystąpić
     * @throws SQLException generyczny błąd SQL
     * @throws NoSuchAlgorithmException wyjątek zwracający niedostępność algorytmu szyfrującego. Nie powinien wystąpić
     */
    public void addProwadzacy(Prowadzacy p, char[] haslo) throws InvalidKeySpecException, SQLException, NoSuchAlgorithmException {
        BazaDanych.bazaDanych.addProwadzacy(p, haslo);
    }

    /**
     * Dodaje nowy przedmiot do bazy danych.
     * @param przedmiot Nazwa przedmiotu
     * @throws SQLException generyczny błąd SQL
     */
    public void addPrzedmiot(String przedmiot) throws SQLException {
        BazaDanych.bazaDanych.addPrzedmiot(przedmiot);
    }

    /**
     * Usuwa studenta z bazy danych.
     * @param student usuwany student
     * @throws SQLException generyczny błąd SQL
     */
    public void removeStudent(Student student) throws SQLException {
        ResultSet query = BazaDanych.bazaDanych.getStudent(student);
        BazaDanych.bazaDanych.removeStudent(query.getInt("id"));
    }

    /**
     * Otwiera menu główne dziekanatu.
     * @see Interfaces.WindowManagement
     */
    @Override
    public void openWindow() {
        new DziekanatUI();
    }
}
