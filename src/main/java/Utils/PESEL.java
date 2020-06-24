package Utils;

public abstract class PESEL {
    /**
     * Sprawdza czy wprowadzony nr PESEL jest prawidłowy.
     * Przy pisaniu algorytmu posłużono się informacjami na stronie: https://www.gov.pl/web/gov/czym-jest-numer-pesel
     * Algorytm nie sprawdza sensowności danych liczb na swoich miejscach, a jedynie jego długość i prawidłowość liczby kontrolnej.
     * @param pesel Numer PESEL
     * @return true jeżeli numer jest prawidłowym numerem PESEL; false jeżeli jest nieprawidłowy.
     */
    public static boolean PESELValid(String pesel) {
        int[] waga = {1, 3, 7, 9, 1, 3, 7 ,9 ,1 ,3};
        if (pesel.length() != 11)
            return false;
        int checksum = 0;
        for (int i = 0; i < 10; i++)
            checksum += Integer.parseInt(pesel.substring(i, i+1)) * waga[i];
        int ostatniaCyfra = Integer.parseInt(pesel.substring(10, 11));
        checksum%=10;
        checksum = 10 - checksum;
        return checksum%10 == ostatniaCyfra;

    }
}
