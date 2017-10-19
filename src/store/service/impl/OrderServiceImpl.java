package store.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import store.dao.OrderDao;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.PageBean;
import store.service.OrderService;
import store.utils.BeanFactory;
import store.utils.JDBCUtils;

/**
 * 商品订单操作
 * @author Leo
 *
 */
public class OrderServiceImpl implements OrderService {
	/**
	 * 提交订单
	 */
	public void submit(Order order) {
		Connection conn = null ;
		try{
			conn = JDBCUtils.getConn() ;
			conn.setAutoCommit(false);
			
			//调用业务层
			OrderDao orderDao = (OrderDao) BeanFactory.getBean("OrderDao") ;
			orderDao.submitOrder(conn,order) ;
			for(OrderItem orderItem : order.getList()) {
				orderDao.submitOrderItem(conn,orderItem) ;
			}
			DbUtils.commitAndCloseQuietly(conn);
		}catch(Exception e) {
			e.printStackTrace();
			DbUtils.rollbackAndCloseQuietly(conn);
		}
	}
	/**
	 * 根据UID查找我的订单
	 */
	public PageBean<Order> findByUid(String uid, Integer currPage) throws Exception {
		PageBean<Order> pageBean = new PageBean<Order>() ;
		//当前页
		pageBean.setCurrPage(currPage);
		//每页显示订单记录数
		Integer pageSize = 5 ;
		pageBean.setPageSize(pageSize) ;
		//总订单记录数
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("OrderDao") ;
		Integer totalCount = orderDao.findCountByUid(uid) ;
		//总订单页数
		double tc = totalCount ;
		Double totalPage = Math.ceil(tc / pageSize) ;
		pageBean.setTotalPage(totalPage.intValue());
		//设置每页显示订单记录的集合
		int begin = (currPage - 1) * pageSize ;
		List<Order> list = orderDao.findPageByUid(uid,begin,pageSize) ;
		pageBean.setList(list);
		return pageBean;
	}
	/**
	 * 我的订单页面根据oid去结算页面
	 * @throws Exception 
	 */
	public Order findByOid(String oid) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("OrderDao") ;
		return orderDao.findByOid(oid);
	}
	/**
	 * 订单结算
	 */
	public void updateOrder(Order order) throws SQLException {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("OrderDao") ;
		orderDao.updateOrder(order);
	}
	/**
	 * 根据订单状态查询所有订单
	 */
	public PageBean<Order> findByState(Integer currPage, Integer status) throws Exception {
		PageBean<Order> pageBean = new PageBean<Order>() ;
		//当前页
		pageBean.setCurrPage(currPage);
		//每页显示的记录数
		Integer pageSize = 10 ;
		pageBean.setPageSize(pageSize);
		// 总记录数
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("OrderDao") ;
		Integer totalCount = orderDao.findCountByState(status) ;
		pageBean.setTotalCount(totalCount);
		//总页数
		double tc = totalCount ;
		Double totalPage = Math.ceil(tc / pageSize) ;
		pageBean.setTotalPage(totalPage.intValue());
		//设置每页显示订单记录的集合
		int begin = (currPage - 1) * pageSize ;
		List<Order> list = orderDao.findPageByState(begin, pageSize, status) ;
		pageBean.setList(list);
		return pageBean;
	}
	/**
	 * 查询所有订单
	 */
	public PageBean<Order> findAll(Integer currPage) throws Exception {
		PageBean<Order> pageBean = new PageBean<Order>() ;
		//当前页
		pageBean.setCurrPage(currPage);
		//每页显示的记录数
		Integer pageSize = 10 ;
		pageBean.setPageSize(pageSize);
		// 总记录数
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("OrderDao") ;
		Integer totalCount = orderDao.findCount() ;
		pageBean.setTotalCount(totalCount);
		//总页数
		double tc = totalCount ;
		Double totalPage = Math.ceil(tc / pageSize) ;
		pageBean.setTotalPage(totalPage.intValue());
		//设置每页显示订单记录的集合
		int begin = (currPage - 1) * pageSize ;
		List<Order> list = orderDao.findPage(begin,pageSize) ;
		pageBean.setList(list);
		return pageBean;
	}
	/**
	 * 订单管理：订单详情
	 */
	public List<OrderItem> showDetail(String oid) throws Exception {
		OrderDao orderDao = (OrderDao) BeanFactory.getBean("OrderDao") ;
		return orderDao.showDetail(oid);
	}

}
