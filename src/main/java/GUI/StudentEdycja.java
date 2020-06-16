package GUI;

import Exceptions.InvalidPESELException;
import Models.Student;
import Services.BazaDanych;


import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class StudentEdycja extends JDialog {
    JLabel imienazwisko_label, pesel_label, rokstudiow_label, nralbumu_label, haslo_label;
    JTextField imienazwisko, pesel, rokstudiow, nralbumu;
    JPasswordField haslo;
    JButton ok, anuluj;
    private final int id;

    public StudentEdycja() {
        this.setModal(true);
        this.add(this.imienazwisko_label = new JLabel("Imie i nazwisko "));
        this.add(this.imienazwisko = new JTextField());
        this.add(this.pesel_label = new JLabel("PESEL: "));
        this.add(this.pesel = new JTextField());
        this.add(this.rokstudiow_label = new JLabel("Rok studiow: "));
        this.add(this.rokstudiow = new JTextField());
        this.add(this.nralbumu_label = new JLabel("Nr albumu: "));
        this.add(this.nralbumu = new JTextField(6));
        this.add(this.haslo_label = new JLabel("Haslo: "));
        this.add(this.haslo = new JPasswordField(8));
        this.add(this.ok = new JButton("OK"));
        this.add(this.anuluj = new JButton("Anuluj"));
        this.id = 0;
        armWindow();
    }

    public StudentEdycja(Student student) {
        this.setModal(true);
        this.add(this.imienazwisko_label = new JLabel("Imie i nazwisko "));
        this.add(this.imienazwisko = new JTextField(student.getImieNazwisko()));
        this.add(this.pesel_label = new JLabel("PESEL: "));
        this.add(this.pesel = new JTextField(student.getPesel()));
        this.add(this.rokstudiow_label = new JLabel("Rok studiow: "));
        this.add(this.rokstudiow = new JTextField(student.getRok_studiow()));
        this.add(this.nralbumu_label = new JLabel("Nr albumu: "));
        this.add(this.nralbumu = new JTextField(Integer.toString(student.getNralbumu()), 6));
        this.add(this.haslo_label = new JLabel("Haslo: "));
        this.add(this.haslo = new JPasswordField(8));
        this.add(this.ok = new JButton("OK"));
        this.add(this.anuluj = new JButton("Anuluj"));
        this.id = student.getId();
        armWindow();
    }

    private void armWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.pack();
        this.setSize(200, 250);
        this.setVisible(true);
        this.ok.addActionListener(e -> {
            try {
                Student temp = new Student(imienazwisko.getText(), pesel.getText(), Integer.parseInt(nralbumu.getText()),Integer.parseInt(rokstudiow.getText()));
                BazaDanych.bazaDanych.addStudent(temp, haslo.getPassword());
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Pola zawierają nieprawidłowe dane. Popraw je.");
            } catch (InvalidPESELException peselException) {
                JOptionPane.showMessageDialog(null, "Numer PESEL jest nieprawidłowy.");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException noSuchAlgorithmException) {
                JOptionPane.showMessageDialog(null, "Problem z szyfrowaniem.");
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Problem z dodaniem do bazy danych.");
                throwables.printStackTrace();
            }

        });
        this.anuluj.addActionListener(e -> {
            this.dispose();
        });
    }

    public static void main(String[] args) {
        try {
            new StudentEdycja(new Student("", "",0, 0));
        } catch (InvalidPESELException ignored) {
        }
    }
}
