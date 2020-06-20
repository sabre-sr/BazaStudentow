package GUI;

import Exceptions.InvalidPESELException;
import Models.Student;
import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class PrzedmiotManagement extends JFrame {
    private PrzedmiotLista tabelka;
    private final JButton dodaj, usun;

    public PrzedmiotManagement() throws SQLException {
        this.add(this.tabelka = new PrzedmiotLista());
        this.add(dodaj = new JButton("Dodaj"));
        this.add(usun = new JButton("Usuń"));
        this.setLayout(new FlowLayout());
        this.setSize(470, 520);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dodaj.addActionListener(e -> {
            try {
                new PrzedmiotEdycja();
                tabelka.loadData();

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Wystąpił problem z edycją danych");
                throwables.printStackTrace();
            }
        });
        this.usun.addActionListener(e -> {
            int row = tabelka.table.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(tabelka.table.getValueAt(row, 0)));
            try {
                BazaDanych.bazaDanych.removePrzedmiot(id);
                tabelka.loadData();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Wystąpił problem z edycją danych");
                throwables.printStackTrace();
            }
        });
        this.setVisible(true);

    }

    public static void main(String[] args) throws SQLException {
        new PrzedmiotManagement();
    }
}
