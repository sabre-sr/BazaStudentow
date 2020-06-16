package GUI;

import Exceptions.InvalidPESELException;
import Models.Student;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class StudentEdycja extends JFrame {
    JLabel imienazwisko_label, pesel_label, rokstudiow_label, nralbumu_label, haslo_label;
    JTextField imienazwisko, pesel, rokstudiow, nralbumu;
    JPasswordField haslo;
    JButton ok, anuluj;

    public StudentEdycja(Student student) {
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
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.pack();
        this.setSize(200, 250);
        this.setVisible(true);
        this.ok.addActionListener(e -> {

        });
        this.anuluj.addActionListener(e -> {
            this.dispose();
        });
    }

    public static void main(String[] args) {
        try {
            new StudentEdycja(new Student("Jankowalski", "96052400677", 4, 5));
        } catch (InvalidPESELException ignored) {
        }
    }
}
