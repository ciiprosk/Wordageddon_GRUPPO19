package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    List<T> selectAll() throws SQLException, DBException;

    void insert(T t) throws SQLException, DBException;

    void update(T t) throws SQLException, DBException;

    void delete(T t) throws SQLException, DBException;

}
