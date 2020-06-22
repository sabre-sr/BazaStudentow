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
import Utils.Passwords;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;

public final class BazaDanych {
    public static BazaDanych bazaDanych = new BazaDanych();
    private static Connection conn;
    private PreparedStatement ps;
    private ResultSet result;


    private BazaDanych() {
        Connection conn = null;
        String path = "jdbc:sqlite:baza.db";
        try {
            conn = DriverManager.getConnection(path);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (conn != null)
            BazaDanych.conn = conn;
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

    private void reopenConn() throws SQLException {
        conn.close();
        String path = "jdbc:sqlite:baza.db";
        try {
            conn = DriverManager.getConnection(path);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet getStudents() throws SQLException {
        ps = conn.prepareStatement("SELECT * FROM studenci");
        return result = ps.executeQuery();
    }

    public ResultSet getProwadzacy() throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM prowadzacy");
        return result = ps.executeQuery();
    }

    public ResultSet getPrzedmioty() throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM przedmioty");
        return result = ps.executeQuery();
    }

    public Osoba logIn(String imienazwisko, @NotNull String haslo, String pozycja) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidPESELException {
        reopenConn();
        String query = String.format("SELECT * FROM %s WHERE (imienazwisko = ?)", pozycja);
        ps = conn.prepareStatement(query);
        ps.setString(1, imienazwisko);
        result = ps.executeQuery();
        if (!result.next())
            return null;
        String hash = result.getString("passwordhash");
        byte[] salt = result.getBytes("salt");
        if (Passwords.validatePassword(haslo, salt, hash)) {
            switch (pozycja) {
                case "studenci":
                    return Student.createStudent(result);
                case "prowadzacy":
                    return Prowadzacy.createProwadzacy(result);
                case "dziekanat":
                    return Dziekanat.createDziekanat(result);
                default:
                    return null;
            }
        }
        return null;
    }


    public void addStudent(Student s, char[] haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        reopenConn();
        ps = conn.prepareStatement("INSERT INTO studenci(imienazwisko, passwordhash, " +
                "salt, pesel, rokstudiow, nralbumu) VALUES (?, ?, ?, ?, ?, ?)");
        ImmutablePair<String, byte[]> hasla = Passwords.generateHashPair(haslo);
        ps.setString(2, hasla.left);
        ps.setBytes(3, hasla.right);
        bindStudentFields(s);
        ps.execute();
    }

    public void addProwadzacy(Prowadzacy p, char[] haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        reopenConn();
        ps = conn.prepareStatement("INSERT INTO prowadzacy(imienazwisko, passwordhash, przedmiot, salt) VALUES (?, ?, ?, ?)");
        ImmutablePair<String, byte[]> hasla = Passwords.generateHashPair(haslo);
        ps.setString(1, p.getImienazwisko());
        ps.setString(2, hasla.left);
        ps.setString(3, p.getPrzedmiot());
        ps.setBytes(4, hasla.right);
        ps.execute();
    }

    public void addDziekanat(Dziekanat dziekanat, char[] haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        reopenConn();
        ps = conn.prepareStatement("INSERT INTO dziekanat(imienazwisko, passwordhash, salt) VALUES (?,?,?)");
        @NotNull ImmutablePair<String, byte[]> hasla = Passwords.generateHashPair(haslo);
        ps.setString(1, dziekanat.getImienazwisko());
        ps.setString(2, hasla.left);
        ps.setBytes(3, hasla.right);
        ps.execute();
    }

    public ResultSet getStudent(Student s) throws SQLException {
        reopenConn();
        // TODO: jezeli nie wszystkie pola studenta sa podane, dodaj dwiazdki (*)
        if (s.getId() != 0) {
            ps = conn.prepareStatement("SELECT * FROM studenci WHERE id=?");
            ps.setInt(1, s.getId());
        } else if (s.getNralbumu() != 0) {
            ps = conn.prepareStatement("SELECT * FROM studenci WHERE nralbumu=?");
            ps.setInt(1, s.getNralbumu());
        } else if (!s.getImieNazwisko().equals("")) {
            ps = conn.prepareStatement("SELECT * FROM studenci WHERE imienazwisko LIKE ?");
            ps.setString(1, ('%'+s.getImieNazwisko()+'%'));
        }
        result = ps.executeQuery();
        return result;
    }

    public void editStudent(Student s, char[] haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        reopenConn();
        System.out.println(haslo);
        if (haslo[0] != '\0') {
            ps = conn.prepareStatement("UPDATE studenci SET imienazwisko = ?, passwordhash = ?, salt = ?, pesel = ?, rokstudiow = ?, nralbumu = ? WHERE id = ?");
            ImmutablePair<String, byte[]> hashPair = Passwords.generateHashPair(haslo);
            ps.setString(2, hashPair.left);
            ps.setBytes(3, hashPair.right);
        } else
            ps = conn.prepareStatement("UPDATE studenci SET imienazwisko = ?, pesel = ?, rokstudiow = ?, nralbumu = ? WHERE id = ?");
        ps.setInt(7, s.getId());
        bindStudentFields(s);
    }

    public void updateYear(int id, int grade) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("UPDATE studenci SET rokstudiow = ? WHERE id= ?");
        ps.setInt(1, grade+1);
        ps.setInt(2, id);
        ps.execute();
    }

    public void editProwadzacy(Prowadzacy p, char[] haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        reopenConn();
        System.out.println(haslo);
        if (haslo[0] != '\0') {
            ps = conn.prepareStatement("UPDATE prowadzacy SET imienazwisko = ?, passwordhash = ?, salt = ?, przedmiot = ? WHERE id = ?");
            ImmutablePair<String, byte[]> hashPair = Passwords.generateHashPair(haslo);
            ps.setString(2, hashPair.left);
            ps.setBytes(3, hashPair.right);
        } else
            ps = conn.prepareStatement("UPDATE prowadzacy SET imienazwisko = ?, przedmiot = ? WHERE id = ?");
        ps.setString(1, p.getImienazwisko());
        ps.setString(4, p.getPrzedmiot());
        ps.setInt(5, p.getId());
    }

    private void bindStudentFields(Student s) throws SQLException {
        ps.setString(1, s.getImieNazwisko());
        ps.setString(4, s.getPesel());
        ps.setInt(5, s.getRok_studiow());
        ps.setInt(6, s.getNralbumu());
    }

    public void updateGrades(int studentId, String grades, String ocenakoncowa, String przedmiotDb) throws SQLException {
        String query = "UPDATE " + przedmiotDb;
        reopenConn();
        ps = conn.prepareStatement(query + " SET oceny = ?, ocenakoncowa = ? WHERE id_stud = ?");
        ps.setString(1, grades);
        ps.setString(2, ocenakoncowa);
        ps.setInt(3, studentId);
        ps.execute();
    }

    public ArrayList<ImmutablePair<String, ResultSet>> getGrades(int student_id) throws SQLException {
        ps = conn.prepareStatement("SELECT * FROM przedmioty");
        result = ps.executeQuery();
        ArrayList<ImmutablePair<String, ResultSet>> results = new ArrayList<>();
        while (result.next()) {
            String przedmiot = result.getString("nazwatabeli");
            System.out.println(przedmiot);
            String query = String.format("SELECT * FROM %s WHERE (id_stud = ?)", przedmiot);
            ps = conn.prepareStatement(query);
            ps.setInt(1, student_id);
            ResultSet oceny = ps.executeQuery();
            results.add(new ImmutablePair<>(result.getString("nazwa"), oceny));
        }
        return results;
    }

    public ResultSet getGrade(int student_id, String przedmiot) throws SQLException {
        reopenConn();
        String query = String.format("SELECT * FROM %S WHERE (id_stud = ?)", przedmiot);
        ps = conn.prepareStatement(query);
        ps.setInt(1, student_id);
        return result = ps.executeQuery();
    }

    public ArrayList<Integer> getStudentIDList(String przedmiot) throws SQLException {
        reopenConn();
        getPrzedmiot(przedmiot);
        ArrayList<Integer> out = new ArrayList<>();
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next())
            out.add(resultSet.getInt("id_stud"));
        return out;
    }

