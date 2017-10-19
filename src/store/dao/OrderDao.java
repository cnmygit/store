package store.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import store.domain.Order;
import store.domain.OrderItem;

/**
 * 商品订单操作
 * @author Leo
 *
 */
public interface OrderDao {

	void submitOrder(Connection conn, Order order) throws SQLException;

	void submitOrderItem(Connection conn, OrderItem orderItem) throws SQLException;

	Integer findCountByUid(String uid) throws SQLException;

	List<Order> findPageByUid(String uid, int begin, Integer pageSize) throws Exception;

	Order findByOid(String oid) throws Exception;

	void updateOrder(Order order) throws SQLException;

	Integer findCount() throws Exception;

	List<Order> findPage(int begin, Integer pageSize) throws Exception;

	Integer findCountByState(Integer status) throws Exception;

	List<Order> findPageByState(int begin, Integer pageSize, Integer status) throws Exception;

	List<OrderItem> showDetail(String oid) throws Exception;

}
