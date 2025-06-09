package it.unisa.diem.dao.interfacce;

import java.util.List;
import java.util.Optional;

public interface ReadOnlyDAO<T> {

    List<T> selectAll();

    Optional<T> selectById(long id);

}
