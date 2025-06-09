package it.unisa.diem.dao.postgres;
import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.dao.interfacce.DomandaDAO;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.sessione.Domanda;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DomandaDAOPostgres implements DomandaDAO {

    private final String url;
    private final String user;
    private final String pass;

    public DomandaDAOPostgres(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }


    public Optional<Domanda> selectByTitle(String titolo) throws SQLException, DBException {

        Optional<Domanda> result = Optional.empty();

        String query = "SELECT * FROM Domanda WHERE titolo = ?";

        Connection connection = DriverManager.getConnection(url, user, pass);

            PreparedStatement cmd = connection.prepareStatement (query);

            cmd.setString(1, titolo);

            ResultSet rs = cmd.executeQuery();

            Domanda domanda = null;

            if (rs.next()) {
                domanda = getDomanda(rs);
            }

            result = Optional.ofNullable(domanda);


        return result;

    }

    @Override
    public List<Domanda> selectAll() {

        List<Domanda> domande = new ArrayList<>();

        String query = "SELECT * FROM Domanda";



        return domande;
    }

    //questo metodo inserisce all'interno del db: nome del documento, posizione relativa del file, estensione, limgua
    @Override
    public void insert(Domanda domanda) {

    }

    @Override
    public void update(Domanda domanda) {

    }

    @Override
    public void delete(Domanda domanda) {

    }

    private Domanda getDomanda(ResultSet rs) throws SQLException, DBException {

        Domanda domanda = null;

        //da implementare

        return domanda;

    }

}
