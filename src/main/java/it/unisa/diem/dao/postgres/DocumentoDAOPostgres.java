package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.model.gestione.analisi.Documento;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DocumentoDAOPostgres implements DAO<Documento> {
    private final String url;
    private final String user;
    private final String pass;

    public DocumentoDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }


    public Optional<Documento> selectById(long id) {
        return Optional.empty();
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

}
