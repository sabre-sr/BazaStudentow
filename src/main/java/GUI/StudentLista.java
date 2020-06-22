package GUI;

import Services.BazaDanych;
import Utils.TableTools;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentLista extends JPanel {
    public JTable table;
    String[] kolumny = {"ID", "Imie i Nazwisko", "PESEL", "Rok studiow", "Nr albumu"};
    DefaultTableModel tableModel;

    public StudentLista() throws SQLException {
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
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() throws SQLException {
        ResultSet rs = BazaDanych.bazaDanych.getStudents();
        this.loadData(rs);
    }

    public void loadData(ResultSet rs) throws SQLException {
        tableModel.setRowCount(0);
        TableTools.fillTable(rs, tableModel, table, "student");
        rs.close();
    }
}
