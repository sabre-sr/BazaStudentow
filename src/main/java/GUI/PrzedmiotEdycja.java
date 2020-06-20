package GUI;

import Models.Prowadzacy;
import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;

public class PrzedmiotEdycja extends JDialog {
    JLabel nazwaLabel;
    JTextField nazwa;
    JButton ok, anuluj;
    private int id;
    public PrzedmiotEdycja(){
        new PrzedmiotEdycja(0);
    }
    public PrzedmiotEdycja(int id) {
        this.setModal(true);
        this.id = id;
        this.add(this.nazwaLabel = new JLabel("Nazwa Przedmiotu"));
        this.add(this.nazwa = new JTextField(8));
        this.add(this.ok = new JButton("OK"));
        this.add(this.anuluj = new JButton("Anuluj"));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.pack();
        this.setSize(200, 150);
        this.anuluj.addActionListener(e -> {
            this.dispose();
        });
        this.ok.addActionListener(e -> {
            if (this.id == 0) {
                try {
                    BazaDanych.bazaDanych.addPrzedmiot(nazwa.getText());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            this.dispose();
        });
        this.setVisible(true);
    }
}
