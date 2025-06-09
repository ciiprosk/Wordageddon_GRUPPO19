package it.unisa.diem.dao.interfacce;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    List<T> selectAll() throws SQLException;

    void insert(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(T t) throws SQLException;

}
