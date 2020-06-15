package GUI;

import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OcenyTabela extends JPanel {
    String[] kolumny = {"Przedmiot", "Oceny", "Ocena Końcowa"};

    public OcenyTabela(ArrayList<ImmutablePair<String, ResultSet>> oceny, boolean editable) throws SQLException {
        super();
        DefaultTableModel tableModel = new DefaultTableModel(kolumny,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1 || column == 2)
                    return editable;
                else return false;
            }
        };
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
