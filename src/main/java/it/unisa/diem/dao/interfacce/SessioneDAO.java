package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.VoceStorico;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SessioneDAO extends  DAO<Sessione>{

    Optional<Sessione> selectByUser(String username) throws SQLException, DBException;
    Optional<Sessione> selectById(long id) throws SQLException, DBException;


    void delete(long sessioneId) throws DBException;

    //LEADERBOARD & HISTORY
    List<VoceStorico> selectByLastSessions(String username, Difficolta difficolta) throws DBException;

    List<VoceClassifica> selectByTopRanking(Difficolta difficolta) throws DBException;
}
