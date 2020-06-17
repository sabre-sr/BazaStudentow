package GUI;

import Services.BazaDanych;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrzedmiotLista extends JPanel {
    public JTable table;
    String[] kolumny = {"ID", "Nazwa przedmiotu", "Nazwa tabeli"};
    DefaultTableModel tableModel;
    public PrzedmiotLista() throws SQLException {
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
        ResultSet rs = BazaDanych.bazaDanych.getPrzedmioty();
        while (rs.next()) {
            data = new Object[]{rs.getInt("id"), rs.getString("nazwa"), rs.getString("nazwatabeli")};
            tableModel.addRow(data);
        }
    }

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame();
        frame.add(new PrzedmiotLista());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

