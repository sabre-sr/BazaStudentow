package GUI;

import Services.BazaDanych;

import javax.swing.*;
import java.sql.SQLException;

public class StudentGUI {
    private Models.Student uzytkownik;
    JLabel imienazwisko, pesel;
    JButton haslo_zmiana;
    JTable oceny;

    public StudentGUI(Models.Student student) throws SQLException {
        this.imienazwisko = new JLabel("Zalogowany jako: " + uzytkownik.getImieNazwisko());
        this.pesel = new JLabel("PESEL: " + uzytkownik.getPesel());
        this.oceny = new Oceny(BazaDanych.bazaDanych.getGrades(uzytkownik.getId()));
    }
}