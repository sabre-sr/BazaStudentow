package GUI;

import Models.Prowadzacy;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class OcenyEdit extends JPanel {
    String[] kolumny = {"ID", "Nazwisko", "Oceny", "Ocena Końcowa"};
    DefaultTableModel tableModel;

    public OcenyEdit(ArrayList<ImmutableTriple<String, String, String>> oceny, ArrayList<Integer> id) {
        super();
        tableModel = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2;
            }
        };
        this.setLayout(new BorderLayout());
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        Object[] data;
        Iterator<ImmutableTriple<String, String, String>> immutableTripleIterator = oceny.iterator();
        Iterator<Integer> integerIterator = id.iterator();
        while (immutableTripleIterator.hasNext() && integerIterator.hasNext()) {
            ImmutableTriple<String, String, String> listaocen = immutableTripleIterator.next();
            Integer listaid = integerIterator.next();
            data = new Object[]{listaid, listaocen.left, listaocen.middle, listaocen.right};
            tableModel.addRow(data);
        }
        table.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String output = null;
                get
            }
        })
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private String editGrades(String imienazwisko, String oceny, String ocenakoncowa) {
        JLabel nazwisko = new JLabel(imienazwisko);
        JButton ok = new JButton("Ok");
        JFrame frame = new JFrame("Edycja");
        ArrayList<JTextField> ocenyField = new ArrayList<>();
        frame.add(nazwisko);
        String[] tokens = oceny.split(" ");
        for (String i : tokens) {
            ocenyField.add(new JTextField(i, 3));
            frame.add(ocenyField.get(ocenyField.size()-1));
        }
        JLabel srednia = new JLabel("Obliczona srednia: " + Prowadzacy.srednia(oceny));
        JTextField srednia_input = new JTextField();
    }
}
