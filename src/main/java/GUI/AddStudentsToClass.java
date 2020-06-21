package GUI;

import Models.Student;
import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddStudentsToClass extends JDialog {
    SearchStudents wyniki;
    JButton dodaj;
    public AddStudentsToClass(String przedmiot) throws SQLException {
        this.setModal(true);
        this.add(this.wyniki = new SearchStudents());
        this.setSize(400,400);
        this.add(this.dodaj = new JButton("Dodaj"));
        this.setLayout(new FlowLayout());
        this.dodaj.addActionListener(e -> {
            int row = this.wyniki.tabelka.table.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(this.wyniki.tabelka.table.getValueAt(row,0)));
            Student temp = new Student();
            temp.setId(id);
            try {
                BazaDanych.bazaDanych.addStudentToClass(temp, przedmiot);
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, ("Błąd: " + throwables.getMessage()));
                throwables.printStackTrace();
            }
        });
        this.setVisible(true);
    }
}