    public void addStudentToClass(Student temp, String przedmiot) throws SQLException {
        String tabela = this.getNazwaTabeli(przedmiot);
        ps = conn.prepareStatement(String.format("INSERT INTO %s (id_stud) VALUES (?)", tabela));
        ps.setInt(1, temp.getId());
        ps.execute();
        ps.close();
    }

    public void removeStudentFromClass(Student temp, String przedmiot) throws SQLException {
        String tabela = this.getNazwaTabeli(przedmiot);
        ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE id_stud = ?", tabela));
        ps.setInt(1, temp.getId());
        ps.execute();
        ps.close();
    }

    private void getPrzedmiot(String przedmiot) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM przedmioty WHERE nazwa = ?");
        ps.setString(1, przedmiot);
        String tabela = ps.executeQuery().getString("nazwatabeli");
        ps.close();
        String query = String.format("SELECT * FROM %s", tabela);
        ps = conn.prepareStatement(query);
    }

    public String getNazwaTabeli(String przedmiot) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM przedmioty WHERE nazwa = ?");
        ps.setString(1, przedmiot);
        ResultSet query= ps.executeQuery();
        query.next();
        String nazwaTabeli = query.getString("nazwatabeli");
        query.close();
        return nazwaTabeli;
    }

    public ArrayList<ImmutableTriple<String, String, String>> getGradeList(String przedmiot) throws SQLException {
        getPrzedmiot(przedmiot);
        ArrayList<ImmutableTriple<String, String, String>> out = new ArrayList<>();
        ResultSet resultSet = ps.executeQuery();
        ResultSet uczniowie;
        while (resultSet.next()) {
            ps = conn.prepareStatement("SELECT * FROM studenci WHERE id=?");
            ps.setInt(1, resultSet.getInt("id_stud"));
            uczniowie = ps.executeQuery();
            uczniowie.next();
            out.add(new ImmutableTriple<>(uczniowie.getString("imienazwisko"), resultSet.getString("oceny"), resultSet.getString("ocenakoncowa")));
            uczniowie.close();
        }
        resultSet.close();
        return out;
    }

    public void addPrzedmiot(String przedmiot) throws SQLException {
        reopenConn();
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
                ");" +
                "create unique index %s_id_stud_uindex\n" +
                "    on %s (id_stud);\n", tabelanazwa, (tabelanazwa + "_pk"), (tabelanazwa), tabelanazwa);
        ps = conn.prepareStatement(sql);
        ps.execute();
        ps = conn.prepareStatement("INSERT INTO przedmioty(nazwa, nazwatabeli) VALUES (?, ?)");
        ps.setString(1, przedmiot);
        ps.setString(2, tabelanazwa);
        ps.execute();
    }

    public void removeStudent(int id) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("DELETE FROM studenci WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
    }

    public void removeProwadzacy(int id) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("DELETE FROM prowadzacy WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
    }

    public void removePrzedmiot(int id) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM przedmioty WHERE id=?");
        ps.setInt(1, id);
        ResultSet query = ps.executeQuery();
        query.next();
        String nazwatabeli = query.getString("nazwatabeli");
        query.close();
        ps = conn.prepareStatement(String.format("DROP TABLE %s", nazwatabeli));
        ps.execute();
        ps = conn.prepareStatement("DELETE FROM przedmioty WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
    }

    public void main(String[] args) {
//        ImmutablePair<String, byte[]> hashPair = Passwords.generateHashPair("[1, 2, 3]");
//        ps = conn.prepareStatement("UPDATE studenci SET passwordhash = ?, salt = ? WHERE imienazwisko = 'Jan Kowalski'");
//        ps.setString(1, hashPair.left);
//        ps.setBytes(2, hashPair.right);
//        ps.execute();
    }



}
