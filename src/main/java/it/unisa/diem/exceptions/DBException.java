package it.unisa.diem.exceptions;

/**
 * Eccezione personalizzata per errori di accesso al database.
 */
public class DBException extends Exception {

    /**
     * Costruisce una nuova DBException con un messaggio e la causa dell'eccezione.
     *
     * @param message Il messaggio descrittivo dell'eccezione.
     * @param cause   L'eccezione originale che ha causato questa eccezione.
     */
    public DBException(String message, Throwable cause) {
        super(message);
        cause.printStackTrace();
    }

    /**
     * Costruisce una nuova DBException con solo un messaggio.
     *
     * @param message Il messaggio descrittivo dell'eccezione.
     */
    public DBException(String message) {
        super(message);
    }
}
