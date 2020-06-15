package GUI;

import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Oceny extends JTable {
    String[] kolumny = {"Przedmiot", "Oceny", "Ocena Końcowa"};
    DefaultTableModel tableModel = new DefaultTableModel(kolumny, 3);
    public Oceny(ArrayList<ImmutablePair<String, ResultSet>> oceny) throws SQLException {
        JTable table = new JTable(tableModel);
        this.setAutoCreateRowSorter(true);
        Object[] data;
        for (ImmutablePair<String, ResultSet> i : oceny) {
            data = new Object[]{i.left, i.right.getString("oceny"), i.right.getString("ocenakoncowa")};
            tableModel.addRow(data);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
    }
}
