package Models;

import Exceptions.InvalidPESELException;
import Services.BazaDanych;
import Utils.PESEL;
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

    public static Student createStudent(ResultSet resultSet) throws SQLException, InvalidPESELException {
        Student student = new Student(resultSet.getString("imienazwisko"), resultSet.getString("pesel"), resultSet.getInt("nralbumu"), resultSet.getInt("rokstudiow"));
        student.setId(resultSet.getInt("id"));
        return student;
    }

    public Student(String imienazwisko, String pesel, int nralbumu, int rok_studiow) throws InvalidPESELException {
        super(imienazwisko);
        if (PESEL.PESELValid(pesel))
            this.pesel = pesel;
        else throw new InvalidPESELException("Nieprawidlowy Tools.PESEL");
        this.nralbumu = nralbumu;
        this.rok_studiow = rok_studiow;
    }

    @Override
    public void openWindow() throws SQLException {
        new GUI.Student(this);
    }

    public String getGrades() throws SQLException {
        ResultSet studentQuery = Services.BazaDanych.bazaDanych.getStudent(this);
        ArrayList<ImmutablePair<String, ResultSet>> oceny = BazaDanych.bazaDanych.getGrades(studentQuery.getInt("id"));
        StringBuilder out = new StringBuilder();
        for (ImmutablePair<String, ResultSet> i : oceny) {
            out.append(i.left).append(": ").append(i.right).append('\n');
        }
        return out.toString();
    }


}
