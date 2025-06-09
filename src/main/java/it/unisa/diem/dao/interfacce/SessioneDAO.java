package it.unisa.diem.dao.interfacce;

import it.unisa.diem.model.gestione.sessione.Sessione;

import java.util.List;
import java.util.Optional;

public interface SessioneDAO extends  DAO<Sessione>{

    Optional<Sessione> selectByUser(String username);

    Optional<Sessione> selectById(long id);

}
