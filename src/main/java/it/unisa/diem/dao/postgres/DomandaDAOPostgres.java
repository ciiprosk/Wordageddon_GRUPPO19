package it.unisa.diem.dao.postgres;
import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.dao.interfacce.DomandaDAO;
import it.unisa.diem.model.gestione.sessione.Domanda;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DomandaDAOPostgres implements DAO<Domanda> {

    @Override
    public Optional<Domanda> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Domanda> selectAll() {
        return Collections.emptyList();
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

}
