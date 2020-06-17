package GUI;

import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ProwadzacyManagement extends JFrame {
    private ProwadzacyLista tabelka;
    private final JButton dodaj, usun;

    public ProwadzacyManagement() throws SQLException {
        this.add(this.tabelka = new ProwadzacyLista());
        this.add(dodaj = new JButton("Dodaj"));
        this.add(usun = new JButton("Usuń"));
        this.setLayout(new FlowLayout());
        this.setSize(470, 520);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dodaj.addActionListener(e -> {
            try {
                new ProwadzacyEdycja();
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
                BazaDanych.bazaDanych.removeProwadzacy(id);
                tabelka.loadData();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Wystąpił problem z edycją danych");
                throwables.printStackTrace();
            }
        });
        this.setVisible(true);

    }
}
