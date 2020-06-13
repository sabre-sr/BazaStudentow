import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;

public abstract class Passwords {
    private static int petle = 1000;
    public static @NotNull
    ImmutablePair<String, byte[]> generateHashPair(String haslo) throws InvalidKeySpecException, NoSuchAlgorithmException {
        char[] chars = haslo.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, petle, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return new ImmutablePair<>(Arrays.toString(hash), salt);
    }

    public static boolean validatePassword(@NotNull String haslo, byte[] salt, @NotNull String storedHash) throws InvalidKeySpecException, NoSuchAlgorithmException {
        char[] chars = haslo.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, petle, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Arrays.toString(hash).equals(storedHash.toString());
    }
    private static byte @NotNull [] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
