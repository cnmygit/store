package store.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import store.dao.OrderDao;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.Product;
import store.utils.JDBCUtils;

/**
 * 商品订单操作
 * 
 * @author Leo
 *
 */
public class OrderDaoImpl implements OrderDao {

	public void submitOrder(Connection conn, Order order) throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orders values (?,?,?,?,?,?,?,?) ";
		Object[] params = { order.getOid(), order.getOrderTime(), order.getTotal(), order.getState(), order.getAddress(),
				order.getName(), order.getTelephone(), order.getUser().getUid() };
		qr.update(conn, sql, params) ;
	}

	public void submitOrderItem(Connection conn, OrderItem orderItem) throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orderitem values (?,?,?,?,?) " ;
		Object[] params = {orderItem.getItemId(),orderItem.getCount(),orderItem.getSubtotal(),orderItem.getProduct().getPid(),orderItem.getOrder().getOid()} ;
		qr.update(conn, sql, params) ;
	}
	/**
	 * 商品订单：根据uid查找总记录数
	 */
	public Integer findCountByUid(String uid) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select count(*) from orders where uid = ?" ;
		Long count = (Long) qr.query(sql, new ScalarHandler(), uid) ;
		return count.intValue();
	}
	/**
	 * 商品订单：根据uid查找每页显示的集合（pageBean中)
	 */
	public List<Order> findPageByUid(String uid, int begin, Integer pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where uid = ? order by ordertime desc limit ?,? " ;
		List<Order> list = qr.query(sql, new BeanListHandler<Order>(Order.class),uid, begin, pageSize) ;  //此时查找的order是不全的
		for(Order order : list) {
			sql = "select * from orderitem o, product p where o.pid = p.pid and oid = ?" ;
			List<Map<String,Object>> olist = qr.query(sql, new MapListHandler(), order.getOid()) ;
			for(Map<String,Object> map : olist) {
				Product product = new Product() ;
				BeanUtils.populate(product, map);
				
				OrderItem orderItem = new OrderItem() ;
				BeanUtils.populate(orderItem, map); 
				orderItem.setProduct(product);
				
				order.getList().add(orderItem) ;
			}
		}
		return list;
	}
	/**
	 * 我的订单页面根据oid去结算页面
	 * @throws Exception 
	 */
	public Order findByOid(String oid) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where oid = ?" ;
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid) ;
		
		sql = "select * from orderitem o, product p where o.pid = p.pid and oid = ?" ;
		List<Map<String,Object>> olist = qr.query(sql, new MapListHandler(),oid) ;
			for(Map<String,Object> map : olist) {
			Product product = new Product() ;
			BeanUtils.populate(product, map);
			
			OrderItem orderItem = new OrderItem() ;
			BeanUtils.populate(orderItem, map);
			orderItem.setProduct(product);
			
			order.getList().add(orderItem) ;
		}
		return order;
	}
	/**
	 * 订单结算
	 */
	public void updateOrder(Order order) throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update orders set total = ?, state = ?, address = ?, name = ?, telephone = ? where oid = ?" ;
		Object[] params = {order.getTotal(), order.getState(), order.getAddress(), order.getName(), order.getTelephone(), order.getOid()} ;
		qr.update(sql, params) ;
	}
	/**
	 * 管理员：查询所有订单：订单数
	 */
	public Integer findCount() throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select count(*) from orders" ;
		Long count = (Long) qr.query(sql, new ScalarHandler()) ;
		return count.intValue();
	}
	/**
	 * 管理员：查询所有订单：订单分页
	 */
	public List<Order> findPage(int begin, Integer pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders order by ordertime desc limit ?,? " ;
		List<Order> list = qr.query(sql, new BeanListHandler<Order>(Order.class), begin, pageSize) ;  //此时查找的order是不全的
		for(Order order : list) {
			sql = "select * from orderitem o, product p where o.pid = p.pid and oid = ?" ;
			List<Map<String,Object>> olist = qr.query(sql, new MapListHandler(), order.getOid()) ;
			for(Map<String,Object> map : olist) {
				Product product = new Product() ;
				BeanUtils.populate(product, map);
				
				OrderItem orderItem = new OrderItem() ;
				BeanUtils.populate(orderItem, map); 
				orderItem.setProduct(product);
				
				order.getList().add(orderItem) ;
			}
		}
		return list;
	}
	/**
	 * 管理员：根据订单状态查询所有订单：订单数
	 */
	public Integer findCountByState(Integer status) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select count(*) from orders where state = ?" ;
		Long count = (Long) qr.query(sql, new ScalarHandler(),status) ;
		return count.intValue();
	}
	/**
	 * 管理员：根据订单状态查询所有订单：订单分页
	 */
	public List<Order> findPageByState(int begin, Integer pageSize, Integer status) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from orders where state = ? order by ordertime desc limit ?,?" ;
		List<Order> list = qr.query(sql, new BeanListHandler<Order>(Order.class), status, begin, pageSize) ;
		for(Order order : list) {
			sql = "select * from orderitem o, product p where o.pid = p.pid and oid = ?" ;
			List<Map<String,Object>> olist = qr.query(sql, new MapListHandler(), order.getOid()) ;
			for(Map<String,Object> map : olist) {
				Product product = new Product() ;
				BeanUtils.populate(product, map); 
				
				OrderItem orderItem = new OrderItem() ;
				BeanUtils.populate(orderItem, map);
				orderItem.setProduct(product);
				
				order.getList().add(orderItem) ;
			}
		}
		return list;
	}
	/**
	 * 订单管理：订单详情
	 */
	public List<OrderItem> showDetail(String oid) throws Exception {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from orderitem o, product p where o.pid = p.pid and oid = ?" ;
		List<OrderItem> list = new ArrayList<OrderItem>() ;
		List<Map<String,Object>> olist = qr.query(sql, new MapListHandler(), oid) ;
		for(Map<String,Object> map : olist) {
			Product product = new Product() ;
			BeanUtils.populate(product, map);
			
			OrderItem orderItem = new OrderItem() ;
			BeanUtils.populate(orderItem, map);
			orderItem.setProduct(product);
			
			list.add(orderItem) ;
		}
		return list;
	}
	
}
