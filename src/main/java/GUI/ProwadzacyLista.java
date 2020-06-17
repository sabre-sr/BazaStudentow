package GUI;

import Services.BazaDanych;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProwadzacyLista extends JPanel{
    public JTable table;
    String[] kolumny = {"ID", "Imie i nazwisko", "Przedmiot"};
    DefaultTableModel tableModel;
    public ProwadzacyLista() throws SQLException {
        super();
        tableModel = new DefaultTableModel(kolumny,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.setLayout(new BorderLayout());
        loadData();
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() throws SQLException {
        this.table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        Object[] data;
        ResultSet rs = BazaDanych.bazaDanych.getProwadzacy();
        while (rs.next()) {
            data = new Object[]{rs.getInt("id"), rs.getString("imienazwisko"), rs.getString("przedmiot")};
            tableModel.addRow(data);
        }
    }

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame();
        frame.add(new ProwadzacyLista());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
