package main.java.it.unisa.diem.dao.postgres;
import main.java.it.unisa.diem.dao.interfacce.DomandaDAO;
import main.java.it.unisa.diem.model.gestionedomande.Domanda;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DomandaDAOPostgres implements DomandaDAO {

    @Override
    public Optional<Domanda> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Domanda> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(Domanda domanda) {

    }

    @Override
    public void update(Domanda domanda) {

    }

    @Override
    public void delete(long id) {

    }

}
