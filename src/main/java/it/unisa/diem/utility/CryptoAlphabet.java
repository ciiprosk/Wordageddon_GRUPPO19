package it.unisa.diem.utility;

/**
 * Classe di utilità per cifrare e decifrare stringhe usando un alfabeto personalizzato.
 * L'algoritmo utilizza una semplice sostituzione di caratteri tra un alfabeto chiaro e uno cifrato.
 */
public class CryptoAlphabet {

    /**
     * Alfabeto originale usato come base per la cifratura.
     */
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.!?";

    /**
     * Alfabeto cifrato corrispondente all'alfabeto originale.
     * Ogni carattere ha un mapping uno-a-uno con l'alfabeto in chiaro.
     */
    private static final String alfabeto_cifrato = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm9876543210£,.!?";

    /**
     * Cifra una stringa usando l'alfabeto personalizzato.
     * I caratteri non presenti nell'alfabeto vengono lasciati invariati.
     *
     * @param line la stringa da cifrare
     * @return la stringa cifrata
     */
    public static String cripta(String line) {
        StringBuffer str = new StringBuffer();
        for (char carattere : line.toCharArray()) {
            int posAlphabet = alphabet.indexOf(carattere);
            if (posAlphabet != -1) {
                char criptato = alfabeto_cifrato.charAt(posAlphabet);
                str.append(criptato);
            } else {
                str.append(carattere);
            }
        }
        return str.toString();
    }

    /**
     * Decifra una stringa precedentemente cifrata
     * I caratteri non presenti nell'alfabeto cifrato vengono lasciati invariati.
     *
     * @param line la stringa da decifrare
     * @return la stringa decifrata
     */
    public static String decripta(String line) {
        StringBuffer str = new StringBuffer();
        for (char carattere : line.toCharArray()) {
            int posCifrato = alfabeto_cifrato.indexOf(carattere);
            if (posCifrato != -1) {
                char decriptato = alphabet.charAt(posCifrato);
                str.append(decriptato);
            } else {
                str.append(carattere);
            }
        }
        return str.toString();
    }
}
