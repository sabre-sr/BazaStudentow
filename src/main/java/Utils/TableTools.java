package Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Wypełnianie tabeli danymi w różnych częściach programu.
 */
public abstract class TableTools implements Serializable {
    /**
     * Wypełnia tablice danymi zgodnie z ich przeznaczeniem.
     * @param rs ResultSet zawierający listę danych.
     * @param tableModel TableModel tabeli do wypełnienia.
     * @param table Tabela do wypełnienia.
     * @param tryb Jaki rodzaj danych jest dodawany do tabeli.
     * @throws SQLException Generyczny błąd SQL.
     */
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