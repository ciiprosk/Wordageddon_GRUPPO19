package it.unisa.diem.dao.interfacce;

import it.unisa.diem.model.gestione.sessione.Sessione;

import java.util.Optional;

public interface SessioneDAO extends  DAO<Sessione>{



    Optional<Sessione> selectById(long id);

}
