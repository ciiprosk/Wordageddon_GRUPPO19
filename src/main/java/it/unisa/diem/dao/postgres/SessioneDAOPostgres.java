/**
 *  SessioneDAOPostgres.java
 *  Implementazione PostgreSQL per la gestione delle sessioni
 */

package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.SessioneDAO;
import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.VoceStorico;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.dbpool.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *  SessioneDAOPostgres
 *  Implementazione concreta di SessioneDAO per PostgreSQL
 *
 * Gestisce tutte le operazioni CRUD per le sessioni:
 * - Creazione e gestione sessioni utente
 * - Recupero storico sessioni
 * - Gestione classifiche
 * - Operazioni di ranking
 */

public class SessioneDAOPostgres implements SessioneDAO {
    private  UtenteDAO utenteDAO;

    /**
     *
     * Inizializza una nuova istanza del DAO creando una dipendenza
     * con UtenteDAOPostgres per la gestione degli utenti
     */
    public SessioneDAOPostgres() {
        this.utenteDAO = new UtenteDAOPostgres();
    }

    /**
     *  Ricerca una sessione per ID
     * @param id L'ID della sessione da cercare
     * @return Optional contenente la sessione se trovata
     * @throws DBException in caso di errori di database
     */
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

    /**
     *  Ricerca l'ultima sessione di un utente
     * @param username L'username dell'utente
     * @return Optional contenente la sessione se trovata
     * @throws DBException in caso di errori di database
     */
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

    /**
     *  Recupera tutte le sessioni ordinate per data
     * @return Lista di tutte le sessioni (ordine decrescente per data)
     * @throws DBException in caso di errori di database
     */
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

    /**
     *  Inserisce una nuova sessione
     * @param sessione La sessione da inserire
     * @throws DBException in caso di errori di database
     *  Imposta l'ID generato automaticamente sulla sessione
     */

    @Override
    public void insert(Sessione sessione) throws DBException {

        String query = "INSERT INTO sessione " +
                "(utente, dataFine, dataInizio, punteggioottenuto) " +
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

    /**
     *  Aggiorna i dati di una sessione esistente
     * @param sessione La sessione con i dati aggiornati
     * @throws DBException in caso di errori di database
     */
    @Override
    public void update(Sessione sessione) throws DBException {

        String query = "UPDATE sessione \n" +
                "SET datafine = ?, punteggioottenuto = ? \n" +
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

    /**
     *  Elimina una sessione
     * @param sessione La sessione da eliminare
     * @throws DBException in caso di errori di database
     */

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

    /**
     *  Elimina una sessione per ID
     * @param sessioneId L'ID della sessione da eliminare
     * @throws DBException in caso di errori di database
     */

    @Override
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

    //LEADERBOARD & HISTORY
    /**
     *  Recupera le ultime 10 sessioni di un utente per difficoltà
     * @param username L'username dell'utente
     * @param difficolta Il livello di difficoltà da filtrare
     * @return Lista delle voci dello storico
     * @throws DBException in caso di errori di database
     */
    @Override
    public List<VoceStorico> selectByLastSessions(String username, Difficolta difficolta) throws DBException {
        List<VoceStorico> storico = new ArrayList<>();

        String query = "SELECT DISTINCT ON  (s.id) s.dataFine, s.punteggioOttenuto, d.difficolta, d.lingua " +
                "FROM SESSIONE s " +
                "JOIN SESSIONEDOCUMENTO sd ON sd.sessione = s.id " +
                "JOIN DOCUMENTO d ON d.nome = sd.documento " +
                "WHERE d.difficolta = ? AND s.utente = ? " +
                "ORDER BY s.id DESC, s.dataFine DESC " +
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

    /**
     *  Recupera la top 10 della classifica per difficoltà
     * @param difficolta Il livello di difficoltà da filtrare
     * @return Lista delle voci di classifica
     * @throws DBException in caso di errori di database
     *
     */
    @Override
    public List<VoceClassifica> selectByTopRanking(Difficolta difficolta) throws DBException {

        List<VoceClassifica> classifica = new ArrayList<>();

        String query =

                "SELECT u.username, AVG(s.punteggioottenuto) AS media_punteggio, SUM(s.punteggioottenuto) AS somma_punteggio " +
                        "FROM sessione s "+
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

    //METODI PRIVATI

    /**
     *
     *  Crea un oggetto Sessione da un ResultSet
     * @param rs Il ResultSet contenente i dati
     * @return L'oggetto Sessione creato
     * @throws SQLException in caso di errori SQL
     * @throws DBException se l'utente non viene trovato
     */
    private Sessione getSession(ResultSet rs) throws SQLException, DBException {

        Sessione sessione = null;

        long id = rs.getLong("id");

        String username = rs.getString("utente");

        Utente utente = getUser(username);

        LocalDateTime inizio = rs.getTimestamp("dataInizio").toLocalDateTime();

        int punteggio = rs.getInt("punteggioottenuto");

        LocalDateTime fine = rs.getTimestamp("dataFine").toLocalDateTime();

        sessione = new Sessione(id, utente, inizio, punteggio, fine);

        return sessione;

    }

    /**
     *
     *  Recupera un utente dal database
     * @param username L'username dell'utente
     * @return L'oggetto Utente
     * @throws DBException se l'utente non viene trovato
     */
    private Utente getUser(String username) throws SQLException, DBException {

        Optional<Utente> optionalUtente = utenteDAO.selectByUsername(username);

        return optionalUtente.orElseThrow(() ->
                new DBException("ERRORE: Utente " + username + " non trovato!")
        );

    }

    /**
     *
     *  Prepara lo statement per l'inserimento di una sessione
     */
    private void setSessionForInsert(PreparedStatement cmd, Sessione sessione) throws SQLException {

        cmd.setString(1, sessione.getUtente().getUsername());
        cmd.setTimestamp(2, Timestamp.valueOf(sessione.getFine()));
        cmd.setTimestamp(3, Timestamp.valueOf(sessione.getInizio()));
        cmd.setInt(4, sessione.getPunteggio());


    }

    /**
     *
     *  Prepara lo statement per l'aggiornamento di una sessione
     */
    private void setSessionForUpdate(PreparedStatement cmd, Sessione sessione) throws SQLException{

        cmd.setTimestamp(1, Timestamp.valueOf(sessione.getFine()));
        cmd.setInt(2, sessione.getPunteggio());
        cmd.setLong(3, sessione.getId());

    }

    /**
     *
     *  Prepara lo statement per l'eliminazione di una sessione
     */
    private void setSessionForDelete(PreparedStatement cmd, Sessione sessione) throws SQLException{

        cmd.setLong(1, sessione.getId());

    }

    /**
     *
     *  Crea una voce di classifica da un ResultSet
     */

    private VoceClassifica getLeaderboard(ResultSet rs) throws SQLException {

        VoceClassifica vc = null;

        String username = rs.getString("username");

        int sum = rs.getInt("somma_punteggio");

        double avg = rs.getDouble("media_punteggio");

        vc = new VoceClassifica(username, sum, avg);

        return vc;

    }


    /**
     *
     *  Crea una voce dello storico da un ResultSet
     */
    private VoceStorico getLastSessions(ResultSet rs) throws SQLException {

        VoceStorico voceStorico = null;

        LocalDateTime dataFine = rs.getTimestamp("dataFine").toLocalDateTime();

        int punteggio = rs.getInt("punteggioOttenuto");

        Lingua lingua = Lingua.valueOf(rs.getString("lingua"));

        voceStorico = new VoceStorico(dataFine, punteggio, lingua);

        return voceStorico;

    }


}
