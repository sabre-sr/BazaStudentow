public class Student extends Osoba {
    private String[] pesel;
    private int nralbumu;
    private int rok_studiow;

    public String[] getPesel() {
        return pesel;
    }

    public int getRok_studiow() {
        return rok_studiow;
    }

    public int getNralbumu() {
        return nralbumu;
    }

    public String getImieNazwisko() {
        return super.getImienazwisko();
    }
    public Student(String imienazwisko, String[] pesel, int nralbumu, int rok_studiow)  {
        super(imienazwisko);
        this.pesel = pesel;
        this.nralbumu = nralbumu;
        this.rok_studiow = rok_studiow;
    }

}
