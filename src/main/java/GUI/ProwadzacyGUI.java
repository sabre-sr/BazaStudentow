package GUI;

import javax.swing.*;

public class ProwadzacyGUI extends JFrame {
    private Models.Prowadzacy uzytkownik;
    JLabel imienazwisko, przedmiot;
    JButton dodajUcznia;

    public ProwadzacyGUI(Models.Prowadzacy uzytkownik) {
        this.add(imienazwisko = new JLabel("Zalogowany użytkownik: " + uzytkownik.getImienazwisko()));
        this.add(this.przedmiot = new JLabel("Przedmiot: " + uzytkownik.getPrzedmiot()));
    }
}
