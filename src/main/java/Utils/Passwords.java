package Utils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * Klasa odpowiadająca za tworzenie i walidację hashów haseł.
 */
public abstract class Passwords {
    private static final int petle = 1000;

    /**
     * Generuje hash hasła.
     * Użyty został algorytm PBKDF2, gdzie każdy hash ma 512 znaków, zasolony 16 bajtową solą.
     *
     * @param haslo Hasło użytkownika
     * @return Para hash:sól
     * @throws InvalidKeySpecException  Specyfikacja klucza szyfrującego jest nieprawidłowa. Błąd ten nie powinien wystąpić.
     * @throws NoSuchAlgorithmException Algorytm szyfrujący jest niedostępny.
     */
    public static @NotNull
    ImmutablePair<String, byte[]> generateHashPair(char[] haslo) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(haslo, salt, petle, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return new ImmutablePair<>(Arrays.toString(hash), salt);
    }

    /**
     * Sprawdza poprawność wprowadzonego hasła przy logowaniu.
     * @param haslo Wprowadzone hasło.
     * @param salt Sól zapisana w bazie danych.
     * @param storedHash Hash hasła zapisany w bazie danych.
     * @return true jeżeli hasło jest prawidłowe; false jeżeli nie pokrywa się.
     * @throws InvalidKeySpecException  Specyfikacja klucza szyfrującego jest nieprawidłowa. Błąd ten nie powinien wystąpić.
     * @throws NoSuchAlgorithmException Algorytm szyfrujący jest niedostępny.
     */
    public static boolean validatePassword(@NotNull String haslo, byte[] salt, @NotNull String storedHash) throws InvalidKeySpecException, NoSuchAlgorithmException {
        char[] chars = haslo.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, petle, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Arrays.toString(hash).equals(storedHash.toString());
    }

    /**
     * Generuje nową sól.
     * 16-bajtowy ciąg bajtów jest generowany w kryptograficznie bezpieczny sposób.
     * @see SecureRandom
     * @return 16 bajtowy ciąg znaków - sól
     * @throws NoSuchAlgorithmException Algorytm generujący jest niedostępny.
     */
    private static byte @NotNull [] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
