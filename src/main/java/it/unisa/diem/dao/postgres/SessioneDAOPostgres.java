package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SessioneDAOPostgres implements SessioneDAO {

    private final String url;
    private final String user;
    private final String pass;
    private final UtenteDAOPostgres utenteDAO;

    public SessioneDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.utenteDAO = new UtenteDAOPostgres(url, user, pass);
    }

    public UtenteDAOPostgres getUtenteDAO() {
        return utenteDAO;
    }

    @Override
    public Optional<Sessione> selectById(long id) throws SQLException {

        Optional<Sessione> result = Optional.empty();

        String query = "SELECT * FROM sessione WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setLong(1, id);
            ResultSet rs = cmd.executeQuery();

            Sessione sessione = null;

            if (rs.next()) {
                sessione = getSession(rs);
            }

            result = Optional.ofNullable(sessione);

        }

        return result;

    }

    @Override
    public Optional<Sessione> selectByUser(String username) throws SQLException {

        Optional<Sessione> result = Optional.empty();

        String query = "SELECT * FROM sessione WHERE utente = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setString(1, username);
            ResultSet rs = cmd.executeQuery();

            Sessione sessione = null;

            if (rs.next()) {
                sessione = getSession(rs);
            }

            result = Optional.ofNullable(sessione);

        }

        return result;

    }

    @Override
    public List<Sessione> selectAll() throws SQLException {

        List<Sessione> sessioni = new ArrayList<>();

        String query = "SELECT * FROM sessione ORDER BY dataInizio DESC";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                sessioni.add ( getSession(rs) );
            }

        }

        return sessioni;

    }

    @Override
    public void insert(Sessione sessione) throws SQLException {

        String query = "INSERT INTO sessione " +
                "(utente, completato, dataInizio, punteggio) " +
                "VALUES (?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query, Statement.RETURN_GENERATED_KEYS) ){

            setSessionForInsert(cmd, sessione);

            cmd.executeUpdate();

            try (ResultSet rs = cmd.getGeneratedKeys()) {

                if (rs.next()) {
                    sessione.setId(rs.getLong(1));
                }
            }

        }

    }

    @Override
    public void update(Sessione sessione) throws SQLException {

        String query = "UPDATE sessione " +
                "SET completato = ?, punteggio = ? " +
                "WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            setSessionForUpdate(cmd, sessione);

            cmd.executeUpdate();

        }

    }

    @Override
    public void delete(Sessione sessione) throws SQLException {

        String query = "DELETE FROM sessione WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

            setSessionForDelete(cmd, sessione);

            cmd.executeUpdate();

        }

    }

    private Sessione getSession(ResultSet rs) throws SQLException {

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

    private Utente getUser(String username) throws SQLException {

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

    private void setSessionForUpdate(PreparedStatement cmd, Sessione sessione) throws SQLException {

        cmd.setBoolean(1, sessione.isCompletato());
        cmd.setInt(2, sessione.getPunteggio());
        cmd.setLong(3, sessione.getId());

    }

    private void setSessionForDelete(PreparedStatement cmd, Sessione sessione) throws SQLException {

        cmd.setLong(1, sessione.getId());

    }


}
