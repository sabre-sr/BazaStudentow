package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentManagement extends JFrame {
    private JPanel tabelka;
    private final JButton znajdz, dodaj, edytuj, karta;
    public StudentManagement () throws SQLException {
        super("Zarządzanie studentami");
        this.add(this.tabelka = new StudentLista());
        this.add(this.dodaj = new JButton("Dodaj"));
        this.add(this.znajdz = new JButton("Znajdź"));
        this.add(this.edytuj = new JButton("Edytuj"));
        this.add(this.karta = new JButton("Karta studenta"));
        this.setLayout(new FlowLayout());
        this.setSize(470,520);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws SQLException {
        new StudentManagement();
    }
}
