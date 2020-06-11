import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.ImmutablePair

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
        }
        else if (!s.getImieNazwisko().equals(""))
            ps.setString(1, s.getImieNazwisko());
        ResultSet wynik = ps.executeQuery();
        return wynik;
    }

    public ImmutablePair<String, String> getGrades (int student_id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM przedmioty");
        ResultSet przedmioty = ps.executeQuery();
    }

    public static void main(String[] args) {
        new BazaDanych();
    }

}
