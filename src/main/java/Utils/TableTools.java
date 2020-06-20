package Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TableTools implements Serializable {
    public static void fillTable(ResultSet rs, DefaultTableModel tableModel, JTable table, String tryb) throws SQLException {
        Object[] data;
        while (rs.next()) {
            if (tryb.contentEquals("student"))
                data = new Object[]{rs.getInt("id"), rs.getString("imienazwisko"), rs.getString("pesel"), rs.getInt("rokstudiow"), rs.getInt("nralbumu")};
            else if (tryb.contentEquals("prowadzacy"))
                data = new Object[]{rs.getInt("id"), rs.getString("imienazwisko"), rs.getString("przedmiot")};
            else
                data = new Object[]{rs.getInt("id"), rs.getString("nazwa"), rs.getString("nazwatabeli")};
            tableModel.addRow(data);
        }
        tableModel.setRowCount(tableModel.getRowCount());
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(2);
        tableModel.fireTableDataChanged();
    }
}