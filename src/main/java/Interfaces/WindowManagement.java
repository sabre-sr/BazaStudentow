package Interfaces;

import java.sql.SQLException;

/**
 * Interfejs pozwalający na przypisanie odpowiednich okien menu głównego dla każdego typu zalogowanego użytkownika.
 * @see Models.Osoba
 */
public interface WindowManagement {
    /**
     * Metoda uruchamiająca menu główne zalogowanego użytkownika.
     * @throws SQLException generyczny błąd SQL
     */
    void openWindow() throws SQLException;
}
