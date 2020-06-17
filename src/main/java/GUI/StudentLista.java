package GUI;

import Services.BazaDanych;
import org.apache.commons.lang3.tuple.ImmutablePair;

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

    public void loadData() throws SQLException {
        tableModel.setRowCount(0);
        this.table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        Object[] data;
        ResultSet rs = BazaDanych.bazaDanych.getStudents();
        while (rs.next()) {
            data = new Object[]{rs.getInt("id"), rs.getString("imienazwisko"), rs.getString("pesel"), rs.getInt("rokstudiow"), rs.getInt("nralbumu")};
            tableModel.addRow(data);
        }
//        rs.close();
    }

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame();
        frame.add(new StudentLista());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
