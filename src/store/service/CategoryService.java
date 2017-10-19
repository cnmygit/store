package store.service;

import java.sql.SQLException;
import java.util.List;

import store.domain.Category;

public interface CategoryService {

	List<Category> findAll() throws SQLException;

	void save(Category category) throws SQLException;

	Category fandByCid(String cid) throws SQLException;

	void update(Category category) throws SQLException;

	void delete(String cid) throws SQLException;

}
