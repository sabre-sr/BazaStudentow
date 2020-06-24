package Models;

import Interfaces.WindowManagement;

/**
 * Abstrakcyjny model użytkownika programu.
 * <p>W zależności od rodzaju dostępu dzieli się na: dziekanat, prowadzącego i studenta. Pozwala otworzyć odpowiednie menu główne.</p>
 * @see Dziekanat
 * @see Prowadzacy
 * @see Student
 */
public abstract class Osoba implements WindowManagement {
    /**
     * Identyfikator osoby w bazie danych.
     */
    private int id = 0;

    /**
     * Imie i nazwisko użytkownika
     */
    private final String imienazwisko;

    public Osoba(String imienazwisko) {
        this.imienazwisko = imienazwisko;
    }

    public String getImienazwisko() {
        return imienazwisko;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
