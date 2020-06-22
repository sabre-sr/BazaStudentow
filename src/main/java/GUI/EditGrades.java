package GUI;

import Models.Prowadzacy;
import Services.BazaDanych;
import Utils.WrapLayout;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditGrades extends JDialog implements Serializable {

    private final JButton ok, dodaj, odswiez;
    private final JLabel nazwisko, oceny_label;
    private final int studentId;
    private final JTextField srednia_input;
    private final JLabel srednia;
    private final ArrayList<JTextField> ocenyField;
    JPanel oceny;
    private final String przedmiot;

    public EditGrades(int studentId, String imienazwisko, String oceny, String ocenakoncowa, String przedmiot) {
        this.setModal(true);
        this.setSize(400,500);
        this.studentId = studentId;
        this.przedmiot = przedmiot;
        this.add(this.nazwisko = new JLabel(imienazwisko));
        this.oceny = new JPanel();
        this.oceny.add(this.oceny_label = new JLabel("Oceny: "));
        this.ocenyField = new ArrayList<>();
        if (oceny == null){
            oceny = "0.0";
        }
        String[] tokens = oceny.split(" ");
        for (String i : tokens) {
            ocenyField.add(new JTextField(i, 3));
            this.oceny.add(ocenyField.get(ocenyField.size() - 1));
        }
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(this.srednia = new JLabel("Ocena koncowa: (Obliczona srednia: " + Prowadzacy.srednia(oceny) + ")"));
        this.add(this.srednia_input = new JTextField(ocenakoncowa, 3));
        this.add(this.ok = new JButton("Ok"));
        this.add(this.odswiez = new JButton("Odśwież średnią"));
        this.add(this.dodaj = new JButton("Dodaj pole"));
        this.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        this.add(this.oceny);
        this.setLayout(new FlowLayout());
        this.oceny.setLayout(new WrapLayout());
        this.oceny.setSize(300, 500);
        ok.addActionListener(e -> {
            String temp = getGradeString();
            MutablePair<String, String> pair = new MutablePair<>();
            pair.left = temp;
            try {
                Float.parseFloat(srednia_input.getText());
                pair.right = srednia_input.getText();
            } catch (NumberFormatException err) {
                pair.right = ocenakoncowa;
            }
            if (pair.right == null)
                pair.right = " ";
            try {
                BazaDanych.bazaDanych.updateGrades(studentId, pair.left, pair.right, this.przedmiot);
                this.dispose();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null,"Wystąpił problem z aktualizacją danych.");
                throwables.printStackTrace();
            }
        });
        this.dodaj.addActionListener(e -> {
            ocenyField.add(new JTextField(3));
            this.oceny.add(ocenyField.get(ocenyField.size() - 1));
            this.oceny.setSize(300, 500);
            this.oceny.revalidate();
        });
        this.odswiez.addActionListener(e -> {
            String temp = getGradeString();
            srednia.setText("Ocena koncowa: (Obliczona srednia: " + Prowadzacy.srednia(temp) + ")");
        });
        this.setVisible(true);
    }

    @NotNull
    private String getGradeString() {
        StringBuilder temp = new StringBuilder();
        for (JTextField i : ocenyField) {
            try {
                Float.parseFloat(i.getText());
                temp.append(i.getText()).append(" ");
            } catch (NumberFormatException ignored) {
            }
        }
        return temp.toString();
    }
}