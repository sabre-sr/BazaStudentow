package GUI;

import Models.Prowadzacy;
import Services.BazaDanych;
import org.apache.commons.lang3.tuple.MutablePair;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditGrades extends JDialog implements Serializable {

    private final JButton ok;
    private final JLabel nazwisko, oceny_label;
    private final int studentId;
    private final JTextField srednia_input;
    private final JLabel srednia;
    private final ArrayList<JTextField> ocenyField;
    private final String przedmiot;

    public EditGrades(int studentId, String imienazwisko, String oceny, String ocenakoncowa, String przedmiot) {
        this.setModal(true);
        this.setSize(300,500);
        this.studentId = studentId;
        this.przedmiot = przedmiot;
        this.add(this.nazwisko = new JLabel(imienazwisko));
        this.add(this.oceny_label = new JLabel("Oceny: "));
        this.ocenyField = new ArrayList<JTextField>();
        String[] tokens = oceny.split(" ");
        for (String i : tokens) {
            ocenyField.add(new JTextField(i, 3));
            this.add(ocenyField.get(ocenyField.size() - 1));
        }
        this.add(this.srednia = new JLabel("Ocena koncowa: (Obliczona srednia: " + Prowadzacy.srednia(oceny) + ")"));
        this.add(this.srednia_input = new JTextField(ocenakoncowa, 3));
        this.add(this.ok = new JButton("Ok"));
        this.setLayout(new FlowLayout());
        ok.addActionListener(e -> {
            StringBuilder temp = new StringBuilder();
            for (JTextField i : ocenyField) {
                try {
                    Float.parseFloat(i.getText());
                    temp.append(i.getText()).append(" ");
                } catch (NumberFormatException ignored) {
                }
            }
            MutablePair<String, String> pair = new MutablePair<String, String>();
            pair.left = temp.toString();
            try {
                Float.parseFloat(srednia_input.getText());
                pair.right = srednia_input.getText();
            } catch (NumberFormatException err) {
                pair.right = ocenakoncowa;
            }
            try {
                BazaDanych.bazaDanych.updateGrades(studentId, pair.left, pair.right, this.przedmiot);
                this.dispose();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null,"Wystąpił problem z aktualizacją danych.");
                throwables.printStackTrace();
            }
        });
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new EditGrades(0, "", "3.0 3.5", "", "");
    }
}