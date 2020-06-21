package GUI;

import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ProwadzacyGUI extends JFrame {
    private final Models.Prowadzacy uzytkownik;
    JLabel imienazwisko, przedmiot;
    JButton dodajUcznia, edytujOcene;
    OcenyGrupy tabelka;

    public ProwadzacyGUI(Models.Prowadzacy uzytkownik) throws SQLException {
        this.uzytkownik = uzytkownik;
        this.add(imienazwisko = new JLabel("Zalogowany użytkownik: " + this.uzytkownik.getImienazwisko()));
        this.add(this.przedmiot = new JLabel("Przedmiot: " + this.uzytkownik.getPrzedmiot()));
        this.add(this.tabelka = new OcenyGrupy(BazaDanych.bazaDanych.getGradeList(this.uzytkownik.getPrzedmiot()), BazaDanych.bazaDanych.getStudentIDList(this.uzytkownik.getPrzedmiot()), this.uzytkownik.getPrzedmiot()));
        this.add(this.edytujOcene = new JButton("Edytuj ocenę"));
        this.add(this.dodajUcznia = new JButton("Dodaj ucznia"));
        this.setSize(500, 600);
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.edytujOcene.addActionListener(e -> {
            int row = tabelka.table.getSelectedRow();
            int id = (int) tabelka.table.getValueAt(row,0);
            String imienazwisko = (String) tabelka.table.getValueAt(row, 1);
            String oceny = (String) tabelka.table.getValueAt(row, 2);
            String ocenakoncowa = (String) tabelka.table.getValueAt(row, 3);
            //[0]-ID [1]-Nazwisko [2]-Oceny [3]-Ocena Koncowa
            try {
                new EditGrades(id, imienazwisko, oceny, ocenakoncowa, BazaDanych.bazaDanych.getNazwaTabeli(uzytkownik.getPrzedmiot()));
                this.tabelka.loadData(BazaDanych.bazaDanych.getGradeList(this.uzytkownik.getPrzedmiot()), BazaDanych.bazaDanych.getStudentIDList(this.uzytkownik.getPrzedmiot()));
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, ("Problem z bazą danych: ")+throwables.getMessage());
                throwables.printStackTrace();
            }
        });
        this.dodajUcznia.addActionListener(e -> {
            try {
                new AddStudentsToClass(uzytkownik.getPrzedmiot());
                this.tabelka.loadData(BazaDanych.bazaDanych.getGradeList(this.uzytkownik.getPrzedmiot()), BazaDanych.bazaDanych.getStudentIDList(this.uzytkownik.getPrzedmiot()));
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, ("Problem z bazą danych: ")+throwables.getMessage());
                throwables.printStackTrace();
            }
        });
    }
}
