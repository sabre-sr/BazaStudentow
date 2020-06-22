package GUI;

import Services.BazaDanych;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrzedmiotLista extends JPanel {
    public JTable table;
    String[] kolumny = {"ID", "Przedmiot", "Nazwa tabeli"};
    DefaultTableModel tableModel;

    public PrzedmiotLista() throws SQLException {
        super();
        tableModel = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        loadData();
        this.setLayout(new BorderLayout());
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() throws SQLException {
        tableModel.setRowCount(0);
        ResultSet rs = BazaDanych.bazaDanych.getPrzedmioty();
        Utils.TableTools.fillTable(rs, tableModel, table, "przedmiot");
    }
}


