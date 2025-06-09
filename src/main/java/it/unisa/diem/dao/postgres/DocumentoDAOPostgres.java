package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Ruolo;
import it.unisa.diem.model.gestione.utenti.Utente;

import java.sql.*;
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
    public Optional<Documento> selectByTitle(String documento) throws SQLException {

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


    public int geDocumentoId(Documento documento)throws Exception{

        String idQuery ="SELECT id FROM DOCMENTO WHERE nome= ?";
        int id=-1;
        try(Connection con = DriverManager.getConnection(url,user, pass);
            PreparedStatement ps= con.prepareStatement(idQuery);){
            ps.setString(1, documento.getTitolo()); // adesso posso cercare l'id del documento
            ResultSet ritorno = ps.executeQuery();
            while(ritorno.next()){
                id=ritorno.getInt("id");
            }

        }
        return id;
    }
    @Override
    public List<Documento> selectAll() {
        return Collections.emptyList();
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

    private Documento getDocument(ResultSet rs) throws SQLException {

        Documento documento = null;

        String titolo = rs.getString("nome");
        Lingua lingua = Lingua.valueOf(rs.getString("lingua"));
        Difficolta difficolta = Difficolta.valueOf(rs.getString("difficolta"));
        String path = rs.getString("percorso");


        documento = new Documento(titolo, lingua, difficolta, path);

        return documento;

    }

}
