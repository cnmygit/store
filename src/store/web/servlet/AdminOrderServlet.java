package store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.PageBean;
import store.service.OrderService;
import store.utils.BaseServlet;
import store.utils.BeanFactory;

/**
 * 管理员：订单管理
 */
public class AdminOrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    /**
     * 订单查询
     * @param req
     * @param resp
     * @return
     */
	public String findAll(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String state = req.getParameter("state") ;
			Integer currPage = Integer.parseInt(req.getParameter("currPage")) ;
			OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
			PageBean<Order> pageBean = null ;
			if(state == null) {
				//查询所有
				pageBean = orderService.findAll(currPage) ;
			}else {
				//查询某个订单状态
				Integer status = Integer.parseInt(state) ;
				pageBean = orderService.findByState(currPage,status) ;
			}
			req.setAttribute("pageBean", pageBean);
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
		return "/admin/order/list.jsp" ;
	}
	/**
	 * 订单管理：订单详情
	 * @param req
	 * @param resp
	 * @return
	 */
	public String showDetail(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String oid = req.getParameter("oid") ;
			
			OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
			List<OrderItem> list = orderService.showDetail(oid) ;
			
			JsonConfig config = new JsonConfig() ;
			config.setExcludes(new String[]{"order"});
			JSONArray jsonArray = JSONArray.fromObject(list,config) ;
			System.out.println(jsonArray.toString());
			resp.getWriter().println(jsonArray.toString());
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
		return null ;
	}
	/**
	 * 发货
	 */
	public String sendProduct(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String oid = req.getParameter("oid") ;
			OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
			Order order = orderService.findByOid(oid) ;
			order.setState(3); 
			orderService.updateOrder(order); 
			
			resp.getWriter().println(1) ;
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
		return null ;
	}
}
