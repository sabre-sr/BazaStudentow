package Models;

import Interfaces.WindowManagement;

public abstract class Osoba implements WindowManagement {
    private int id = 0;
    private final String imienazwisko;

    public Osoba(String imienazwisko) {
        this.imienazwisko = imienazwisko;
    }


    public String getImienazwisko() {
        return imienazwisko;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
