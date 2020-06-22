package GUI;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Panel z tabelą ocen uczniów z danego przedmiotu.
 */
public class OcenyGrupy extends JPanel {
    public final JTable table;
    final String[] kolumny = {"ID", "Nazwisko", "Oceny", "Ocena Końcowa"};
    DefaultTableModel tableModel;

    /**
     * @param oceny lista ocen, gdzie lewa zmienna to imie i nazwisko; środkowa - lista ocen; prawa - ocena koncowa.
     * @param id    lista identyfikatorów uczniów w bazie danych
     */
    public OcenyGrupy(ArrayList<ImmutableTriple<String, String, String>> oceny, ArrayList<Integer> id) {
        super();
        tableModel = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        loadData(oceny, id);
        this.setLayout(new BorderLayout());
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Funkcja odświeżająca zawartość tabeli.
     * @param oceny lista ocen, gdzie lewa zmienna to imie i nazwisko; środkowa - lista ocen; prawa - ocena koncowa.
     * @param id    lista identyfikatorów uczniów w bazie danych
     */
    public void loadData(ArrayList<ImmutableTriple<String, String, String>> oceny, ArrayList<Integer> id) {
        tableModel.setRowCount(0);
        ListIterator<ImmutableTriple<String, String, String>> iOceny = oceny.listIterator();
        ListIterator<Integer> i_Id = id.listIterator();
        while (iOceny.hasNext() && i_Id.hasNext()) {
            ImmutableTriple<String, String, String> temp = iOceny.next();
            Object[] data = new Object[]{i_Id.next(), temp.left, temp.middle, temp.right};
            tableModel.addRow(data);
        }
        tableModel.setRowCount(tableModel.getRowCount());
        tableModel.fireTableDataChanged();
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(2);
    }

}
