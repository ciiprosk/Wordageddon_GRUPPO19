package it.unisa.diem.utility.dbpool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.unisa.diem.utility.PropertiesLoader;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private static final HikariDataSource dataSource;


    //grazie a static si iinizalizza AUTOMATICAMENTE DA SOLA la prima volta che viene chiamata
    static {
        HikariConfig config = new HikariConfig();

        //miei dati del db
        //creo letteralmente una piscina di connessionni, limitandone iln numero
        config.setJdbcUrl(PropertiesLoader.getProperty("database.url"));
        config.setUsername(PropertiesLoader.getProperty("database.user"));
        config.setPassword(PropertiesLoader.getProperty("database.password"));

        config.setMaximumPoolSize(5);             // massimo 5 connessioni attive
        config.setMinimumIdle(1);                 // tiene 1 connessione "pronta"
        config.setIdleTimeout(30000);             // libera connessioni inutilizzate dopo 30s
        config.setConnectionTimeout(10000);       // attende max 10s se tutte sono occupate
        config.setValidationTimeout(3000);        // controlla la connessione in max 3s
        config.setConnectionTestQuery("SELECT 1");// test per validare connessioni

        dataSource = new HikariDataSource(config);
    }

    private ConnectionManager() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}