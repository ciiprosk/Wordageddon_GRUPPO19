package it.unisa.diem.model.gestione.utenti;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class SicurezzaPassword {

    private static final int ITERATION_COUNT = 65536;       //numero di cicli eseguiti per calcolare la passowrd hashata
    private static final int KEY_LENGTH = 256;      //lunghezza in bit della password hashata


    public static String hashPassword(String password, byte[] salt) {

        byte[] hashedPassword = null;

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);

        try {

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");    //usiamo l'algoritmo di hashing SHA256

            hashedPassword = skf.generateSecret(spec).getEncoded();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Errore algortimo di hashing: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.err.println("Errore hashing password: " + e.getMessage());
        }

        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public static byte[] generaSalt() {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;

    }

    /*
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    MANCA UN METODO PER VERIFICARE CHE LA PASSWORD INSERITA SIA CORRETTA
    CREDO ANDREBBE CREATA UN'ALTRA CLASSE PER FARE QUESTA COSA
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */

}
