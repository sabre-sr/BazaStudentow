package GUI;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
public class OcenyGrupy extends JPanel {
    final String[] kolumny = {"ID", "Nazwisko", "Oceny", "Ocena Końcowa"};
    private Object[] data;
    DefaultTableModel tableModel;
    private final JTable table;

    /**
     * @param oceny - lista ocen, gdzie lewa zmienna to imie i nazwisko; środkowa - lista ocen; prawa - ocena koncowa.
     * @param id - lista identyfikatorów uczniów w bazie danych
     */
    public OcenyGrupy(ArrayList<ImmutableTriple<String, String, String>> oceny, ArrayList<Integer> id, String przedmiot) {
        super();
        this.table = buildTable(oceny, id);
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            new EditGrades((int)this.table.getValueAt(row, 0), this.table.getValueAt(row, 1).toString(), this.table.getValueAt(row, 2).toString(), this.table.getValueAt(row, 3).toString(), przedmiot);
            buildTable(oceny, id);
        });
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @NotNull
    private JTable buildTable(ArrayList<ImmutableTriple<String, String, String>> oceny, ArrayList<Integer> id) {
        tableModel = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.setLayout(new BorderLayout());
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        Iterator<ImmutableTriple<String, String, String>> immutableTripleIterator = oceny.iterator();
        Iterator<Integer> integerIterator = id.iterator();
        while (immutableTripleIterator.hasNext() && integerIterator.hasNext()) {
            ImmutableTriple<String, String, String> listaocen = immutableTripleIterator.next();
            Integer listaid = integerIterator.next();
            data = new Object[]{listaid, listaocen.left, listaocen.middle, listaocen.right};
            tableModel.addRow(data);
        }
        table.setCellSelectionEnabled(true);
        return table;
    }

}
