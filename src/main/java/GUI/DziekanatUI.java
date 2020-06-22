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
        JButton zarzadzanieProwadzacymiButton;
        this.add(zarzadzanieProwadzacymiButton = new JButton("Zarzadzanie prowadzącymi"));
        zarzadzanieStudentamiButton.addActionListener(e -> {
            try {
                new StudentManagement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        zarzadzanieProwadzacymiButton.addActionListener(e -> {
            try {
                new ProwadzacyManagement();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd dostępu do SQL");
                throwables.printStackTrace();
            }
        });
        zarzadzaniePrzedmiotamiButton.addActionListener(e -> {
            try {
                new PrzedmiotManagement();
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
}
