package store.service;

import java.sql.SQLException;
import java.util.List;

import store.domain.Order;
import store.domain.OrderItem;
import store.domain.PageBean;

/**
 * 商品订单操作
 * @author Leo
 *
 */
public interface OrderService {

	void submit(Order order) ;

	PageBean<Order> findByUid(String uid, Integer currPage) throws Exception;

	Order findByOid(String oid) throws Exception;

	void updateOrder(Order order) throws SQLException;

	PageBean<Order> findByState(Integer currPage, Integer status) throws Exception;

	PageBean<Order> findAll(Integer currPage) throws Exception;

	List<OrderItem> showDetail(String oid) throws Exception;

}
