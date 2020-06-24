package Models;

import Exceptions.InvalidPESELException;
import GUI.ProwadzacyGUI;
import Services.BazaDanych;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasa reprezentująca prowadzącego zajęcia.
 * <p>Prowadzący może dodawać i usuwać studentów z listy obecności, wpisywać im oceny i wystawiać oceny końcowe.</p>
 */
public class Prowadzacy extends Osoba{
    /**
     * Przedmiot wykładany przez prowadzącego.
     */
    private final String przedmiot;
    public Prowadzacy(String imienazwisko, String przedmiot) {
        super(imienazwisko);
        this.przedmiot = przedmiot;
    }

    /**
     * Loguje użytkownika klasy Prowadzacy.
     * @param resultSet Rezultat z bazy danych, zawierający dane logującego się użytkownika.
     * @return Klasa Prowadzacy, reprezentująca zalogowanego użytkownika.
     * @throws SQLException generyczny błąd SQL
     */
    public static Prowadzacy createProwadzacy(ResultSet resultSet) throws SQLException {
        Prowadzacy prowadzacy = new Prowadzacy(resultSet.getString("imienazwisko"), resultSet.getString("przedmiot"));
        prowadzacy.setId(resultSet.getInt("id"));
        return prowadzacy;
    }

    /**
     * @return Zwraca nazwę przedmiotu.
     */
    public String getPrzedmiot() {
        return przedmiot;
    }

    public void wystawOcene(Student student) throws SQLException {
        int id_student = (BazaDanych.bazaDanych.getStudent(student)).getInt("id");
        ResultSet rs = BazaDanych.bazaDanych.getGrade(id_student, this.przedmiot);
        float srednia = srednia(rs.getString("oceny"));
        rs.updateFloat("ocenakoncowa", srednia);
    }

    /**
     * Oblicza średnią ocen studenta. Jest sugestią dla prowadzącego.
     * @param oceny String zawierający oceny ucznia
     * @return Średnia arytmetyczna ocen
     */
    public static float srednia(String oceny) {
        float srednia = (float) 0.0;
        String[] tokens = oceny.split(" ");
        int liczba=0;
        for (String i:tokens) {
            srednia += Float.parseFloat(i);
            liczba++;
        }
        if (liczba == 0)
            return (float) 0.0;
        return srednia/liczba;
    }
    /**
     * Otwiera menu główne prowadzącego.
     * @see Interfaces.WindowManagement
     */
    @Override
    public void openWindow() throws SQLException {
        new ProwadzacyGUI(this);
    }
}
