package store.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import store.dao.CategoryDao;
import store.domain.Category;
import store.utils.JDBCUtils;

public class CategoryDaoImpl implements CategoryDao {
	/**
	 * 分类查询所有
	 * @throws SQLException 
	 */
	public List<Category> findAll() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from category" ;
		List<Category> list = qr.query(sql, new BeanListHandler<Category>(Category.class)) ;
		return list;
	}
	/**
	 * 保存添加的分类
	 */
	public void save(Category category) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "insert into category values (?,?) " ;
		qr.update(sql,category.getCid(),category.getCname()) ;
	}
	/**
	 * cid找到对应分类
	 */
	public Category fandByCid(String cid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from category where cid = ?" ;
		return qr.query(sql, new BeanHandler<Category>(Category.class),cid);
	}
	/**
	 * 分类编辑；更新分类
	 */
	public void update(Category category) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "update category set cname = ? where cid = ?" ;
		qr.update(sql, category.getCname(), category.getCid()) ;
	}
	/**
	 * 根据cid删除对应分类
	 */
	public void delete(String cid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		
		//删除分类下面对应的商品（主外键的原因）
		String sql = "update product set cid = null where cid = ?" ;
		qr.update(sql,cid) ;
		
		sql = "delete from category where cid = ?" ;
		qr.update(sql,cid) ;
	}

}
