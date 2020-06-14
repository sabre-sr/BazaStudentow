package Models;

public abstract class Osoba {
    private String imienazwisko;
    public Osoba(String imienazwisko) {
        this.imienazwisko = imienazwisko;
    }

    public String getImienazwisko() {
        return imienazwisko;
    }
}
