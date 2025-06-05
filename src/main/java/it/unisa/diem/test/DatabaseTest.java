package it.unisa.diem.test;

import it.unisa.diem.dao.interfacce.AnalisiDAO;
import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.dao.postgres.AnalisiDAOPostgres;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.utility.PropertiesLoader;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/*Java cerca i file nelle seguenti posizioni:

src/main/resources (in progetti Maven/Gradle) → Copiati in target/classes o build/resources.

Librerie esterne (JAR) → File inclusi nel classpath.

Per accedere a queste risorse, Java usa il ClassLoader, che è responsabile di:

Trovare i file nel classpath.

Caricare classi e risorse in memoria.*/

public class DatabaseTest {
    public static void main(String[] args) {

        /*try(InputStream fis = DatabaseTest.class.getClassLoader().getResourceAsStream("ITA/facile/storiella.txt")) {
            try(BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }*/
        PropertiesLoader.init();
        String url=PropertiesLoader.getProperty("database.url");
        System.out.println("url:"+url);

    }
}