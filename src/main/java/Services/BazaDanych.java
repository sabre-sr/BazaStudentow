package Services;

import Exceptions.InvalidPESELException;
import Models.Dziekanat;
import Models.Osoba;
import Models.Prowadzacy;
import Models.Student;
import Utils.Passwords;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Klasa odpowiadająca za komunikację wszystkich innych klas z bazą danych SQL (dystrybucja SQLite).
 * <p>Klasa zajmuje się wykonywaniem operacji na bazie danych, i zwracaniem jej rezultatów w formie gotowej do wykorzystania
 * przez resztę klas.</p>
 */
public final class BazaDanych {
    /**
     * Element singleton, pozwalający na wykonywanie operacji bez potrzeby tworzenia nowego obiektu klasy.
     */
    public static BazaDanych bazaDanych = new BazaDanych();
    /**
     * Połączenie z bazą danych. Konwertuje obiekty klasy Statement w zapytania języka SQL.
     */
    private static Connection conn;
    /**
     * Obiekt pozwalający na składanie przygotowanych zapytań SQL.
     * <p>Zapytanie zostaje najpierw wysłane do bazy, a dopiero po tym zostaje wypełnione zmiennymi. Pozwala to na uniknięcia ataków typu SQL injection.</p>
     *
     * @see PreparedStatement
     */
    private PreparedStatement ps;
    /**
     * Wspólna zmienna z wynikami zapytania.
     * <p>Ze względu na to, że obiekty klasy ResultSet nie są automatycznie niszczone po użyciu, unika się tworzenia ich większej ilości.</p>
     *
     * @see ResultSet
     */
    private ResultSet result;


    /**
     * Konstruktor obiektu BazaDanych.
     * <p>Następuje próba połączenia z bazą danych. Jeżeli jest ona pomyślna, połącznenie zostaje przypisane do pola BazaDanych.conn.
     * W przeciwnym razie zostaje wyrzucony wyjątek SQLException.</p>
     *
     * @see SQLException
     */
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
     * Destruktor bazy danych. Wszystkie niezapisane zmiany są wysyłane do bazy danych, a następnie połączenie zostaje zamknięte.
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

    /**
     * Metoda odnawiająca połączenie z bazą danych.
     * <p>Ze względu na specyfikę SQLite, jest to potrzebne do np użycia StudentGUI przez dziekanat.</p>
     * <p>Odnowienie połączenia zamyka wszystkie zapytania i dostęp do poprzednich rezultatów.</p>
     *
     * @throws SQLException Generyczny błąd SQL
     */
    private void reopenConn() throws SQLException {
        conn.close();
        String path = "jdbc:sqlite:baza.db";
        try {
            conn = DriverManager.getConnection(path);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Metoda pobierająca zawartość tabeli przechowującej dane studentów.
     *
     * @return ResultSet zawierający wszystkie rekordy z tabeli studentów.
     * @throws SQLException Generyczny błąd SQL.
     */
    public ResultSet getStudents() throws SQLException {
        ps = conn.prepareStatement("SELECT * FROM studenci");
        return result = ps.executeQuery();
    }

    /**
     * Medota pobierająca zawartość tabeli przechowującej dane prowadzących.
     *
     * @return ResultSet zawierający wszystkie rekordy z tabeli prowadzących.
     * @throws SQLException Generyczny błąd SQL.
     */
    public ResultSet getProwadzacy() throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM prowadzacy");
        return result = ps.executeQuery();
    }

    /**
     * Metoda zwracająca wszystkie przedmioty z bazy danych.
     *
     * @return ResultSet zawierający wszystkie rekordy z tabeli przedmiotów
     * @throws SQLException Generyczny błąd SQL
     */
    public ResultSet getPrzedmioty() throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM przedmioty");
        return result = ps.executeQuery();
    }

