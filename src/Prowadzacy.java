public class Prowadzacy extends Osoba{
    private String przedmiot;
    public Prowadzacy(String imienazwisko, String przedmiot) {
        super(imienazwisko);
        this.przedmiot = przedmiot;
    }

    public String getPrzedmiot() {
        return przedmiot;
    }
}
