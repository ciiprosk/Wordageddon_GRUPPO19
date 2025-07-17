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

public class DocumentoDAOPostgres implements DocumentoDAO {

    public DocumentoDAOPostgres() {}

    @Override
    public Optional<Documento> selectByTitle(String documento) throws DBException {

        Optional<Documento> result = Optional.empty();

        String query = "SELECT * FROM documento WHERE nome = ?";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd = connection.prepareStatement (query) ){

            cmd.setString(1, documento);
            ResultSet rs = cmd.executeQuery();

            Documento d = null;

            if (rs.next()) {
                d = getDocument(rs);
            }

            result = Optional.ofNullable(d);

        }  catch (SQLException e) {
            throw new DBException("ERRORE: documento " + documento + " non trovato!",e);
        }

        return result;

    }

    @Override
    public List<Documento> selectAll() throws DBException {

        List<Documento> documenti = new ArrayList<>();

        String query = "SELECT * FROM documento";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                documenti.add ( getDocument (rs) );
            }

        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile selezionare i documenti!", e);
        }

        return documenti;

    }

    @Override
    public List<String> selectAllTitles() throws DBException {

        List<String> titoli = new ArrayList<>();

        String query = "SELECT nome FROM documento";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

            ResultSet rs = cmd.executeQuery();

            while (rs.next()) {
                titoli.add ( rs.getString("nome") );
            }

        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile selezionare i titoli dei documenti!", e);
        }

        return titoli;

    }

    @Override
    public void insert(Documento documento) throws DBException {

        String query = "INSERT INTO DOCUMENTO (nome, percorso, lingua, difficolta) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getConnection();

             PreparedStatement cmd=connection.prepareStatement (query) ){

            setDocumentForInsert(cmd, documento);

            int lines = cmd.executeUpdate();

            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");

        } catch (SQLException e) {
            throw new DBException("ERRORE: Impossibile inserire il documento" + documento.getTitolo() + "!",e);
        }

    }

    @Override
    public void delete(Documento documento) throws DBException {

        String query = "DELETE FROM DOCUMENTO WHERE nome = ?";

        try(Connection connection = ConnectionManager.getConnection();

            PreparedStatement cmd = connection.prepareStatement(query)){

            setDocumentForDelete(cmd, documento);

            int lines= cmd.executeUpdate();
            if(lines == 0)
                throw new  DBException("ERRORE: nessuna riga modificata");

        }catch(SQLException e){

            throw new DBException("Errore nel database, Impossibile cancellare documento");

        }

    }

    private Documento getDocument(ResultSet rs) throws SQLException {

        Documento documento = null;

        String titolo = rs.getString("nome");
        Lingua lingua = Lingua.valueOf(rs.getString("lingua"));
        Difficolta difficolta = Difficolta.valueOf(rs.getString("difficolta"));
        String path = rs.getString("percorso");

        documento = new Documento(titolo, lingua, difficolta, path);

        return documento;

    }

    private void setDocumentForInsert(PreparedStatement cmd, Documento documento) throws SQLException {

        cmd.setString(1,documento.getTitolo());
        cmd.setString(2, documento.getPath());
        cmd.setObject(3,documento.getLingua(),java.sql.Types.OTHER);
        cmd.setObject(4,documento.getDifficolta(),java.sql.Types.OTHER);

    }

    private void setDocumentForDelete(PreparedStatement cmd, Documento documento) throws SQLException {

        cmd.setString(1,documento.getTitolo());

    }



}
