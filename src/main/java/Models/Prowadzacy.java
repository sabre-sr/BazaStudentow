package Models;

import Exceptions.InvalidPESELException;
import GUI.ProwadzacyGUI;
import Services.BazaDanych;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Prowadzacy extends Osoba{
    private String przedmiot;
    public Prowadzacy(String imienazwisko, String przedmiot) {
        super(imienazwisko);
        this.przedmiot = przedmiot;
    }

    public static Prowadzacy createProwadzacy(ResultSet resultSet) throws SQLException, InvalidPESELException {
        Prowadzacy prowadzacy = new Prowadzacy(resultSet.getString("imienazwisko"), resultSet.getString("przedmiot"));
        prowadzacy.setId(resultSet.getInt("id"));
        return prowadzacy;
    }
    public String getPrzedmiot() {
        return przedmiot;
    }

    public void wystawOcene(Student student) throws SQLException {
        int id_student = (BazaDanych.bazaDanych.getStudent(student)).getInt("id");
        ResultSet rs = BazaDanych.bazaDanych.getGrade(id_student, this.przedmiot);
        float srednia = srednia(rs.getString("oceny"));
        rs.updateFloat("ocenakoncowa", srednia);
    }

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

    @Override
    public void openWindow() throws SQLException {
        new ProwadzacyGUI(this);
    }
}
