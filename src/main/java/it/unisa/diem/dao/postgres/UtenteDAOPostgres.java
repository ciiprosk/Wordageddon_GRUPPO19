package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UtenteDAOPostgres implements UtenteDAO {

    private final String URL = "jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon";
    private final String USER = "postgres";
    private final String PASS = "Farinotta01_";


    @Override
    public Optional<Utente> selectById(long id) {
        
        return Optional.empty();

    }

    @Override
    public List<Utente> selectAll() {

        return Collections.emptyList();
    }

    @Override
    public void insert(Utente utente) {
        //inserisci il nuovo utente con insert
        //prima bisogna fare hasing della password inserita
        // da dove prendo i dati???? me lo passa utente
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement cmd=connection.prepareStatement("INSERT INTO Utente VALUES (?,?,?)");){
                cmd.setString(1, utente.getUsername());
                cmd.setString(2,utente.getEmail());
                cmd.setString(3,utente.getHashedPassword());
                /*  da decidere, ma nel caso in cui salviamo anche il salt
                    cmd.setString(4,utente.getSalt().toString());
                 */
                cmd.executeUpdate();

        } catch (SQLException e) {
       //     throw new DBException("Errore insert",e);     //la creiamo?
        }
    }

        @Override
    public void update(Utente utente) {

    }

    @Override
    public void delete(long id) {
    }

}
