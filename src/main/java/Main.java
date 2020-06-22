import GUI.Logowanie;

import javax.swing.*;
import java.io.File;

public class Main {
    static String filename = "baza.db";
    public static void main(String[] args) {
        if (!new File(filename).isFile()) {
            JOptionPane.showMessageDialog(null, "Nie znaleziono bazy danych. Tworzenie nowej...");
            if (!Utils.CreateDb.newDB()) {
                JOptionPane.showMessageDialog(null, "Nie udało się stworzyć nowej bazy danych. Zamykanie...");
                System.exit(1);
            }
        }
        new Logowanie();
    }
}
