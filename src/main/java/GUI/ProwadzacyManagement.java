package GUI;

import Services.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Okno zarządzania prowadzącymi.
 */
public class ProwadzacyManagement extends JFrame {
    /**
     * Przycisk dodawania prowadzących. <p>Wywołuje menu, gdzie można wprowadzić dane, i dodaje je do bazy danych.</p>
     */
    private final JButton dodaj;
    private final JButton usun;
    private final ProwadzacyLista tabelka;

    public ProwadzacyManagement() throws SQLException {
        super("Zarządzanie prowadzącymi");
        this.add(this.tabelka = new ProwadzacyLista());
        this.add(dodaj = new JButton("Dodaj"));
        this.add(usun = new JButton("Usuń"));
        this.setLayout(new FlowLayout());
        this.setSize(470, 520);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dodaj.addActionListener(e -> {
            try {
                new ProwadzacyEdycja();
                tabelka.loadData();

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Wystąpił problem z edycją danych");
                throwables.printStackTrace();
            }
        });
        this.usun.addActionListener(e -> {
            int row = tabelka.table.getSelectedRow();
            int id = Integer.parseInt(String.valueOf(tabelka.table.getValueAt(row, 0)));
            try {
                BazaDanych.bazaDanych.removeProwadzacy(id);
                tabelka.loadData();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Wystąpił problem z edycją danych");
                throwables.printStackTrace();
            }
        });
        this.setVisible(true);

    }
}
