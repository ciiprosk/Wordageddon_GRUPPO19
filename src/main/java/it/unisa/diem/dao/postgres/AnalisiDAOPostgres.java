package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AnalisiDAOPostgres implements DAO<Analisi> {
    private String url;
    private String user;
    private String password;

    public AnalisiDAOPostgres(String url, String user, String password) {
        this.url = url;
        this.user=user;
        this.password=password;
    }


    public Optional<Analisi> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Analisi> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(Analisi analisi){
        // per inserire analisi ho bisogno di documento che trovo già in analisi--> GODO
        //1. preparo query
        String query = "INSERT INTO analisi (nome, documento, percorso) VALUES (?, ?, ?)";
        try(Connection con = DriverManager.getConnection(url,user, password);
            PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, analisi.getTitolo());
            ps.setString(2, analisi.getDocumento().getTitolo());
            ps.setString(3, analisi.getPathAnalisi());
            int lines = ps.executeUpdate();
            if(lines == 0 )
                throw new DBException("Errore: nessuna riga modificata");
        }catch(SQLException e){
            throw e;
        }

    }

    @Override
    public void update(Analisi analisi) {

    }

    @Override
    public void delete(Analisi analisi) {
        //preparo la query

    }
}
