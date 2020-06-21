package GUI;

import Models.Student;
import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class FindStudents extends JDialog{
    SearchStudents parent;
    JLabel fraza, tryb_label;
    JTextField szukany;
    JRadioButton nrindeksu, imienazwisko;
    ButtonGroup tryb;
    JButton ok;
    private Student student;

    public FindStudents(SearchStudents parent) {
        this.parent = parent;
        this.setModal(true);
        this.add(this.fraza = new JLabel("Szukana fraza: "));
        this.add(this.szukany = new JTextField(8));
        this.add(this.tryb_label = new JLabel("Szukaj po: "));
        tryb = new ButtonGroup();
        this.add(this.nrindeksu = new JRadioButton("Nr indeksu"));
        this.add(this.imienazwisko = new JRadioButton("Imie i nazwisko"));
        this.nrindeksu.setActionCommand("nrindeksu");
        this.imienazwisko.setActionCommand("imienazwisko");
        tryb.add(nrindeksu);
        tryb.add(imienazwisko);
        this.add(this.ok = new JButton("OK"));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.ok.addActionListener(e -> {
            if (tryb.getSelection().getActionCommand().equals("nrindeksu")) {
                try {
                    student = new Student();
                    student.setNralbumu(Integer.parseInt(szukany.getText()));
                } catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(null, "Nieprawidlowe dane.");
                }
            } else if (tryb.getSelection().getActionCommand().equals("imienazwisko")) {
                student = new Student(szukany.getText());
            } else if (tryb.getSelection() == null) {
                return;
            }
            try {
                parent.resultSet = BazaDanych.bazaDanych.getStudent(student);
                this.dispose();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, ("Błąd: ")+throwables.getMessage());
                throwables.printStackTrace();
            }
        });
        this.setLayout(new FlowLayout());
        this.setSize(122,200);
        this.setVisible(true);
    }
}
