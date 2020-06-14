package Tools;

public abstract class PESEL {
    public static boolean PESELValid(String pesel) {
        int[] waga = {1, 3, 7, 9, 1, 3, 7 ,9 ,1 ,3};
        if (pesel.length() != 11)
            return false;
        int checksum = 0;
        for (int i = 0; i < 10; i++)
        checksum += Integer.parseInt(pesel.substring(i, i+1)) * waga[i];
        int ostatniaCyfra = Integer.parseInt(pesel.substring(10, 11));
        return checksum%10 == ostatniaCyfra;
    }
}
