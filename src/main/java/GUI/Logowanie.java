package GUI;

import Exceptions.InvalidPESELException;
import Models.Osoba;
import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Arrays;

public class Logowanie extends JFrame {
    JTextField login;
    JPasswordField haslo;
    JLabel login_label, haslo_label;
    JRadioButton student, prowadzacy, dziekanat;
    ButtonGroup trybDostepu;
    JButton ok, zamknij;

    public Logowanie() {
        this.add(login_label = new JLabel("Login: "));
        this.add(login = new JTextField(15));
        this.add(haslo_label = new JLabel("Hasło: "));
        this.add(haslo = new JPasswordField(15));
        this.add(student = new JRadioButton("Student"));
        this.add(prowadzacy = new JRadioButton("Prowadzący"));
        this.add(dziekanat = new JRadioButton("Dziekanat"));
        this.student.setActionCommand("studenci");
        this.prowadzacy.setActionCommand("prowadzacy");
        this.dziekanat.setActionCommand("dziekanat");
        this.trybDostepu = new ButtonGroup();
        this.trybDostepu.add(student);
        this.trybDostepu.add(prowadzacy);
        this.trybDostepu.add(dziekanat);
        this.setSize(500, 130);
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(this.ok = new JButton("OK"));
        this.add(this.zamknij = new JButton("Zamknij"));
        this.zamknij.addActionListener(e -> System.exit(0));
        var ref = new Object() {
            Osoba uzytkownik = null;
        };
        this.ok.addActionListener(e -> {
            System.out.println(Arrays.toString(this.haslo.getPassword()));
            try {
                ref.uzytkownik = BazaDanych.bazaDanych.logIn(this.login.getText(), Arrays.toString(this.haslo.getPassword()), this.trybDostepu.getSelection().getActionCommand());
                if (ref.uzytkownik == null) {
                    JOptionPane.showMessageDialog(null, "Błędne hasło");
                } else {
                    JOptionPane.showMessageDialog(null, "Zalogowano.");
                    ref.uzytkownik.openWindow();
                    this.dispose();
                }
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd dostępu do bazy danych");
                throwables.printStackTrace();
            } catch (InvalidKeySpecException | NoSuchAlgorithmException invalidKeySpecException) {
                JOptionPane.showMessageDialog(null, "Błąd z dostępem do funkcji szyfrującej.");
                invalidKeySpecException.printStackTrace();
            }
            catch (InvalidPESELException invalidPESELException) {
                JOptionPane.showMessageDialog(null, "Na twoim koncie wprowadzony jest nieprawidlowy numer Pesel. Skontaktuj się z administracją.");
            }
        });
    }
}
