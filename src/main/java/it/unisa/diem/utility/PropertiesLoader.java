package it.unisa.diem.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe di utilità per il caricamento delle proprietà da file .properties,
 * utile ad esempio per configurazioni come la connessione al database.
 */
public class PropertiesLoader {

    private static Properties prop;

    /**
     * Inizializza il loader delle proprietà caricando il file properties
     * dal classpath. Se già inizializzato, il metodo non ha effetto.
     */
    public static void init() {
        if (prop == null) {
            prop = new Properties();
        }

        try (InputStream is = PropertiesLoader.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (is == null) {
                System.out.println("Config file not found");
                return;
            }
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce il valore della proprietà associata alla chiave specificata.
     *
     * @param key la chiave della proprietà
     * @return il valore della proprietà
     */
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
