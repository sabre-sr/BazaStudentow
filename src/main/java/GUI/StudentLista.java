package GUI;

import Services.BazaDanych;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentLista extends JPanel {
//    private final JButton znajdz;
    private final JTable table;
//    private final JButton dodaj, edytuj, karta;
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
        this.table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        loadData();
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

//        this.add(this.dodaj = new JButton("Dodaj"));
//        this.add(this.edytuj = new JButton("Edytuj"));
//        this.add(this.znajdz = new JButton("Znajdz"));
//        this.add(this.karta = new JButton("Karta studenta"));
//        this.dodaj.addActionListener(e -> {
//            new StudentEdycja();
//            try {
//
//            } catch (SQLException throwables) {
//                JOptionPane.showMessageDialog(null, "Wystąpił problem z ładowaniem zawartości bazy.");
//                throwables.printStackTrace();
//            }
//        });
    }

    private void loadData() throws SQLException {
        Object[] data;
        ResultSet rs = BazaDanych.bazaDanych.getStudents();
        while (rs.next()) {
            data = new Object[]{rs.getInt("id"), rs.getString("imienazwisko"), rs.getString("pesel"), rs.getInt("rokstudiow"), rs.getInt("nralbumu")};
            tableModel.addRow(data);
        }
    }

//    private void loadData() throws SQLException {
//
//    }

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame();
        frame.add(new StudentLista());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
