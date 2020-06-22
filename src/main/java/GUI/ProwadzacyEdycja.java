package GUI;

import Models.Prowadzacy;
import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Okno edycji prowadzącego.
 */
public class ProwadzacyEdycja extends JDialog {
    JLabel imienazwisko_label, przedmiot_label, haslo_label;
    JTextField imienazwisko, przedmiot;
    JPasswordField haslo;
    JButton ok, anuluj;
    private int id;

    public ProwadzacyEdycja() {
        new ProwadzacyEdycja(0);
    }

    /**
     * Konstruktor okna
     * @param id numer ID prowadzącego w bazie danych
     */
    public ProwadzacyEdycja(int id) {
        this.setModal(true);
        this.id = id;
        this.add(this.imienazwisko_label = new JLabel("Imie i nazwisko: "));
        this.add(this.imienazwisko = new JTextField(7));
        this.add(this.przedmiot_label = new JLabel("Przedmiot: "));
        this.add(this.przedmiot = new JTextField(7));
        this.add(this.haslo_label = new JLabel("Hasło: "));
        this.add(this.haslo = new JPasswordField(8));
        this.add(this.ok = new JButton("OK"));
        this.add(this.anuluj = new JButton("Anuluj"));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.pack();
        this.setSize(200, 150);
        this.anuluj.addActionListener(e -> this.dispose());
        this.ok.addActionListener(e -> {
            try {
                Prowadzacy temp = new Prowadzacy(this.imienazwisko.getText(), this.przedmiot.getText());
                if (this.id == 0) {
                    BazaDanych.bazaDanych.addProwadzacy(temp, Arrays.toString(haslo.getPassword()).toCharArray());
                } else {
                    if (haslo.getPassword().length == 0) {
                        BazaDanych.bazaDanych.editProwadzacy(temp, new char[]{'\0'});
                        this.dispose();
                    } else
                        BazaDanych.bazaDanych.editProwadzacy(temp, Arrays.toString(haslo.getPassword()).toCharArray());
                }
                this.dispose();
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Pola zawierają nieprawidłowe dane. Popraw je.");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException noSuchAlgorithmException) {
                JOptionPane.showMessageDialog(null, "Problem z szyfrowaniem.");
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Problem z dodaniem do bazy danych.");
                throwables.printStackTrace();
            }
        });
        this.setVisible(true);
    }
}
