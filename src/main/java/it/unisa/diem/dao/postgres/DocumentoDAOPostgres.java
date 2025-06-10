package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Ruolo;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DocumentoDAOPostgres implements DocumentoDAO {
    private final String url;
    private final String user;
    private final String pass;

    public DocumentoDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public Optional<Documento> selectByTitle(String documento) throws DBException {

        Optional<Documento> result = Optional.empty();

        String query = "SELECT * FROM documento WHERE nome = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

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

        try (Connection connection = DriverManager.getConnection(url, user, pass);

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
    public List<String> selectTitlesByLangAndDif(Lingua lingua, Difficolta difficolta) throws DBException {
        List<String> titoli = new ArrayList<>();

        String query = "SELECT nome FROM documento WHERE lingua = ? AND difficolta = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);

             PreparedStatement cmd=connection.prepareStatement (query) ){

            cmd.setObject(1, lingua.name(), java.sql.Types.OTHER);
            cmd.setObject(2, difficolta.name(), java.sql.Types.OTHER);

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
    public void insert(Documento documento) {

    }

    @Override
    public void update(Documento documento) {

    }

    @Override
    public void delete(Documento documento) {

    }

    private Documento getDocument(ResultSet rs) throws SQLException, DBException {

        Documento documento = null;

        String titolo = rs.getString("nome");
        Lingua lingua = Lingua.valueOf(rs.getString("lingua"));
        Difficolta difficolta = Difficolta.valueOf(rs.getString("difficolta"));
        String path = rs.getString("percorso");


        documento = new Documento(titolo, lingua, difficolta, path);

        return documento;

    }

}
