package Models;

import Services.BazaDanych;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dziekanat extends Osoba {
    public Dziekanat(String imienazwisko) {
        super(imienazwisko);
    }
    public void addStudent(Student s, String haslo) throws InvalidKeySpecException, SQLException, NoSuchAlgorithmException {
        BazaDanych.bazaDanych.addStudent(s,haslo);
    }
    public void graduateStudent(Student s) throws SQLException {
        ResultSet studentQuery = BazaDanych.bazaDanych.getStudent(s);
        studentQuery.updateInt("rokstudiow", studentQuery.getInt("rokstudiow")+1) ;
    }
    public boolean addProwadzacy(Prowadzacy p, String haslo) throws InvalidKeySpecException, SQLException, NoSuchAlgorithmException {
        return BazaDanych.bazaDanych.addProwadzacy(p,haslo);
    }
    public void addPrzedmiot(String przedmiot) throws SQLException {
        BazaDanych.bazaDanych.addPrzedmiot(przedmiot);
    }
    public void removeStudent(Student student) throws SQLException {
        ResultSet query = BazaDanych.bazaDanych.getStudent(student);
        BazaDanych.bazaDanych.removeStudent(query);
    }

    public static void main(String[] args) throws InvalidKeySpecException, SQLException, NoSuchAlgorithmException {
        System.out.println(BazaDanych.bazaDanych.addDziekanat());
//        System.out.println(BazaDanych.bazaDanych.logIn("root", "root", "dziekanat"));
    }
}
