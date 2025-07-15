package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.dao.interfacce.StoricoSessioneDAO;
import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.StoricoSessione;
import it.unisa.diem.model.gestione.sessione.VoceStorico;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.dbpool.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class StoricoSessioneDAOPostgres implements StoricoSessioneDAO {

    private String url;
    private String user;
    private String pass;
    private SessioneDAO sessioneDAO;
    private UtenteDAO utenteDAO;

    public StoricoSessioneDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        sessioneDAO = new SessioneDAOPostgres(url, user, pass);
        utenteDAO = new UtenteDAOPostgres(url, user, pass);
    }

    public StoricoSessioneDAOPostgres() {
        sessioneDAO = new SessioneDAOPostgres(url, user, pass);
        utenteDAO = new UtenteDAOPostgres(url, user, pass);
    }
    public SessioneDAO getSessioneDAO() {
        return sessioneDAO;
    }
    
    @Override
    public List<StoricoSessione> selectByUser(String username) throws SQLException, DBException {

        List<StoricoSessione> sessioni = new ArrayList<>();

        String query = "SELECT storico.id_sessione, storico.dataFine, s.id, s.utente, s.dataInizio, s.punteggioottenuto, s.completato " +
                "FROM STORICOSESSIONE storico JOIN SESSIONE s ON storico.id_sessione=s.id " +
                "WHERE s.utente = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

            cmd.setString(1, username);

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                sessioni.add ( getSessionHistory(rs) );
            }

        }

        return sessioni;

    }

    @Override
    public Optional<StoricoSessione> selectById(long id) throws SQLException, DBException {

        Optional<StoricoSessione> result = Optional.empty();

        String query = "SELECT * FROM STORICOSESSIONE ss " +
                "JOIN SESSIONE s ON ss.id_sessione = s.id " +
                "WHERE id_sessione = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setLong(1, id);
            ResultSet rs = cmd.executeQuery();

            StoricoSessione sessione = null;

            if (rs.next()) {
                sessione = getSessionHistory(rs);
            }

            result = Optional.ofNullable(sessione);

        }

        return result;

    }

    @Override
    public List<StoricoSessione> selectAll() throws SQLException, DBException {

        List<StoricoSessione> sessioni = new ArrayList<>();

        String query = "SELECT * FROM STORICOSESSIONE"
                + " JOIN SESSIONE s ON STORICOSESSIONE.id_sessione = s.id";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                sessioni.add ( getSessionHistory(rs) );
            }

        }

        return sessioni;

    }

    public List<VoceStorico> selectByLastSessions(String username, Difficolta difficolta) throws DBException {
        List<VoceStorico> storico = new ArrayList<>();

        String query = "SELECT ss.dataFine, s.punteggioOttenuto, d.difficolta, d.lingua " +
                "FROM STORICOSESSIONE ss JOIN SESSIONE s ON ss.id_sessione = s.id " +
                "JOIN SESSIONEDOCUMENTO sd ON sd.sessione = s.id " +
                "JOIN DOCUMENTO d ON d.nome = sd.documento " +
                "WHERE d.difficolta = ? AND s.utente = ? " +
                "ORDER BY ss.dataFine DESC " +
                "LIMIT 10";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {

            cmd.setObject(1, difficolta.name(), Types.OTHER);
            cmd.setString(2, username);

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                storico.add(getLastSessions(rs));
            }

        } catch (SQLException e) {
            throw new DBException("Errore durante la selectByLastSessions: " + e.getMessage(), e);
        }

        return storico;
    }



    public List<VoceClassifica> selectByTopRanking(Difficolta difficolta) throws DBException {

        List<VoceClassifica> classifica = new ArrayList<>();

        String query =

                "SELECT u.username, AVG(s.punteggioottenuto) AS media_punteggio, SUM(s.punteggioottenuto) AS somma_punteggio " +
                        "FROM sessione s "+
                        "JOIN storicosessione ss ON s.id = ss.id_sessione " +
                        "JOIN sessionedocumento sd ON s.id = sd.sessione " +
                        "JOIN utente u ON s.utente = u.username "+
                        "JOIN documento d ON sd.documento=d.nome "+
                        "WHERE d.difficolta = ? "+
                        "GROUP BY u.username "+
                        "ORDER BY media_punteggio DESC "+
                        "LIMIT 10";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

            cmd.setObject(1, difficolta.name(), Types.OTHER);

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {

                classifica.add(getLeaderboard(rs));

            }

        }   catch(SQLException e){
            throw new DBException("Errore: impossibile caricare la Leaderboard per difficoltà " + difficolta + "!",e);
        }

        return classifica;

    }

    private VoceClassifica getLeaderboard(ResultSet rs) throws SQLException {

        VoceClassifica vc = null;

        String username = rs.getString("username");

        int sum = rs.getInt("somma_punteggio");

        double avg = rs.getDouble("media_punteggio");

        vc = new VoceClassifica(username, sum, avg);

        return vc;

    }

    private StoricoSessione getSessionHistory(ResultSet rs) throws SQLException, DBException {

        StoricoSessione storicoSessione = null;
        //prima di cosrtuire StoricoSessione devo costruirmi utente
        Utente utente= utenteDAO.selectByUsername(rs.getString("utente")).orElseThrow(()->new DBException("Utente non trovato"));

        long idSessione = rs.getLong("id_sessione");


        LocalDateTime dataFine = rs.getTimestamp("dataFine").toLocalDateTime();

        storicoSessione = new StoricoSessione(idSessione, utente,rs.getTimestamp("dataInizio").toLocalDateTime(),
                rs.getInt("punteggioottenuto"), dataFine
        );

        return storicoSessione;

    }


    private Sessione getSession(long idSessione) throws SQLException, DBException {

        Optional<Sessione> optionalSessione = sessioneDAO.selectById(idSessione);

        return optionalSessione.orElseThrow(() ->
                new DBException("ERRORE: Sessione " + idSessione + " non trovata!")
        );

    }

    private VoceStorico getLastSessions(ResultSet rs) throws SQLException {

        VoceStorico voceStorico = null;

        LocalDateTime dataFine = rs.getTimestamp("dataFine").toLocalDateTime();

        int punteggio = rs.getInt("punteggioOttenuto");

        Lingua lingua = Lingua.valueOf(rs.getString("lingua"));

        voceStorico = new VoceStorico(dataFine, punteggio, lingua);

        return voceStorico;

    }

}
