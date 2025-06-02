package it.unisa.diem.dao.interfacce;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    Optional<T> selectById(long id);

    List<T> selectAll();

    void insert(T t);

    void update(T t);

    void delete(long id);

}
