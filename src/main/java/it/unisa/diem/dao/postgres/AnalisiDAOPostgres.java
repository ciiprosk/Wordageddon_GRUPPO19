package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.AnalisiDAO;
import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AnalisiDAOPostgres implements AnalisiDAO {
    private String url;
    private String user;
    private String password;

    public AnalisiDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.password = pass;
    }

    @Override
    public Optional<Analisi> selectAnalisiByTitle(String titolo) throws DBException{
        Optional<Analisi> analisi = Optional.empty();
        //query: la query fa una select per nome analisi
        String query= "SELECT * FROM analisi where nome = ?";
        try(Connection conn=DriverManager.getConnection(url, user, password);
             PreparedStatement ps= conn.prepareStatement(query);){
             ps.setString(1, titolo);
             ResultSet rs= ps.executeQuery();

             Analisi a=null;
             if(rs.next()){
                 a= getAnalisi(rs);
             }
            analisi = Optional.ofNullable(a);

        }catch(SQLException e){
            throw new DBException("Errore nel database");
        }
        return analisi;
    }
/*
    @Override
    public void update(Analisi a, String oldTitolo) throws SQLException, DBException { //c'è un trigger che cambia anche documento e vale il viceversa
        String query = "UPDATE analisi SET nome = ? percorso = ? WHERE nome = ?";
        try(Connection con=DriverManager.getConnection(url, user, password);
        PreparedStatement ps=con.prepareStatement(query);){
            ps.setString(1, a.getTitolo());
            ps.setString(2, a.getPathAnalisi());
            ps.setString(3, oldTitolo);
            int lines=ps.executeUpdate();
            if(lines == 0) throw new DBException("Errore: nessuna riga modificata");

        }catch(SQLException e){
            throw new DBException("Errore nel databse, Impossibile inserire username",e);
        }
    }


 */
    @Override
    public List<Analisi> selectAll() throws DBException{
        List<Analisi> analisi=  new ArrayList<>();
        String query = "SELECT * FROM analisi";
        try(Connection con = DriverManager.getConnection(url, user, password);
            Statement st=con.createStatement();){
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                //qui devo popolare la listadi analisi, dev prima creare analisi
                analisi.add(getAnalisi(rs));
            }
        }catch(SQLException e){
            throw new DBException("Errore nel database, Impossibile selezionare analisi");
        }
        return analisi;
    }

    @Override
    public void insert(Analisi analisi) throws DBException{
        //1. preparo query
        String query = "INSERT INTO analisi (nome, documento, percorso) VALUES (?, ?, ?)";
        try(Connection con = DriverManager.getConnection(url,user, password);
            PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, analisi.getTitolo());
            ps.setString(2, analisi.getDocumento().getTitolo());
            ps.setString(3, analisi.getPathAnalisi());
            int lines = ps.executeUpdate();
            if(lines == 0)
                throw new DBException("Errore: nessuna riga modificata");
        }catch(SQLException e){
            throw new DBException("Errore: impossibile inserire analisi");
        }

    }

    @Override
    public void delete(Analisi analisi) throws DBException {
        //nel db c'è un trigger che alla cancellazione di analisi cancella anche il documento
        String query = "DELETE FROM analisi WHERE nome = ?";
        try(Connection con= DriverManager.getConnection(url, user, password);
        PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, analisi.getTitolo());
            int lines= ps.executeUpdate();
            if(lines == 0)  throw new  DBException("Errore: nessuna riga cancellata");

        }catch(SQLException e){
            throw new DBException("Errore nel database, Impossibile cancellare analisi");
        }
    }

    private Analisi getAnalisi(ResultSet rs)throws SQLException{
        Analisi a=null;
        String nome=rs.getString("nome");
        String documento=rs.getString("documento");
        String path=rs.getString("percorso");
        String split[]= path.split("[/.]");
        String nomeFile=split[split.length-2];
        Difficolta difficolta= Difficolta.valueOf(split[split.length-3]);
        Lingua lingua= Lingua.valueOf(split[split.length-4]);
        a = new Analisi(new Documento(documento,lingua, difficolta), null); //non mi interessano le stopword
        return a;
    }


}
