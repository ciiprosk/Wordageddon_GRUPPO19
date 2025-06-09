package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.model.gestione.analisi.Documento;
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
    public Optional<Documento> selectByName(String documento) throws SQLException {

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

    private Utente getDocument(ResultSet rs) throws SQLException {

        Documento documento = null;

        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String password = rs.getString("password");
        byte[] salt = rs.getBytes("salt");
        Ruolo ruolo = Ruolo.valueOf(rs.getString("ruolo"));

        utente = new Utente(username, email, password, salt, ruolo);

        return utente;

    }

}
