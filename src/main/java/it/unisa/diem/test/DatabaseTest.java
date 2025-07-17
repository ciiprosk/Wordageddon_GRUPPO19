package it.unisa.diem.test;

import it.unisa.diem.utility.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*Java cerca i file nelle seguenti posizioni:

src/main/resources (in progetti Maven/Gradle) → Copiati in target/classes o build/resources.

Librerie esterne (JAR) → File inclusi nel classpath.

Per accedere a queste risorse, Java usa il ClassLoader, che è responsabile di:

Trovare i file nel classpath.

Caricare classi e risorse in memoria.*/

public class DatabaseTest {
    public static void main(String[] args) {


        PropertiesLoader.init();
        String url=PropertiesLoader.getProperty("database.url");
        System.out.println("url:"+url);
        String user=PropertiesLoader.getProperty("database.user");
        System.out.println("user:"+user);
        String password=PropertiesLoader.getProperty("database.password");
        System.out.println("password:"+password);

        try{
            Connection co=DriverManager.getConnection(url, user, password);
            System.out.println("okay");
        } catch (SQLException e) {
            System.out.println("noooooo");
        }

    }
}