package it.unisa.diem.dao.interfacce;

import it.unisa.diem.model.gestione.sessione.StoricoSessione;

import java.util.List;

public interface StoricoSessioneDAO extends ReadOnlyDAO {

    List<StoricoSessione> selectByUser(String username);



}
