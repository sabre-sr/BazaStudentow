package GUI;

import javax.swing.*;
import java.awt.*;

public class DziekanatUI extends JFrame{
    private JButton zarzadzanieStudentamiButton;
    private JButton zarzadzaniePrzedmiotamiButton;
    private JButton zarzadzanieProwadzącymiButton;
    private JButton zarzadzanieDziekanatemButton;

    public DziekanatUI() {
        super("Dziekanat");
        this.add(this.zarzadzanieStudentamiButton = new JButton("Zarządzanie studentami"));
        this.add(this.zarzadzaniePrzedmiotamiButton = new JButton("Zarzadzanie przedmiotami"));
        this.add(this.zarzadzanieProwadzącymiButton = new JButton("Zarzadzanie prowadzącymi"));
        this.zarzadzanieStudentamiButton.addActionListener(e -> {
            new StudentLista();
        });
        this.zarzadzanieProwadzącymiButton.addActionListener(e -> {
            new ProwadzacyLista();
        });
        this.zarzadzaniePrzedmiotamiButton.addActionListener(e -> {
            new PrzedmiotLista();
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
