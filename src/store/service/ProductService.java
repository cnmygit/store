package store.service;

import java.sql.SQLException;
import java.util.List;

import store.domain.PageBean;
import store.domain.Product;

public interface ProductService {

	List<Product> findByNew() throws SQLException;

	List<Product> findByHot() throws SQLException;

	PageBean<Product> findPageByCid(String cid, Integer currPage) throws SQLException;

	Product findByPid(String pid) throws SQLException;

	PageBean<Product> findByPage(Integer currPage) throws SQLException;

	void save(Product product) throws SQLException;

	void update(Product product) throws SQLException;

	PageBean<Product> findByPushDown(Integer currPage) throws SQLException;

}
