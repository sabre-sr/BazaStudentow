package GUI;

import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OcenyTabela extends JPanel {
    String[] kolumny = {"Przedmiot", "Oceny", "Ocena Końcowa"};
    DefaultTableModel tableModel = new DefaultTableModel(kolumny,0);

    public OcenyTabela(ArrayList<ImmutablePair<String, ResultSet>> oceny) throws SQLException {
        super();
        this.setLayout(new BorderLayout());
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        Object[] data;
        for (ImmutablePair<String, ResultSet> i : oceny) {
            data = new Object[]{i.left, i.right.getString("oceny"), i.right.getString("ocenakoncowa")};
            tableModel.addRow(data);
        }
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
