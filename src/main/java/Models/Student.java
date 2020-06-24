package Models;

import Exceptions.InvalidPESELException;
import GUI.StudentGUI;
import Services.BazaDanych;
import Utils.PESEL;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Klasa reprezentująca studenta.
 * <p>Student może sprawdzić swoje oceny z przedmiotów oraz swoje oceny końcowe.</p>
 */
public class Student extends Osoba {
    private final String pesel;
    private int nralbumu;
    private final int rok_studiow;

    public String getPesel() {
        return pesel;
    }

    public int getRok_studiow() {
        return rok_studiow;
    }

    public int getNralbumu() {
        return nralbumu;
    }

    public void setNralbumu(int nralbumu) {
        this.nralbumu = nralbumu;
    }

    public String getImieNazwisko() {
        return super.getImienazwisko();
    }


    /**
     * Metoda konstruująca studenta z wyników wyszukiwania.
     * @param resultSet Rezultat z bazy danych, zawierający dane logującego się użytkownika.
     * @return Klasa Student, reprezentująca zalogowanego użytkownika
     * @throws SQLException Generyczny błąd SQL
     * @throws InvalidPESELException Wyjątek błędnego numeru PESEL w bazie
     */
    public static Student createStudent(ResultSet resultSet) throws SQLException, InvalidPESELException {
        Student student = new Student(resultSet.getString("imienazwisko"), resultSet.getString("pesel"), resultSet.getInt("nralbumu"), resultSet.getInt("rokstudiow"));
        student.setId(resultSet.getInt("id"));
        return student;
    }

    public Student(String imienazwisko, String pesel, int nralbumu, int rok_studiow) throws InvalidPESELException {
        super(imienazwisko);
        if (PESEL.PESELValid(pesel))
            this.pesel = pesel;
        else throw new InvalidPESELException("Nieprawidlowy Tools.PESEL");
        this.nralbumu = nralbumu;
        this.rok_studiow = rok_studiow;
    }

    public Student(String imienazwisko, String pesel, int nralbumu, int rok_studiow, int id) throws InvalidPESELException {
        this(imienazwisko, pesel, nralbumu, rok_studiow);
        setId(id);
    }

    public Student() {
        super("");
        this.pesel = "";
        this.nralbumu = 0;
        this.rok_studiow = 0;
    }

    public Student(String imienazwisko) {
        super(imienazwisko);
        this.pesel = "";
        this.nralbumu = 0;
        this.rok_studiow = 0;
    }
    /**
     * Otwiera menu główne studenta.
     * @see Interfaces.WindowManagement
     */
    @Override
    public void openWindow() throws SQLException {
        new StudentGUI(this);
    }

}
