package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PrzedmiotLista extends JPanel {
    public JTable table;
    String[] kolumny = {"ID", "Nazwa przedmiotu", "Nazwa tabeli"};
    DefaultTableModel tableModel;
    public PrzedmiotLista() {
        super();
        tableModel = new DefaultTableModel(kolumny,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.setLayout(new BorderLayout());
        loadData();
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
    }
}

