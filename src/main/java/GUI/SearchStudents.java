package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchStudents extends JPanel {
    public ResultSet resultSet;
    public StudentLista tabelka;
    public SearchStudents() throws SQLException {
        new FindStudents(this);
        tabelka = new StudentLista();
        tabelka.loadData(resultSet);
        this.setSize(400, 300);
        this.add(this.tabelka);
        this.setLayout(new BorderLayout());
        tabelka.table.setFillsViewportHeight(true);
        tabelka.table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelka.table);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
