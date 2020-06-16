package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentLista extends JPanel {
    JTable table;
    JButton dodaj;
    String[] kolumny = {"Imie i Nazwisko", "PESEL", "Rok studiow", "Nr albumu"};
    DefaultTableModel tableModel;
}
