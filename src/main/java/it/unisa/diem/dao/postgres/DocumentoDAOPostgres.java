package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.utility.dbpool.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementazione concreta di DocumentoDAO per PostgreSQL.
 *
 * Gestisce le operazioni CRUD relative ai documenti nel database PostgreSQL.
 * Utilizza un connection pool per la gestione delle connessioni.
 */
public class DocumentoDAOPostgres implements DocumentoDAO {

    /**
     * Costruttore di default.
     */
    public DocumentoDAOPostgres() {}

    /**
     * Cerca un documento per titolo.
     *
     * @param documento titolo del documento da cercare
     * @return Optional contenente il documento se trovato, altrimenti vuoto
     * @throws DBException in caso di errori di database
     */
    @Override
    public Optional<Documento> selectByTitle(String documento) throws DBException {
        Optional<Documento> result = Optional.empty();
        String query = "SELECT * FROM documento WHERE nome = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {
            cmd.setString(1, documento);
            ResultSet rs = cmd.executeQuery();
            Documento d = null;
            if (rs.next()) {
                d = getDocument(rs);
            }
            result = Optional.ofNullable(d);
        } catch (SQLException e) {
            throw new DBException("ERRORE: documento " + documento + " non trovato!", e);
        }
        return result;
    }

    /**
     * Recupera tutti i documenti.
     *
     * @return lista di tutti i documenti presenti nel database
     * @throws DBException in caso di errori di database
     */
    @Override
    public List<Documento> selectAll() throws DBException {
        List<Documento> documenti = new ArrayList<>();
        String query = "SELECT * FROM documento";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                documenti.add(getDocument(rs));
            }
        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile selezionare i documenti!", e);
        }
        return documenti;
    }

    /**
     * Recupera tutti i titoli dei documenti.
     *
     * @return lista di tutti i titoli dei documenti
     * @throws DBException in caso di errori di database
     */
    @Override
    public List<String> selectAllTitles() throws DBException {
        List<String> titoli = new ArrayList<>();
        String query = "SELECT nome FROM documento";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {
            ResultSet rs = cmd.executeQuery();
            while (rs.next()) {
                titoli.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile selezionare i titoli dei documenti!", e);
        }
        return titoli;
    }

    /**
     * Inserisce un nuovo documento nel database.
     *
     * @param documento documento da inserire
     * @throws DBException in caso di errori di database
     */
    @Override
    public void insert(Documento documento) throws DBException {
        String query = "INSERT INTO DOCUMENTO (nome, percorso, lingua, difficolta) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {
            setDocumentForInsert(cmd, documento);
            int lines = cmd.executeUpdate();
            if (lines == 0)
                throw new DBException("Errore: nessuna riga modificata");
        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile inserire il documento " + documento.getTitolo() + "!", e);
        }
    }

    /**
     * Elimina un documento dal database.
     *
     * @param documento documento da eliminare
     * @throws DBException in caso di errori di database
     */
    @Override
    public void delete(Documento documento) throws DBException {
        String query = "DELETE FROM DOCUMENTO WHERE nome = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement cmd = connection.prepareStatement(query)) {
            setDocumentForDelete(cmd, documento);
            int lines = cmd.executeUpdate();
            if (lines == 0)
                throw new DBException("ERRORE: nessuna riga modificata");
        } catch (SQLException e) {
            throw new DBException("Errore nel database, Impossibile cancellare documento", e);
        }
    }

    /**
     * Crea un oggetto Documento a partire dai dati di un ResultSet.
     *
     * @param rs ResultSet contenente i dati
     * @return documento creato
     * @throws SQLException in caso di errori SQL
     */
    private Documento getDocument(ResultSet rs) throws SQLException {
        String titolo = rs.getString("nome");
        Lingua lingua = Lingua.valueOf(rs.getString("lingua"));
        Difficolta difficolta = Difficolta.valueOf(rs.getString("difficolta"));
        String path = rs.getString("percorso");
        return new Documento(titolo, lingua, difficolta, path);
    }

    /**
     * Prepara lo statement per l'inserimento di un documento.
     *
     * @param cmd       PreparedStatement da configurare
     * @param documento documento da inserire
     * @throws SQLException in caso di errore SQL
     */
    private void setDocumentForInsert(PreparedStatement cmd, Documento documento) throws SQLException {
        cmd.setString(1, documento.getTitolo());
        cmd.setString(2, documento.getPath());
        cmd.setObject(3, documento.getLingua(), java.sql.Types.OTHER);
        cmd.setObject(4, documento.getDifficolta(), java.sql.Types.OTHER);
    }

    /**
     * Prepara lo statement per l'eliminazione di un documento.
     *
     * @param cmd       PreparedStatement da configurare
     * @param documento documento da eliminare
     * @throws SQLException in caso di errore SQL
     */
    private void setDocumentForDelete(PreparedStatement cmd, Documento documento) throws SQLException {
        cmd.setString(1, documento.getTitolo());
    }

}
