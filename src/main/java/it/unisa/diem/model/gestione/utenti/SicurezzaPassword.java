package it.unisa.diem.model.gestione.utenti;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Classe per la gestione della sicurezza delle password.
 * Fornisce metodi per generare salt, hashare password e verificarle.
 */
public final class SicurezzaPassword {

    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    /**
     * Genera l'hash di una password utilizzando un salt.
     *
     * @param password La password da hashare
     * @param salt Il salt da utilizzare per l'hashing
     * @return La password hashata come stringa Base64
     */
    public static String hashPassword(String password, byte[] salt) {
        byte[] hashedPassword = null;

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            hashedPassword = skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Errore algortimo di hashing: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.err.println("Errore hashing password: " + e.getMessage());
        }

        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    /**
     * Genera un salt casuale per l'hashing della password.
     *
     * @return Un array di byte contenente il salt generato
     */
    public static byte[] generaSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Verifica se una password inserita corrisponde alla password hashata.
     *
     * @param passwordInserita La password da verificare
     * @param passwordHashata La password hashata memorizzata
     * @param salt Il salt utilizzato per l'hashing originale
     * @return true se le password corrispondono, false altrimenti
     */
    public static boolean verificaPassword(String passwordInserita, String passwordHashata, byte[] salt) {
        String hashed = hashPassword(passwordInserita, salt);
        return hashed.equals(passwordHashata);
    }
}