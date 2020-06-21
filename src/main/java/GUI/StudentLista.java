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
    ResultSet rs;
    String[] kolumny = {"ID", "Imie i Nazwisko", "PESEL", "Rok studiow", "Nr albumu"};
    DefaultTableModel tableModel;
    public StudentLista() throws SQLException {
        super();
        tableModel = new DefaultTableModel(kolumny,0){
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
        this.loadData(BazaDanych.bazaDanych.getStudents());
    }

    public void loadData(ResultSet rs) throws SQLException {
        tableModel.setRowCount(0);
        TableTools.fillTable(rs, tableModel, table, "student");
    }

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame();
        frame.add(new StudentLista());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
