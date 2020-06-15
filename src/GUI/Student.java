package GUI;

import javax.swing.*;

public class Student {
    private Models.Student uzytkownik;
    JLabel imienazwisko, pesel;
    JButton haslo_zmiana;
    JTable oceny;
    public Student(Student student) {
        this.imienazwisko = new JLabel("Zalogowany jako: "+uzytkownik.getImieNazwisko());
        this.pesel = new JLabel("PESEL: "+uzytkownik.getPesel());
    }
}
