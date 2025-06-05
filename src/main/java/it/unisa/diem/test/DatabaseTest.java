package it.unisa.diem.test;

import java.io.InputStream;
import java.util.Properties;

/*Java cerca i file nelle seguenti posizioni:

src/main/resources (in progetti Maven/Gradle) → Copiati in target/classes o build/resources.

Librerie esterne (JAR) → File inclusi nel classpath.

Per accedere a queste risorse, Java usa il ClassLoader, che è responsabile di:

Trovare i file nel classpath.

Caricare classi e risorse in memoria.*/

public class DatabaseTest {
    public static void main(String[] args) {
        try(InputStream fis = DatabaseTest.class.getClassLoader().getResourceAsStream("config/config.properties");) {
            Properties prop = new Properties();

            prop.load(fis);
            String url= prop.getProperty("database.url");
            System.out.println(url);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
