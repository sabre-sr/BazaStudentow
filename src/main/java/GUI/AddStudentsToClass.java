package GUI;

import javax.swing.*;
import java.sql.SQLException;

public class AddStudentsToClass extends JDialog {
    SearchStudents wyniki;
    JButton dodaj;
    public AddStudentsToClass() throws SQLException {
        this.setModal(true);
        this.add(this.wyniki = new SearchStudents());
        this.setSize(400,400);
        this.add(this.dodaj = new JButton("Dodaj"));
        this.dodaj.addActionListener(e -> {
            int row = this.wyniki.tabelka.table.getSelectedRow();
            int id = this.wyniki.tabelka.table.
        });
    }
}
