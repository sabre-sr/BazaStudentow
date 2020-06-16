package GUI;

import Models.Prowadzacy;
import Services.BazaDanych;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutablePair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class OcenyGrupy extends JPanel {
    String[] kolumny = {"ID", "Nazwisko", "Oceny", "Ocena Końcowa"};
    Object[] data;
    DefaultTableModel tableModel;

    public OcenyGrupy(ArrayList<ImmutableTriple<String, String, String>> oceny, ArrayList<Integer> id) {
        super();
        tableModel = new DefaultTableModel(kolumny, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.setLayout(new BorderLayout());
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
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

        cellSelectionModel.addListSelectionListener(e -> {
            String output = null;
            get
        });
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private ImmutablePair<String, String> editGrades(String imienazwisko, String oceny, String ocenakoncowa) {
        JLabel nazwisko = new JLabel(imienazwisko);
        JButton ok = new JButton("Ok");
        JFrame frame = new JFrame("Edycja");
        ArrayList<JTextField> ocenyField = new ArrayList<>();
        frame.add(nazwisko);
        String[] tokens = oceny.split(" ");
        for (String i : tokens) {
            ocenyField.add(new JTextField(i, 3));
            frame.add(ocenyField.get(ocenyField.size() - 1));
        }
        JLabel srednia = new JLabel("Ocena koncowa: (Obliczona srednia: " + Prowadzacy.srednia(oceny) + ")");
        JTextField srednia_input = new JTextField(ocenakoncowa, 3);
        frame.add(srednia);
        frame.add(srednia_input);
        frame.add(ok);
        ok.addActionListener(e -> {
            StringBuilder temp = new StringBuilder();
            for (JTextField i : ocenyField) {
                try {
                    Float.parseFloat(i.getText());
                    temp.append(i.getText()).append(" ");
                } catch (NumberFormatException ignored) {}
            }
            MutablePair<String, String> pair = new MutablePair<>();
            pair.left = temp.toString();
            try {
                Float.parseFloat(srednia_input.getText());
                pair.right = srednia_input.getText();
            } catch (NumberFormatException err) {
                pair.right = ocenakoncowa;
            }
            BazaDanych.bazaDanych.set;
        });
    }
}
