package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.StoricoSessioneDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.StoricoSessione;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class StoricoSessioneDAOPostgres implements StoricoSessioneDAO {

    private final String url;
    private final String user;
    private final String pass;
    private final SessioneDAOPostgres sessioneDAO;

    public StoricoSessioneDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        sessioneDAO = new SessioneDAOPostgres(url, user, pass);
    }

    public SessioneDAOPostgres getSessioneDAO() {
        return sessioneDAO;
    }

    @Override
    public List<StoricoSessione> selectByUser(String username) {

        List<StoricoSessione> sessioni = new ArrayList<>();

        String query = "SELECT storico.* " +
                        "FROM STORICOSESSIONE storico JOIN SESSIONE s ON storico.id_sessione=s.id " +
                            "WHERE s.utente = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            cmd.setString(1, username);

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                sessioni.add ( getSessionHistory(rs) );
            }

        } catch (SQLException e) {

            throw new DBException("ERRORE: Impossibile selezionare le sessioni",e);

        }

        return sessioni;

    }

    @Override
    public Optional<StoricoSessione> selectById(long id) {

        Optional<StoricoSessione> result = Optional.empty();

        String query = "SELECT * FROM STORICOSESSIONE WHERE id_sessione = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setLong(1, id);
            ResultSet rs = cmd.executeQuery();

            StoricoSessione sessione = null;

            if (rs.next()) {
                sessione = getSessionHistory(rs);
            }

            result = Optional.ofNullable(sessione);

        } catch (SQLException e) {

            throw new DBException("ERRORE: Impossibile recuperare info sulla sessione " + id, e);

        }

        return result;

    }

    @Override
    public List<StoricoSessione> selectAll() {

        List<StoricoSessione> sessioni = new ArrayList<>();

        String query = "SELECT * FROM STORICOSESSIONE";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                sessioni.add ( getSessionHistory(rs) );
            }

        } catch (SQLException e) {

            throw new DBException("ERRORE: Impossibile selezionare le sessioni",e);
        }

        return sessioni;

    }

    public Map<String, Integer> selectByTopRanking(Difficolta difficolta) {

        Map<String, Integer> classifica = new LinkedHashMap<>();

        String query = "SELECT u.username as Utente, SUM(s.punteggioOttenuto) as Punti " +
                "FROM SESSIONE s " +
                "JOIN STORICOSESSIONE ss ON s.id = ss.id_sessione " +
                "JOIN SESSIONEDOCUMENTO sd ON s.id = sd.id_sessione " +
                "JOIN utente u ON s.utente = u.username " +
                "WHERE sd.difficolta = ? GROUP BY u.username ORDER BY Punti DESC";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            cmd.setString (1, difficolta.toString());

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {

                classifica.put(rs.getString(1), rs.getInt(2));

            }

        } catch (SQLException e) {

            throw new DBException("ERRORE: Impossibile generare la classifica",e);
        }

        return classifica;

    }



    private StoricoSessione getSessionHistory(ResultSet rs) throws SQLException {

        StoricoSessione storicoSessione = null;

        long idSessione = rs.getLong("id_sessione");

        Sessione sessione = getSession(idSessione);

        LocalDateTime dataFine = rs.getTimestamp("dataFine").toLocalDateTime();

        storicoSessione = new StoricoSessione(sessione, dataFine);

        return storicoSessione;

    }


    private Sessione getSession(long idSessione) {

        Optional<Sessione> optionalSessione = sessioneDAO.selectById(idSessione);

        return optionalSessione.orElseThrow(() ->
                new DBException("ERRORE: Sessione " + idSessione + " non trovata!")
        );
    }

}
