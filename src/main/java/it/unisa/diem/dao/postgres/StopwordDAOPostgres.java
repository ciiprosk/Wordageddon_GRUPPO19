package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.StopwordDAO;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StopwordDAOPostgres implements StopwordDAO {
    private final String url;
    private final String user;
    private final String pass;
    private final DocumentoDAOPostgres documentoDAO;

    /**
     * Costruttore della classe che recupera la stringa di connessione del db, user, pass.
     * @param url Parametro fondamentale per la connessione al database, è una stringa contenente il database e jdbc.
     * @param user Parametro per eseguire l'accesso al database, è l'utente.
     * @param pass Parametro per eseguire l'accesso al database, è la password.
     */
    public StopwordDAOPostgres(String url, String user, String pass) {
        this.url=url;
        this.user=user;
        this.pass=pass;
        documentoDAO = new DocumentoDAOPostgres();
    }

    public Optional<StopwordManager> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<StopwordManager> selectAll() {
        return Collections.emptyList();
    }

    //stopword ha id di documento--> come lo posso ricavare?al nome del documento immagio
    //faccio una query che mi restituisce l'id della query
    @Override
    public void insert(StopwordManager stopword) {
        //1. preparo la query
        //1.1. la query deve prima restituirmi l'id del documento che mi serve per cui ho bisogno di usare documento
        String query = "SELECT id FROM documento WHERE titolo = ?";
        //utilizzo una prepared statement
    }

    @Override
    public void update(StopwordManager stopword) {

    }

    @Override
    public void delete(StopwordManager stopword) {

    }

}
