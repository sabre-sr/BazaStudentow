package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DziekanatUI extends JFrame {

    public DziekanatUI() {
        super("Dziekanat");
        JButton zarzadzanieStudentamiButton;
        this.add(zarzadzanieStudentamiButton = new JButton("Zarządzanie studentami"));
        JButton zarzadzaniePrzedmiotamiButton;
        this.add(zarzadzaniePrzedmiotamiButton = new JButton("Zarzadzanie przedmiotami"));
        JButton zarzadzanieProwadzącymiButton;
        this.add(zarzadzanieProwadzącymiButton = new JButton("Zarzadzanie prowadzącymi"));
        zarzadzanieStudentamiButton.addActionListener(e -> {
            try {
                new StudentManagement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        zarzadzanieProwadzącymiButton.addActionListener(e -> {
            try {
                new ProwadzacyLista();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd dostępu do SQL");
                throwables.printStackTrace();
            }
        });
        zarzadzaniePrzedmiotamiButton.addActionListener(e -> {
            try {
                new PrzedmiotLista();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd dostępu do SQL");
                throwables.printStackTrace();
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.pack();
        this.setSize(500, 150);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new DziekanatUI();
    }
}
