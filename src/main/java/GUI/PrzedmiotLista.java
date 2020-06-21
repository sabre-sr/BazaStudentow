package GUI;

import Services.BazaDanych;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrzedmiotLista extends JPanel{
    public JTable table;
    String[] kolumny = {"ID", "Przedmiot", "Nazwa tabeli"};
    DefaultTableModel tableModel;
    public PrzedmiotLista() throws SQLException {
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
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() throws SQLException {
        tableModel.setRowCount(0);
        ResultSet rs = BazaDanych.bazaDanych.getPrzedmioty();
        rs.next();
        Utils.TableTools.fillTable(rs, tableModel, table, "przedmiot");
    }

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame();
        frame.add(new PrzedmiotLista());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


