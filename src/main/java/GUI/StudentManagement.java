package GUI;

import Exceptions.InvalidPESELException;
import Models.Student;
import Services.BazaDanych;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Okno zarządzania studentami przez dziekanat.
 * <p>Pozwala na ich dodawanie, usuwanie, edycje ich danych osobowych, a także wyświetlanie ich karty ocen, obliczanie średniej końcowej oraz promocję studentów.</p>
 */
public class StudentManagement extends JFrame {
    private final JButton dodaj, edytuj, karta, usun, graduate, srednia;
    private final StudentLista tabelka;

    public StudentManagement() throws SQLException {
        super("Zarządzanie studentami");
        this.add(this.tabelka = new StudentLista());
        this.add(this.dodaj = new JButton("Dodaj"));
        this.add(this.edytuj = new JButton("Edytuj"));
        this.add(this.graduate = new JButton("Zaliczenie roku"));
        this.add(usun = new JButton("Usuń"));
        this.add(this.karta = new JButton("Karta studenta"));
        this.add(this.srednia = new JButton("Średnia"));
        this.setLayout(new FlowLayout());
        this.setSize(470, 540);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dodaj.addActionListener(e -> {
            try {
                new StudentEdycja();
                tabelka.loadData();

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Wystąpił problem z edycją danych");
                throwables.printStackTrace();
            }
        });
        this.edytuj.addActionListener(e -> {
            try {
                int row = tabelka.table.getSelectedRow();
                int nralbumu = Integer.parseInt(String.valueOf(tabelka.table.getValueAt(row, 4)));
                Student temp = new Student();
                temp.setNralbumu(nralbumu);
                if (row != -1) {
                    new StudentEdycja(Student.createStudent(BazaDanych.bazaDanych.getStudent(temp)));
                    tabelka.loadData();
                    tabelka.tableModel.fireTableDataChanged();
                }
            } catch (InvalidPESELException peselException) {
                JOptionPane.showMessageDialog(null, "Problem z nr PESEL");
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Problem z bazą danych.");
                throwables.printStackTrace();
            }
        });
        this.usun.addActionListener(e -> {
            int row = tabelka.table.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(tabelka.table.getValueAt(row, 0)));
            try {
                BazaDanych.bazaDanych.removeStudent(id);
                tabelka.loadData();
                tabelka.tableModel.fireTableDataChanged();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Problem z bazą danych.");
                throwables.printStackTrace();
            }
        });
        this.karta.addActionListener(e -> {
            int row = tabelka.table.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(tabelka.table.getValueAt(row, 0)));
            Student temp = new Student();
            temp.setId(id);
            try {
                new StudentGUI(Student.createStudent(BazaDanych.bazaDanych.getStudent(temp)));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (InvalidPESELException peselException) {
                JOptionPane.showMessageDialog(null, "Nr Pesel jest nieprawidlowy. Nie mozna otworzyc karty.");
            }
        });
        this.graduate.addActionListener(e -> {
            int row = tabelka.table.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(tabelka.table.getValueAt(row, 0)));
            int grade = Integer.parseInt(String.valueOf(tabelka.table.getValueAt(row, 3)));
            try {
                BazaDanych.bazaDanych.updateYear(id, grade);
                tabelka.loadData();
                tabelka.tableModel.fireTableDataChanged();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, ("Problem z bazą danych: ") + throwables.getMessage());
                throwables.printStackTrace();
            }
        });
        this.srednia.addActionListener(e -> {
            float suma = (float) 0.0;
            int liczbaocen = 0;
            int row = tabelka.table.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(tabelka.table.getValueAt(row, 0)));
            try {
                ArrayList<ImmutablePair<String, ResultSet>> oceny = BazaDanych.bazaDanych.getGrades(id);
                for (ImmutablePair<String, ResultSet> i : oceny) {
                    i.right.next();
                    float temp = Float.parseFloat(i.right.getString("ocenakoncowa"));
                    suma += temp;
                    liczbaocen++;
                    i.right.close();
                }
                if (liczbaocen == 0) {
                    suma = (float) 0.0;
                } else suma /= liczbaocen;
                JOptionPane.showMessageDialog(null, "Srednia: " + suma);
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, ("Problem z bazą danych: ") + throwables.getMessage());
                throwables.printStackTrace();
            }
        });
        this.setVisible(true);
    }
}
