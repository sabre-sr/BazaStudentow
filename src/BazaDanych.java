import java.sql.*;
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.text.WordUtils;

public class BazaDanych {
    private Connection conn;

    private BazaDanych() {
        Connection conn = null;
        String path = "jdbc:sqlite:baza.db";
        try {
            conn = DriverManager.getConnection(path);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (conn != null)
            this.conn = conn;
    }

    /**
     * Destruktor bazy danych
     */
    protected void finalize() {
        if (this.conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public ResultSet getStudent(Student s) throws SQLException {
        // TODO: jezeli nie wszystkie pola studenta sa podane, dodaj dwiazdki (*)
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM studenci WHERE (?=?)");
        if (s.getNralbumu() != 0) {
            ps.setInt(1, s.getNralbumu());
        } else if (!s.getImieNazwisko().equals(""))
            ps.setString(1, s.getImieNazwisko());
        ResultSet wynik = ps.executeQuery();
        return wynik;
    }

    public ArrayList<ImmutablePair<String, String>> getGrades(int student_id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM przedmioty");
        ResultSet przedmioty = ps.executeQuery();
        ArrayList<ImmutablePair<String, String>> results = new ArrayList<>();
        while (przedmioty.next()) {
            String przedmiot = przedmioty.getString("nazwatabeli");
            System.out.println(przedmiot);
            String query = String.format("SELECT * FROM %s WHERE (id_stud = ?)", przedmiot);
            ps = conn.prepareStatement(query);
            ps.setInt(1, student_id);
            ResultSet oceny = ps.executeQuery();
            oceny.next();
            results.add(new ImmutablePair<>(przedmioty.getString("nazwa"), oceny.getString("oceny")));
            continue;
        }
        return null;
    }

    public void addPrzedmiot(String przedmiot) throws SQLException {
        String tabelanazwa = WordUtils.capitalizeFully(przedmiot, ' ').replaceAll(" ", "");
        String sql = String.format("create table %s\n" +
                "(\n" +
                "    id      INTEGER\n" +
                "        constraint %s\n" +
                "            primary key autoincrement,\n" +
                "    id_stud int not null\n" +
                "        references studenci\n" +
                "            on update cascade on delete cascade,\n" +
                "    oceny   text\n" +
                ");", tabelanazwa, (tabelanazwa + "_pk"));
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        ps = conn.prepareStatement("INSERT INTO przedmioty(nazwa, nazwatabeli) VALUES (?, ?)");
        ps.setString(1, przedmiot);
        ps.setString(2, tabelanazwa);
        ps.execute();
    }

    public static void main(String[] args) throws SQLException {
        new BazaDanych().addPrzedmiot("Nazwa testowa");
    }

}
