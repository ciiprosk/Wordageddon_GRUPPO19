package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.SessioneDocumentoDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;
import it.unisa.diem.utility.dbpool.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta di SessioneDocumentoDAO per PostgreSQL.
 *
 * Gestisce le operazioni CRUD per le associazioni tra sessioni e documenti.
 * Utilizza un connection pool per la gestione delle connessioni al database.
 */
public class SessioneDocumentoDAOPostgres implements SessioneDocumentoDAO {

    /**
     * Costruttore di default.
     */
    public SessioneDocumentoDAOPostgres() {}

    /**
     * Recupera i documenti associati a una sessione.
     *
     * @param idSessione ID della sessione
     * @return Lista dei documenti associati
     * @throws DBException in caso di errori di database
     */
    @Override
    public List<Documento> selectDocumentsBySession(long idSessione) throws DBException {

        List<Documento> documenti = new ArrayList<>();

        String query = "SELECT d.* " +
                "FROM sessionedocumento s JOIN documento d ON s.documento = d.nome " +
                "WHERE sessione = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {

            cmd.setLong(1, idSessione);
            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                documenti.add(getDocument(rs));
            }

        } catch (SQLException e) {
            throw new DBException("ERRORE: impossibile trovare i documenti della sessione " + idSessione + "!", e);
        }

        return documenti;
    }

    /**
     * Recupera tutte le associazioni sessione-documento.
     *
     * @return Lista di tutte le associazioni
     * @throws DBException in caso di errori di database
     */
    @Override
    public List<SessioneDocumento> selectAll() throws DBException {

        List<SessioneDocumento> sessioneDocumenti = new ArrayList<>();

        String query = "SELECT * FROM sessionedocumento";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                sessioneDocumenti.add(getSessionDocument(rs));
            }

        } catch (SQLException e) {
            throw new DBException("ERRORE: impossibile caricare i documenti delle relative sessioni!", e);
        }

        return sessioneDocumenti;
    }

    /**
     * Inserisce una nuova associazione sessione-documento.
     *
     * @param sessioneDocumento L'associazione da inserire
     * @throws DBException in caso di errori di database
     */
    @Override
    public void insert(SessioneDocumento sessioneDocumento) throws DBException {

        String query = "INSERT INTO SESSIONEDOCUMENTO VALUES(?,?)";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {

            cmd.setLong(1, sessioneDocumento.getIdSessione());
            cmd.setString(2, sessioneDocumento.getNomeDocumento());

            int lines = cmd.executeUpdate();

            if (lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile inserire sessione-documento!", e);
        }
    }

    /**
     * Elimina un'associazione sessione-documento.
     *
     * @param sessioneDocumento L'associazione da eliminare
     * @throws DBException in caso di errori di database
     */
    @Override
    public void delete(SessioneDocumento sessioneDocumento) throws DBException {

        String query = "DELETE FROM SESSIONEDOCUMENTO WHERE sessione=? AND documento=?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {

            cmd.setLong(1, sessioneDocumento.getIdSessione());
            cmd.setString(2, sessioneDocumento.getNomeDocumento());

            int lines = cmd.executeUpdate();
            if (lines == 0)
                throw new DBException("ERRORE: nessuna riga modificata");

        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile cancellare sessione-documento!", e);
        }
    }

    /**
     * Elimina tutte le associazioni di una sessione.
     *
     * @param sessioneId ID della sessione
     * @throws DBException in caso di errori di database
     * @note Stampa il numero di associazioni eliminate
     */
    @Override
    public void deleteBySessioneId(long sessioneId) throws DBException {
        String query = "DELETE FROM SESSIONEDOCUMENTO WHERE sessione = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {

            cmd.setLong(1, sessioneId);

            int lines = cmd.executeUpdate();
            System.out.println("✅ SessioneDocumento eliminati: " + lines);

        } catch (SQLException e) {
            throw new DBException("Errore: impossibile eliminare sessione-documento della sessione " + sessioneId + "!", e);
        }
    }

    /**
     * Crea un oggetto Documento da un ResultSet.
     *
     * @param rs ResultSet contenente i dati
     * @return Documento creato
     * @throws SQLException in caso di errori SQL
     * @implNote Metodo helper per la costruzione di oggetti Documento
     */
    private Documento getDocument(ResultSet rs) throws SQLException {

        String titolo = rs.getString("nome");
        Lingua lingua = Lingua.valueOf(rs.getString("lingua"));
        Difficolta difficolta = Difficolta.valueOf(rs.getString("difficolta"));
        String path = rs.getString("percorso");

        return new Documento(titolo, lingua, difficolta, path);
    }

    /**
     * Crea un oggetto SessioneDocumento da un ResultSet.
     *
     * @param rs ResultSet contenente i dati
     * @return SessioneDocumento creato
     * @throws SQLException in caso di errori SQL
     * @implNote Metodo helper per la costruzione di oggetti SessioneDocumento
     */
    private SessioneDocumento getSessionDocument(ResultSet rs) throws SQLException {

        long idSessione = rs.getLong("sessione");
        String documento = rs.getString("documento");

        return new SessioneDocumento(idSessione, documento);
    }

}
