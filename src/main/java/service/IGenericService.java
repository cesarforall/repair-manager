package service;

import java.util.List;

public interface IGenericService<T> {
	void save(T entity);
    void update(T entity);
    void delete(T entity);
    T findById(int id);
    List<T> findAll();
}
