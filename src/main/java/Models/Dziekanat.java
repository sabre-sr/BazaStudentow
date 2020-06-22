package Models;

import GUI.DziekanatUI;
import Services.BazaDanych;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class Dziekanat extends Osoba {
    public Dziekanat(String imienazwisko) {
        super(imienazwisko);
    }

    public static Dziekanat createDziekanat(ResultSet resultSet) throws SQLException {
        Dziekanat dziekanat = new Dziekanat(resultSet.getString("imienazwisko"));
        dziekanat.setId(resultSet.getInt("id"));
        return dziekanat;
    }

    public void addStudent(Student s, char[] haslo) throws InvalidKeySpecException, SQLException, NoSuchAlgorithmException {
        BazaDanych.bazaDanych.addStudent(s, haslo);
    }


    public void addProwadzacy(Prowadzacy p, char[] haslo) throws InvalidKeySpecException, SQLException, NoSuchAlgorithmException {
        BazaDanych.bazaDanych.addProwadzacy(p, haslo);
    }

    public void addPrzedmiot(String przedmiot) throws SQLException {
        BazaDanych.bazaDanych.addPrzedmiot(przedmiot);
    }

    public void removeStudent(Student student) throws SQLException {
        ResultSet query = BazaDanych.bazaDanych.getStudent(student);
        BazaDanych.bazaDanych.removeStudent(query.getInt("id"));
    }

    @Override
    public void openWindow() {
        new DziekanatUI();
    }
}
