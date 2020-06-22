package GUI;

import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentGUI extends JFrame {
    JLabel imienazwisko, pesel;
    private final Models.Student uzytkownik;

    public StudentGUI(Models.Student student) throws SQLException {
        this.uzytkownik = student;
        this.add(this.imienazwisko = new JLabel("Zalogowany jako: " + uzytkownik.getImieNazwisko()));
        this.add(this.pesel = new JLabel("PESEL: " + uzytkownik.getPesel()));
        this.getContentPane().add(new OcenyUcznia(BazaDanych.bazaDanych.getGrades(uzytkownik.getId())));
        this.pack();
        this.setSize(500, 500);
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
