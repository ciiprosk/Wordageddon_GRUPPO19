/**
 * @file UtenteDAOPostgres.java
 * @brief Implementazione PostgreSQL per la gestione degli utenti
 */
package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.UtenteDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Ruolo;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.dbpool.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @class UtenteDAOPostgres
 * @brief Implementazione concreta di UtenteDAO per PostgreSQL
 *
 * Gestisce tutte le operazioni CRUD per gli utenti:
 * - Registrazione e autenticazione
 * - Gestione profili
 * - Verifica credenziali
 * - Gestione ruoli
 */
public class UtenteDAOPostgres implements UtenteDAO {

    /**
     * @brief Costruttore di default
     */
    public UtenteDAOPostgres() {}

    /**
     * @brief Verifica se un'email è già registrata
     * @param email Email da verificare
     * @return true se l'email esiste, false altrimenti
     * @throws DBException in caso di errori di database
     */
    @Override
    public boolean emailAlreadyExists(String email) throws SQLException, DBException {

        String query = "SELECT COUNT(*) FROM utente WHERE email = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setString(1, email);

            ResultSet rs = cmd.executeQuery();

            if (rs.next()) {

                int count = rs.getInt(1);

                if (count > 0) {

                    return true;

                }
                else return false;

            } else return false;


        } catch (SQLException e) {
            throw new DBException("ERRORE: E-mail " + email + " già esistente!",e);
        }

    }

    /**
     * @brief Verifica se uno username è già in uso
     * @param username Username da verificare
     * @return true se lo username esiste, false altrimenti
     * @throws DBException in caso di errori di database
     */
    @Override
    public boolean usernameAlreadyExists(String username) throws SQLException, DBException {

        String query = "SELECT COUNT(*) FROM utente WHERE username = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd = connection.prepareStatement (query) ) {

            cmd.setString(1, username);

            ResultSet rs = cmd.executeQuery();

            if (rs.next()) {

                int count = rs.getInt(1);

                if (count > 0) {

                    return true;

                } else return false;

            }   else return false;

        }
        catch (SQLException e) {
            throw new DBException("ERRORE: username " + username + " già esistente!",e);
        }

    }

    /**
     * @brief Ricerca un utente per username
     * @param username Username da cercare
     * @return Optional contenente l'utente se trovato
     * @throws DBException in caso di errori di database
     */
    @Override
    public Optional<Utente> selectByUsername(String username) throws SQLException, DBException {

        Optional<Utente> result = Optional.empty();

        String query = "SELECT * FROM utente WHERE username = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd = connection.prepareStatement (query) ){

                        cmd.setString(1, username);
                        ResultSet rs = cmd.executeQuery();

                        Utente utente = null;

                        if (rs.next()) {
                            utente = getUser(rs);
                        }

                        result = Optional.ofNullable(utente);

        } catch (SQLException e) {
            throw new DBException("ERRORE: utente " + username + " non trovato!",e);
        }

        return result;

    }

    /**
     * @brief Recupera tutti gli utenti registrati
     * @return Lista di tutti gli utenti
     * @throws DBException in caso di errori di database
     */
    @Override
    public List<Utente> selectAll() throws SQLException, DBException {

        List<Utente> utenti = new ArrayList<>();

        String query = "SELECT * FROM utente";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

                        ResultSet rs = cmd.executeQuery();

                        while (rs.next()) {
                            utenti.add ( getUser(rs) );
                        }

        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile recuperare le informazioni sugli utenti!",e);
        }

        return utenti;
    }

    /**
     * @brief Registra un nuovo utente
     * @param utente L'utente da registrare
     * @throws DBException in caso di errori di database
     * @note Salva anche il salt per l'hash della password
     */
    @Override
    public void insert(Utente utente) throws SQLException, DBException {

        String query = "INSERT INTO utente (username,email,password,salt,ruolo) " +
                "VALUES (?,?,?,?,?)";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

                        setUserForInsert(cmd, utente);

            int lines = cmd.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile inserire " + utente.getUsername() + "!",e);
        }

    }

    /**
     * @brief Aggiorna i dati di un utente (senza modificare lo username)
     * @param utente L'utente con i dati aggiornati
     * @throws DBException in caso di errori di database
     */
    @Override
    public void update(Utente utente) throws SQLException, DBException {     //NON PREVEDE l'aggiornamento di username

        String query = "UPDATE utente " +
                "SET email=?, password=?, ruolo=? " +
                "WHERE username = ?";

        try (Connection connection = ConnectionManager.getConnection();

                 PreparedStatement cmd=connection.prepareStatement (query) ){

                setUserForUpdate(cmd, utente);

            int lines = cmd.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

            }  catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile aggiornare " + utente.getUsername() + "!",e);
        }

    }

    /**
     * @brief Aggiorna tutti i dati di un utente (incluso lo username)
     * @param oldUsername Username attuale da modificare
     * @param utente Nuovi dati dell'utente
     * @throws DBException in caso di errori di database
     */
    @Override
    public void update(String oldUsername, Utente utente) throws SQLException, DBException {     //PREVEDE aggiornamento username

        String query = "UPDATE utente " +
                "SET username=?, email=?, password=?, ruolo=? " +
                "WHERE username = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement
                     (query)){

            setUserForUpdate(cmd, oldUsername, utente);

            int lines = cmd.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        }  catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile aggiornare " + utente.getUsername() + "!",e);
        }

    }

    /**
     * @brief Elimina un utente
     * @param utente L'utente da eliminare
     * @throws DBException in caso di errori di database
     */
    @Override
    public void delete(Utente utente) throws SQLException, DBException {

        String query = "DELETE FROM utente WHERE username = ?";

        try (Connection connection = ConnectionManager.getConnection();

            PreparedStatement cmd = connection.prepareStatement (query) ){

            setUserForDelete(cmd, utente);

            int lines = cmd.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        }  catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile eliminare " + utente.getUsername() + "!",e);
        }

    }

    /**
     * @private
     * @brief Crea un oggetto Utente da un ResultSet
     * @param rs ResultSet contenente i dati
     * @return Utente creato
     * @throws SQLException in caso di errori SQL
     */
    private Utente getUser(ResultSet rs) throws SQLException {

        Utente utente = null;

        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        byte[] salt = rs.getBytes("salt");
        Ruolo ruolo = Ruolo.valueOf(rs.getString("ruolo"));

        utente = new Utente(username, email, password, salt, ruolo);

        return utente;

    }

    /**
     * @private
     * @brief Prepara lo statement per l'inserimento di un utente
     */
    private void setUserForInsert(PreparedStatement cmd, Utente utente) throws SQLException {
        cmd.setString(1, utente.getUsername());
        cmd.setString(2, utente.getEmail());
        cmd.setString(3, utente.getHashedPassword());
        cmd.setBytes(4, utente.getSalt());
        cmd.setObject(5, utente.getRuolo().name(), java.sql.Types.OTHER);
    }

    /**
     * @private
     * @brief Prepara lo statement per l'aggiornamento (senza username)
     */
    private void setUserForUpdate(PreparedStatement cmd, Utente utente) throws SQLException {
        cmd.setString(1, utente.getEmail());
        cmd.setString(2, utente.getHashedPassword());
        cmd.setObject(3, utente.getRuolo().name(), java.sql.Types.OTHER);
        cmd.setString(4, utente.getUsername());
    }

    /**
     * @private
     * @brief Prepara lo statement per l'aggiornamento (con username)
     */
    private void setUserForUpdate(PreparedStatement cmd, String oldUsername, Utente utente) throws SQLException {
        cmd.setString(1, utente.getUsername());
        cmd.setString(2, utente.getEmail());
        cmd.setString(3, utente.getHashedPassword());
        cmd.setObject(4, utente.getRuolo().name(), java.sql.Types.OTHER);
        cmd.setString(5, oldUsername);
    }

    /**
     * @private
     * @brief Prepara lo statement per l'eliminazione di un utente
     */
    private void setUserForDelete(PreparedStatement cmd, Utente utente) throws SQLException {
        cmd.setString(1, utente.getUsername());
    }

}
