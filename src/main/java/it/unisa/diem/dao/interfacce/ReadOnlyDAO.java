package it.unisa.diem.dao.interfacce;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ReadOnlyDAO<T> {

    List<T> selectAll() throws SQLException;

    Optional<T> selectById(long id) throws SQLException;

}
