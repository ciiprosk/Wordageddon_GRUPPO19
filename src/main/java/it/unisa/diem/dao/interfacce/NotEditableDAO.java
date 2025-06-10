package it.unisa.diem.dao.interfacce;

import it.unisa.diem.exceptions.DBException;

import java.util.List;

public interface NotEditableDAO<T> {

    List<T> selectAll() throws DBException;

    void insert(T t) throws DBException;

    void delete(T t) throws DBException;
}
