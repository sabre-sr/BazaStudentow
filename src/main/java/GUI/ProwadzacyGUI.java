package GUI;

import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ProwadzacyGUI extends JFrame {
    private Models.Prowadzacy uzytkownik;
    JLabel imienazwisko, przedmiot;
    JButton dodajUcznia, edytujOcene;

    public ProwadzacyGUI(Models.Prowadzacy uzytkownik) throws SQLException {
        this.uzytkownik = uzytkownik;
        this.add(imienazwisko = new JLabel("Zalogowany użytkownik: " + this.uzytkownik.getImienazwisko()));
        this.add(this.przedmiot = new JLabel("Przedmiot: " + this.uzytkownik.getPrzedmiot()));
        this.add(new OcenyGrupy(BazaDanych.bazaDanych.getGradeList(this.uzytkownik.getPrzedmiot()), BazaDanych.bazaDanych.getStudentIDList(this.uzytkownik.getPrzedmiot()), this.uzytkownik.getPrzedmiot()));
        this.add(this.edytujOcene = new JButton("Edytuj ocenę"));
        this.add(this.dodajUcznia = new JButton("Dodaj ucznia"));
        this.setSize(500, 600);
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
