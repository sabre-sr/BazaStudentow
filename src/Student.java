import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Student extends Osoba {
    private String pesel;
    private int nralbumu;
    private int rok_studiow;

    public String getPesel() {
        return pesel;
    }

    public int getRok_studiow() {
        return rok_studiow;
    }

    public int getNralbumu() {
        return nralbumu;
    }

    public String getImieNazwisko() {
        return super.getImienazwisko();
    }
    public Student(String imienazwisko, String pesel, int nralbumu, int rok_studiow) throws InvalidPESELException {
        super(imienazwisko);
        if (PESEL.PESELValid(pesel))
            this.pesel = pesel;
        else throw new InvalidPESELException("Nieprawidlowy PESEL");
        this.nralbumu = nralbumu;
        this.rok_studiow = rok_studiow;
    }
    public String getGrades() throws SQLException {
        ResultSet studentQuery = BazaDanych.getStudent(this);
        ArrayList<ImmutablePair<String, String>> oceny = BazaDanych.getGrades(studentQuery.getInt("id"));
        StringBuilder out = new StringBuilder();
        for (ImmutablePair<String, String> i:oceny) {
            out.append(i.left).append(": ").append(i.right);
        }
        return out.toString();
    }


}