    /**
     * Metoda walidująca dane logowania użytkownika
     *
     * @param imienazwisko Imie i nazwisko, służące jako login.
     * @param haslo        Hasło logującego się.
     * @param pozycja      Typ użytkownika logującego się (Student, prowadzący lub dziekanat).
     * @return Obiekt klasy dziedziczącej po klasie Osoba jeżeli dane logowania są prawidłowe;
     * <p>null jeżeli są nieprawidłowe/użytkownik nie istnieje.</p>
     * @throws SQLException             Generyczny błąd SQL
     * @throws InvalidKeySpecException  Specyfikacja klucza szyfrującego jest nieprawidłowa. Błąd ten nie powinien wystąpić.
     * @throws NoSuchAlgorithmException Algorytm szyfrujący jest niedostępny.
     * @throws InvalidPESELException    Nr PESEL jest nieprawidłowy
     */
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


    /**
     * Dodaje studenta do bazy danych.
     *
     * @param s     Dodawany student.
     * @param haslo Hasło dodawanego użytkownika.
     * @throws SQLException             Generyczny błąd SQL
     * @throws InvalidKeySpecException  Specyfikacja klucza szyfrującego jest nieprawidłowa. Błąd ten nie powinien wystąpić.
     * @throws NoSuchAlgorithmException Algorytm szyfrujący jest niedostępny.
     */
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

    /**
     * Dodaje prowadzącego do bazy danych.
     *
     * @param p     Dodawany prowadzący.
     * @param haslo Hasło dodawanego użytkownika.
     * @throws SQLException             Generyczny błąd SQL
     * @throws InvalidKeySpecException  Specyfikacja klucza szyfrującego jest nieprawidłowa. Błąd ten nie powinien wystąpić.
     * @throws NoSuchAlgorithmException Algorytm szyfrujący jest niedostępny.
     */
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

