package it.unisa.diem.dao.postgres;

import it.unisa.diem.dao.interfacce.AnalisiDAO;
import it.unisa.diem.dao.interfacce.DAO;
import it.unisa.diem.model.gestione.analisi.Analisi;

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

    @Override
    public Optional<Analisi> selectById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Analisi> selectAll() {
        return Collections.emptyList();
    }

    @Override
    public void insert(Analisi analisi) {

    }

    @Override
    public void update(Analisi analisi) {

    }

    @Override
    public void delete(Analisi analisi) {

    }
}
