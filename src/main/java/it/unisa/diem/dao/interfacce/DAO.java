package it.unisa.diem.dao.interfacce;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    List<T> selectAll();

    void insert(T t);

    void update(T t);

    void delete(T t);

}