    /**
     * Dodaje nowego użytkownika z dziekanatu do bazy danych.
     *
     * @param dziekanat Dodawany użytkownik dziekanatu
     * @param haslo     Hasło dodawanego użytkownika.
     * @throws SQLException             Generyczny błąd SQL
     * @throws InvalidKeySpecException  Specyfikacja klucza szyfrującego jest nieprawidłowa. Błąd ten nie powinien wystąpić.
     * @throws NoSuchAlgorithmException Algorytm szyfrujący jest niedostępny.
     */
    public void addDziekanat(Dziekanat dziekanat, char[] haslo) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        reopenConn();
        ps = conn.prepareStatement("INSERT INTO dziekanat(imienazwisko, passwordhash, salt) VALUES (?,?,?)");
        @NotNull ImmutablePair<String, byte[]> hasla = Passwords.generateHashPair(haslo);
        ps.setString(1, dziekanat.getImienazwisko());
        ps.setString(2, hasla.left);
        ps.setBytes(3, hasla.right);
        ps.execute();
    }

    /**
     * Pobiera dane studenta z bazy danych.
     *
     * @param s Szukany student
     * @return ResultSet zawierający wpis z danymi szukanej osoby
     * @throws SQLException Generyczny bląd SQL
     */
    public ResultSet getStudent(Student s) throws SQLException {
        reopenConn();
        // TODO: jezeli nie wszystkie pola studenta sa podane, dodaj dwiazdki (*)
        if (s.getId() != 0) {
            ps = conn.prepareStatement("SELECT * FROM studenci WHERE id=?");
            ps.setInt(1, s.getId());
        } else if (s.getNralbumu() != 0) {
            ps = conn.prepareStatement("SELECT * FROM studenci WHERE nralbumu=?");
            ps.setInt(1, s.getNralbumu());
        } else if (!s.getImienazwisko().equals("")) {
            ps = conn.prepareStatement("SELECT * FROM studenci WHERE imienazwisko LIKE ?");
            ps.setString(1, ('%' + s.getImienazwisko() + '%'));
        }
        result = ps.executeQuery();
        return result;
    }

    /**
     * Modyfikuje dane studenta w bazie danych.
     *
     * @param s     Edytowany użytkownik.
     * @param haslo Nowe hasło (jeżeli podane)
     * @throws SQLException             Generyczny błąd SQL
     * @throws InvalidKeySpecException  Specyfikacja klucza szyfrującego jest nieprawidłowa. Błąd ten nie powinien wystąpić.
     * @throws NoSuchAlgorithmException Algorytm szyfrujący jest niedostępny.
     */
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

    /**
     * Metoda wpisująca studenta na kolejny rok;
     *
     * @param id    Numer ID studenta w bazie.
     * @param grade Obecny rok studiów.
     * @throws SQLException Generyczny błąd SQL
     */
    public void updateYear(int id, int grade) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("UPDATE studenci SET rokstudiow = ? WHERE id= ?");
        ps.setInt(1, grade + 1);
        ps.setInt(2, id);
        ps.execute();
    }

    /**
     * Metoda edytująca prowadzącego w bazie danych.
     *
     * @param p     Edytowany użytkownik.
     * @param haslo Nowe hasło (jeżeli podane)
     * @throws SQLException             Generyczny błąd SQL
     * @throws InvalidKeySpecException  Specyfikacja klucza szyfrującego jest nieprawidłowa. Błąd ten nie powinien wystąpić.
     * @throws NoSuchAlgorithmException Algorytm szyfrujący jest niedostępny.
     */
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

    /**
     * Metoda wewnętrzna przypisująca pola danych studenta do zapytania do bazy danych.
     *
     * @param s Student
     * @throws SQLException Generyczny błąd SQL
     */
    private void bindStudentFields(Student s) throws SQLException {
        ps.setString(1, s.getImienazwisko());
        ps.setString(4, s.getPesel());
        ps.setInt(5, s.getRok_studiow());
        ps.setInt(6, s.getNralbumu());
    }

    /**
     * Aktualizuje oceny studenta.
     *
     * @param studentId    Nr ID studenta.
     * @param grades       Nowa lista ocen.
     * @param ocenakoncowa Nowa ocena końcowa.
     * @param przedmiotDb  Nazwa tabeli przedmiotu, gdzie oceny są edytowane.
     * @throws SQLException Generyczny błąd SQL
     */
    public void updateGrades(int studentId, String grades, String ocenakoncowa, String przedmiotDb) throws SQLException {
        String query = "UPDATE " + przedmiotDb;
        reopenConn();
        ps = conn.prepareStatement(query + " SET oceny = ?, ocenakoncowa = ? WHERE id_stud = ?");
        ps.setString(1, grades);
        ps.setString(2, ocenakoncowa);
        ps.setInt(3, studentId);
        ps.execute();
    }

    /**
     * Pobiera listę ocen ucznia ze wszystkich przedmiotów.
     *
     * @param student_id Nr ID ucznia w bazie danych.
     * @return Lista ocen ze wszystkich przedmiotów ucznia.
     * @throws SQLException Generyczny błąd SQL
     */
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

    /**
     * Pobiera oceny studenta z danego przedmiotu z bazy danych.
     *
     * @param student_id Nr ID studenta w bazie danych.
     * @param przedmiot  Nazwa przedmiotu z którego oceny będą pobierane.
     * @return ResultSet zawieracjący listę ocen studenta.
     * @throws SQLException generyczny błąd SQL.
     */
    public ResultSet getGrade(int student_id, String przedmiot) throws SQLException {
        reopenConn();
        String query = String.format("SELECT * FROM %S WHERE (id_stud = ?)", przedmiot);
        ps = conn.prepareStatement(query);
        ps.setInt(1, student_id);
        return result = ps.executeQuery();
    }

    /**
     * Pobiera listę studentów z danego przedmiotu i ich numery ID w tabeli przedmiotu.
     *
     * @param przedmiot Nazwa przedmiotu.
     * @return Lista ID studentów w kolejności występowania w bazie danych.
     * @throws SQLException Generyczny błąd SQL
     */
    public ArrayList<Integer> getStudentIDList(String przedmiot) throws SQLException {
        reopenConn();
        getPrzedmiot(przedmiot);
        ArrayList<Integer> out = new ArrayList<>();
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next())
            out.add(resultSet.getInt("id_stud"));
        resultSet.close();
        return out;
    }

    /**
     * Dodaje studenta do listy obecności danego przedmiotu.
     *
     * @param temp      Dodawany student.
     * @param przedmiot Nazwa przedmiotu.
     * @throws SQLException Generyczny błąd SQL.
     */
    public void addStudentToClass(Student temp, String przedmiot) throws SQLException {
        String tabela = this.getNazwaTabeli(przedmiot);
        ps = conn.prepareStatement(String.format("INSERT INTO %s (id_stud) VALUES (?)", tabela));
        ps.setInt(1, temp.getId());
        ps.execute();
        ps.close();
    }

    /**
     * Usuwa studenta z listy obecności danego przedmiotu.
     *
     * @param temp      Dodawany student.
     * @param przedmiot Nazwa przedmiotu.
     * @throws SQLException Generyczny błąd SQL.
     */
    public void removeStudentFromClass(Student temp, String przedmiot) throws SQLException {
        String tabela = this.getNazwaTabeli(przedmiot);
        ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE id_stud = ?", tabela));
        ps.setInt(1, temp.getId());
        ps.execute();
        ps.close();
    }

    /**
     * Pobiera z bazy danych listę obecności z danego przedmiotu.
     *
     * @param przedmiot Nazwa przedmiotu.
     * @throws SQLException Generyczny błąd SQL.
     */
    private void getPrzedmiot(String przedmiot) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM przedmioty WHERE nazwa = ?");
        ps.setString(1, przedmiot);
        String tabela = ps.executeQuery().getString("nazwatabeli");
        ps.close();
        String query = String.format("SELECT * FROM %s", tabela);
        ps = conn.prepareStatement(query);
    }

    /**
     * Zwraca nazwę tabeli danego przedmiotu.
     *
     * @param przedmiot Nazwa przedmiotu
     * @return Nazwa tabeli przedmiotu.
     * @throws SQLException Generyczny błąd SQL.
     */
    public String getNazwaTabeli(String przedmiot) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("SELECT * FROM przedmioty WHERE nazwa = ?");
        ps.setString(1, przedmiot);
        ResultSet query = ps.executeQuery();
        query.next();
        String nazwaTabeli = query.getString("nazwatabeli");
        query.close();
        return nazwaTabeli;
    }

    /**
     * Zwraca listę ocen uczniów i ich oceny z danego przedmiotu.
     *
     * @param przedmiot Nazwa przedmiotu.
     * @return Lista trójek ocen uczniów w schemacie (imie i nazwisko:oceny:ocena koncowa).
     * @throws SQLException Generyczny błąd SQL,
     */
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

    /**
     * Dodaje nowy przedmiot do bazy danych.
     * <p>Nazwa tabeli jest automatycznie generowana z nazwy przedmiotu, aby uniknąć nazw szkodliwych/znaków niepożądanych w zapytaniach SQL</p>
     *
     * @param przedmiot Nazwa przedmiotu.
     * @throws SQLException Generyczny błąd SQL.
     */
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

    /**
     * Usuwa studenta z bazy danych.
     *
     * @param id Nr ID użytkownika w bazie danych.
     * @throws SQLException Generyczny błąd SQL.
     */
    public void removeStudent(int id) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("DELETE FROM studenci WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
    }

    /**
     * Usuwa prowadzącego z bazy danych.
     *
     * @param id Nr ID użytkownika w bazie danych.
     * @throws SQLException Generyczny błąd SQL.
     */
    public void removeProwadzacy(int id) throws SQLException {
        reopenConn();
        ps = conn.prepareStatement("DELETE FROM prowadzacy WHERE id=?");
        ps.setInt(1, id);
        ps.execute();
    }

    /**
     * Usuwa przedmiot z bazy danych.
     * @param id Nr ID przedmiotu w bazie danych.
     * @throws SQLException Generyczny błąd SQL.
     */
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
}
