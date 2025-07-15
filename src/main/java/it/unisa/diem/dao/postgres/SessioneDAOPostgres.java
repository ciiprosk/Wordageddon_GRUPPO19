package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.dbpool.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SessioneDAOPostgres implements SessioneDAO {

    private  String url;
    private  String user;
    private  String pass;
    private  UtenteDAO utenteDAO;

    public SessioneDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.utenteDAO = new UtenteDAOPostgres(url, user, pass);
    }

    public SessioneDAOPostgres(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public UtenteDAO getUtenteDAO() {
        return utenteDAO;
    }

    @Override
    public Optional<Sessione> selectById(long id) throws DBException {

        Optional<Sessione> result = Optional.empty();

        String query = "SELECT * FROM sessione WHERE id = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setLong(1, id);
            ResultSet rs = cmd.executeQuery();

            Sessione sessione = null;

            if (rs.next()) {
                sessione = getSession(rs);
            }

            result = Optional.ofNullable(sessione);

        } catch(SQLException e){
            throw new DBException("ERRORE: impossibile trovare la sessione attiva con id " + id + "!",e);
        }

        return result;

    }

    @Override
    public Optional<Sessione> selectByUser(String username) throws DBException {

        Optional<Sessione> result = Optional.empty();

        String query = "SELECT * FROM sessione WHERE utente = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setString(1, username);
            ResultSet rs = cmd.executeQuery();

            Sessione sessione = null;

            if (rs.next()) {
                sessione = getSession(rs);
            }

            result = Optional.ofNullable(sessione);

        }  catch(SQLException e){
            throw new DBException("ERRORE: impossibile trovare la sessione attiva di " + username + "!",e);
        }

        return result;

    }

    @Override
    public List<Sessione> selectAll() throws DBException {

        List<Sessione> sessioni = new ArrayList<>();

        String query = "SELECT * FROM sessione ORDER BY dataInizio DESC";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                sessioni.add ( getSession(rs) );
            }

        }  catch(SQLException e){
            throw new DBException("ERRORE: impossibile selezionare le sessioni attive!",e);
        }

        return sessioni;

    }

    @Override
    public void insert(Sessione sessione) throws DBException {

        String query = "INSERT INTO sessione " +
                "(utente, completato, dataInizio, punteggioottenuto) " +
                "VALUES (?,?,?,?)";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query, Statement.RETURN_GENERATED_KEYS) ){

            setSessionForInsert(cmd, sessione);

            int lines = cmd.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

            try (ResultSet rs = cmd.getGeneratedKeys()) {

                if (rs.next()) {
                    sessione.setId(rs.getLong(1));
                }
            }

        }    catch(SQLException e){
            throw new DBException("Errore: impossibile inserire la sessione attiva " + sessione.getId() + "!",e);
        }

    }

    @Override
    public void update(Sessione sessione) throws DBException {

        String query = "UPDATE sessione \n" +
                "SET completato = ?, punteggioottenuto = ? \n" +
                "WHERE id = ?\n";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

            setSessionForUpdate(cmd, sessione);

            int lines = cmd.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        }   catch(SQLException e){
                throw new DBException("Errore: impossibile aggiornare la sessione " + sessione.getId() + "!",e);
        }

    }

    @Override
    public void delete(Sessione sessione) throws DBException {

        String query = "DELETE FROM sessione WHERE id = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd = connection.prepareStatement (query) ){

            setSessionForDelete(cmd, sessione);

            int lines = cmd.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        }   catch(SQLException e){
                throw new DBException("Errore: impossibile eliminare la sessione " + sessione.getId() + "!",e);
        }

    }

    public void delete(long sessioneId) throws DBException {
        String query = "DELETE FROM sessione WHERE id = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {

            cmd.setLong(1, sessioneId);

            int lines = cmd.executeUpdate();
            if (lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

            System.out.println("✅ Sessione eliminata: ID = " + sessioneId);

        } catch (SQLException e) {
            throw new DBException("Errore: impossibile eliminare la sessione " + sessioneId + "!", e);
        }
    }


    private Sessione getSession(ResultSet rs) throws SQLException, DBException {

        Sessione sessione = null;

        long id = rs.getLong("id");

        String username = rs.getString("utente");

        Utente utente = getUser(username);

        LocalDateTime inizio = rs.getTimestamp("dataInizio").toLocalDateTime();

        int punteggio = rs.getInt("punteggioottenuto");

        boolean completato = rs.getBoolean("completato");

        sessione = new Sessione(id, utente, inizio, punteggio, completato);

        return sessione;

    }

    private Utente getUser(String username) throws SQLException, DBException {

        Optional<Utente> optionalUtente = utenteDAO.selectByUsername(username);

        return optionalUtente.orElseThrow(() ->
                new DBException("ERRORE: Utente " + username + " non trovato!")
        );

    }

    private void setSessionForInsert(PreparedStatement cmd, Sessione sessione) throws SQLException {

        cmd.setString(1, sessione.getUtente().getUsername());
        cmd.setBoolean(2, sessione.isCompletato());
        cmd.setTimestamp(3, Timestamp.valueOf(sessione.getInizio()));
        cmd.setInt(4, sessione.getPunteggio());


    }

    private void setSessionForUpdate(PreparedStatement cmd, Sessione sessione) throws SQLException{

        cmd.setBoolean(1, sessione.isCompletato());
        cmd.setInt(2, sessione.getPunteggio());
        cmd.setLong(3, sessione.getId());

    }

    private void setSessionForDelete(PreparedStatement cmd, Sessione sessione) throws SQLException{

        cmd.setLong(1, sessione.getId());

    }


}
