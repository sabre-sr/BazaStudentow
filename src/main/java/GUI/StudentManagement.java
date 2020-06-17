package GUI;

import Exceptions.InvalidPESELException;
import Models.Student;
import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentManagement extends JFrame {
    private StudentLista tabelka;
    private final JButton znajdz, dodaj, edytuj, karta, usun;

    public StudentManagement() throws SQLException {
        super("Zarządzanie studentami");
        this.add(this.tabelka = new StudentLista());
        this.add(this.dodaj = new JButton("Dodaj"));
        this.add(this.znajdz = new JButton("Znajdź"));
        this.add(this.edytuj = new JButton("Edytuj"));
        this.add(usun = new JButton("Usuń"));
        this.add(this.karta = new JButton("Karta studenta"));
        this.setLayout(new FlowLayout());
        this.setSize(470, 520);
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
        this.setVisible(true);
    }


    public static void main(String[] args) throws SQLException {
        new StudentManagement();
    }
}
