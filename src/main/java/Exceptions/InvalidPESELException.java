package Exceptions;

/**
 * Wyjątek zgłaszający nieprawidłowość numeru PESEL.
 * @see Utils.PESEL
 */
public class InvalidPESELException extends Exception {
    public InvalidPESELException(String message) {
        super(message);
    }
}
