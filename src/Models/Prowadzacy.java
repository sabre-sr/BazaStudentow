package Models;

import Services.BazaDanych;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Prowadzacy extends Osoba{
    private String przedmiot;
    public Prowadzacy(String imienazwisko, String przedmiot) {
        super(imienazwisko);
        this.przedmiot = przedmiot;
    }

    public String getPrzedmiot() {
        return przedmiot;
    }

    public void wystawOcene(Student student) throws SQLException {
        int id_student = (BazaDanych.bazaDanych.getStudent(student)).getInt("id");
        ResultSet rs = BazaDanych.bazaDanych.getGrade(id_student, this.przedmiot);
        float srednia = srednia(rs);
        rs.updateFloat("ocenakoncowa", srednia);
    }

    private float srednia(ResultSet rs) throws SQLException {
        float srednia = (float) 0.0;
        while (rs.next()) {
            String entry = rs.getString("oceny");
            String[] tokens = entry.split(", ");
            for (String i:tokens)
                srednia+=Float.parseFloat(i);
        }
        return srednia;
    }
}
