package store.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import store.dao.ProductDao;
import store.domain.Product;
import store.utils.JDBCUtils;

public class ProductDaoImpl implements ProductDao {

	public List<Product> findByNew() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from product where pflag = ? order by pdate desc limit ?" ;
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class), 0, 9) ;  // pflag 0表示上架
		return list;
	}

	public List<Product> findByHot() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from product where pflag = ? and is_hot = ? order by pdate desc limit ?" ;
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class), 0, 1, 9) ;  //1表示热门
		return list;
	}
	/**
	 * cid查到的分类商品的总记录数
	 */
	public Integer findCountByCid(String cid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select count(*) from product where cid = ? and pflag = ? " ;
		Long count = (Long) qr.query(sql, new ScalarHandler(),cid,0) ;
		return count.intValue();
	}
	/**
	 * cid查到的分类商品的当前页的商品集合
	 */
	public List<Product> findPageList(String cid, int begin, Integer pageSize) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from product where cid = ? and pflag = ? order by pdate desc limit ?,?" ;
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class),cid,0,begin,pageSize) ;
		return list;
	}
	/**
	 * 根据商品id查找具体商品详情
	 */
	public Product findByPid(String pid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from product where pid = ?" ;
		Product product = qr.query(sql, new BeanHandler<Product>(Product.class), pid) ;
		return product;
	}
	/**
	 * 管理员：商品管理
	 */
	public Integer findCount() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select count(*) from product where pflag = ?" ;
		Long count = (Long) qr.query(sql, new ScalarHandler(), 0) ; 
		return count.intValue();
	}
	/**
	 * 管理员：商品管理
	 */
	public List<Product> findPageList(int begin, Integer pageSize) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from product where pflag = ? order by pdate desc limit ?,?" ;
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class),0,begin,pageSize) ;
		return list;
	}
	/**
	 * 管理员：添加商品的保存
	 */
	public void save(Product product) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "insert into product values (?,?,?,?,?,?,?,?,?,?)";
		Object[] params = { product.getPid(), product.getPname(), product.getMarket_price(), product.getShop_price(),
				product.getPimage(), product.getPdate(), product.getIs_hot(), product.getPdesc(), product.getPflag(),
				product.getCategory().getCid() };
		queryRunner.update(sql, params);
	}
	/**
	 * 管理员：商品下架
	 */
	public void update(Product product) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "update product set pname = ?,market_price=?,shop_price=?,pimage=?,is_hot=?,pdesc= ?,pflag=? where pid = ?";
		Object[] params = { product.getPname(), product.getMarket_price(), product.getShop_price(),
				product.getPimage(),product.getIs_hot(), product.getPdesc(), product.getPflag(),product.getPid()
				 };
		qr.update(sql, params);
	}

	
	public Integer findPushDownCount() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select count(*) from product where pflag = ?" ;
		Long count = (Long) qr.query(sql, new ScalarHandler(), 1) ; 
		return count.intValue();
	}

	public List<Product> findPushDownPageList(int begin, Integer pageSize) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from product where pflag = ? order by pdate desc limit ?,?" ;
		List<Product> list = qr.query(sql, new BeanListHandler<Product>(Product.class),1,begin,pageSize) ;
		return list;
	}

}
