package it.unisa.diem.test;

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

        try(InputStream is= DatabaseTest.class.getClassLoader().getResourceAsStream("config/config.properties")){
            Properties prop = new Properties();
            if(is == null)
                throw new IOException();
            prop.load(is);
            String url=prop.getProperty("database.url");
            String username=prop.getProperty("database.user");
            String password=prop.getProperty("database.password");
            System.out.println(url);
            System.out.println(username);
            System.out.println(password);

            Connection conn = DriverManager.getConnection(url,username,password);
            System.out.println("Connessione Database");
            conn.close();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
