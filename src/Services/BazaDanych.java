package Services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;

import Exceptions.InvalidPESELException;
import Models.Dziekanat;
import Models.Osoba;
import Models.Prowadzacy;
import Models.Student;
import Tools.Passwords;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

public final class BazaDanych {
    public static BazaDanych bazaDanych = new BazaDanych();
    private Connection conn;
    private PreparedStatement ps;

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
        if (conn != null) {
            try {
                conn.commit();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public ResultSet getStudents() throws SQLException {
        ps = conn.prepareStatement("SELECT * FROM studenci", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        return ps.executeQuery();
    }

    public Osoba logIn(String imienazwisko, @NotNull String haslo, String pozycja) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidPESELException {
        String query = String.format("SELECT * FROM %s WHERE (imienazwisko = ?)", pozycja);
        ps = conn.prepareStatement(query);
        ps.setString(1, imienazwisko);
        ResultSet result = ps.executeQuery();
        if (!result.next())
            return null;
        String hash = result.getString("passwordhash");
        byte[] salt = result.getBytes("salt");
        if (Passwords.validatePassword(haslo, salt, hash)) {
            if (pozycja.equals("student"))
                return Student.createStudent(result);
            else if (pozycja.equals("prowadzacy"))
                return Prowadzacy.createProwadzacy(result);
            else if (pozycja.equals("dziekanat"))
                return Dziekanat.createDziekanat(result);
            else return null;
        }
        return null;
    }



    public boolean addStudent(Student s, String haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        ps = conn.prepareStatement("INSERT INTO studenci(imienazwisko, passwordhash, " +
                "salt, pesel, rokstudiow, nralbumu) VALUES (?, ?, ?, ?, ?, ?)");
        ImmutablePair<String, byte[]> hasla = Passwords.generateHashPair(haslo);
        ps.setString(1, s.getImieNazwisko());
        ps.setString(2, hasla.left);
        ps.setBytes(3, hasla.right);
        ps.setString(4, s.getPesel());
        ps.setInt(5, s.getRok_studiow());
        ps.setInt(6, s.getNralbumu());
        boolean result = ps.execute();
        conn.commit();
        return result;
    }

    public boolean addProwadzacy(Prowadzacy p, String haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        ps = conn.prepareStatement("INSERT INTO prowadzacy(imienazwisko, passwordhash, przedmiot, salt) VALUES (?, ?, ?, ?)", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ImmutablePair<String, byte[]> hasla = Passwords.generateHashPair(haslo);
        ps.setString(1, p.getImienazwisko());
        ps.setString(2, hasla.left);
        ps.setString(3, p.getPrzedmiot());
        ps.setBytes(4, hasla.right);
        boolean result = ps.execute();
        conn.commit();
        return result;
    }

    public boolean addDziekanat(Dziekanat dziekanat, String haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        ps = conn.prepareStatement("INSERT INTO dziekanat(imienazwisko, passwordhash, salt) VALUES (?,?,?)");
        @NotNull ImmutablePair<String, byte[]> hasla = Passwords.generateHashPair(haslo);
        ps.setString(1, dziekanat.getImienazwisko());
        ps.setString(2, hasla.left);
        ps.setBytes(3, hasla.right);
        return ps.execute();
    }

    public ResultSet getStudent(Student s) throws SQLException {
        // TODO: jezeli nie wszystkie pola studenta sa podane, dodaj dwiazdki (*)
        ps = conn.prepareStatement("SELECT * FROM studenci WHERE (?=?)", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        if (s.getNralbumu() != 0) {
            ps.setInt(1, s.getNralbumu());
        } else if (!s.getImieNazwisko().equals(""))
            ps.setString(1, s.getImieNazwisko());
        return ps.executeQuery();
    }

    public ArrayList<ImmutablePair<String, ResultSet>> getGrades(int student_id) throws SQLException {
        ps = conn.prepareStatement("SELECT * FROM przedmioty", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet przedmioty = ps.executeQuery();
        ArrayList<ImmutablePair<String, ResultSet>> results = new ArrayList<>();
        while (przedmioty.next()) {
            String przedmiot = przedmioty.getString("nazwatabeli");
            System.out.println(przedmiot);
            String query = String.format("SELECT * FROM %s WHERE (id_stud = ?)", przedmiot);
            ps = conn.prepareStatement(query);
            ps.setInt(1, student_id);
            ResultSet oceny = ps.executeQuery();
            results.add(new ImmutablePair<>(przedmiot, oceny));
        }
        return results;
    }

    public ResultSet getGrade(int student_id, String przedmiot) throws SQLException {
        String query = String.format("SELECT * FROM %S WHERE (id_stud = ?)", przedmiot);
        ps = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ps.setInt(1, student_id);
        return ps.executeQuery();
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
                "    oceny   text default null,\n" +
                "    ocenakoncowa text default null\n" +
                ");", tabelanazwa, (tabelanazwa + "_pk"));
        ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ps.execute();
        ps = conn.prepareStatement("INSERT INTO przedmioty(nazwa, nazwatabeli) VALUES (?, ?)");
        ps.setString(1, przedmiot);
        ps.setString(2, tabelanazwa);
        ps.execute();
        conn.commit();
    }

    public void removeStudent(@NotNull ResultSet student) throws SQLException {
        student.deleteRow();
        conn.commit();
    }

    public void main(String[] args) {

    }

}
