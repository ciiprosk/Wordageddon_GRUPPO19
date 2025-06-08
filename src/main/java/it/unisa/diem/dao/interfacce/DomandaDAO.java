package it.unisa.diem.dao.interfacce;

import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.dao.interfacce.DAO;

import java.util.Optional;

public interface DomandaDAO extends DAO<Domanda>    {

    Optional<Domanda> selectByTitle(String titolo);

}
