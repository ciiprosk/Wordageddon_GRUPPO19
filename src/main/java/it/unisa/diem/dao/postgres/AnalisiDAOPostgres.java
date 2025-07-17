/**
 * @file AnalisiDAOPostgres.java
 * @brief Implementazione PostgreSQL per la gestione delle analisi
 */

package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.AnalisiDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.utility.dbpool.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @class AnalisiDAOPostgres
 * @brief Implementazione concreta di AnalisiDAO per PostgreSQL
 *
 * Gestisce le operazioni CRUD per le analisi sul database PostgreSQL.
 * La classe sfrutta un connection pool per la gestione delle connessioni.
 */
public class AnalisiDAOPostgres implements AnalisiDAO {

    /**
     * @brief Costruttore di default
     */
    public AnalisiDAOPostgres() {}

    /**
     * @brief Ricerca un'analisi per titolo
     * @param titolo Il titolo dell'analisi da cercare
     * @return Optional contenente l'analisi se trovata
     * @throws DBException in caso di errori di database
     */
    @Override
    public Optional<Analisi> selectAnalisiByTitle(String titolo) throws DBException{
        Optional<Analisi> analisi = Optional.empty();
        //query: la query fa una select per nome analisi
        String query= "SELECT * FROM analisi where nome = ?";
        try(Connection conn=ConnectionManager.getConnection();
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

    /**
     * @brief Recupera tutte le analisi presenti
     * @return Lista di tutte le analisi
     * @throws DBException in caso di errori di database
     */
    @Override
    public List<Analisi> selectAll() throws DBException{
        List<Analisi> analisi=  new ArrayList<>();
        String query = "SELECT * FROM analisi";
        try(Connection con = ConnectionManager.getConnection();
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

    /**
     * @brief Inserisce una nuova analisi
     * @param analisi L'analisi da inserire
     * @throws DBException in caso di errori di database
     */

    @Override
    public void insert(Analisi analisi) throws DBException{
        //1. preparo query
        String query = "INSERT INTO analisi (nome, documento, percorso) VALUES (?, ?, ?)";
        try(Connection con = ConnectionManager.getConnection();
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

    /**
     * @brief Elimina un'analisi esistente
     * @param analisi L'analisi da eliminare
     * @throws DBException in caso di errori di database
     * @note Un trigger sul database elimina automaticamente i documenti associati
     */

    @Override
    public void delete(Analisi analisi) throws DBException {
        //nel db c'è un trigger che alla cancellazione di analisi cancella anche il documento
        String query = "DELETE FROM analisi WHERE nome = ?";
        try(Connection con= ConnectionManager.getConnection();
        PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, analisi.getTitolo());
            int lines= ps.executeUpdate();
            if(lines == 0)  throw new  DBException("Errore: nessuna riga cancellata");

        }catch(SQLException e){
            throw new DBException("Errore nel database, Impossibile cancellare analisi");
        }
    }

    /**
     * @brief Crea un oggetto Analisi da un ResultSet
     * @param rs Il ResultSet contenente i dati
     * @return L'oggetto Analisi creato
     * @throws SQLException in caso di errori SQL
     * @private Metodo helper per la costruzione di oggetti Analisi
     */
    private Analisi getAnalisi(ResultSet rs)throws SQLException{
        Analisi a=null;
        String documento=rs.getString("documento");
        String path=rs.getString("percorso");
        String split[]= path.split("[/.]");
        Difficolta difficolta= Difficolta.valueOf(split[split.length-3].toUpperCase());
        Lingua lingua= Lingua.valueOf(split[split.length-4].toUpperCase());
        a = new Analisi(new Documento(documento,lingua, difficolta), null); //non mi interessano le stopword
        return a;
    }


}
