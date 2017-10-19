package store.web.servlet;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.PageBean;
import store.domain.User;
import store.service.OrderService;
import store.utils.BaseServlet;
import store.utils.BeanFactory;
import store.utils.PaymentUtils;
import store.utils.UUIDUtils;

/**
 * 商品订单的servlet
 */
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
     
	/**
	 * 商品提交订单
	 * @param req
	 * @param resp
	 * @return
	 * private String address ;
	private String name ;
	private String telephone ;
	private User user ;
	private List<OrderItem> list ;
	 */
	public String submitOrder(HttpServletRequest req, HttpServletResponse resp) {
		//封装数据
		Order order = new Order() ;
		order.setOid(UUIDUtils.getUUID());
		order.setOrderTime(new Date());
		order.setState(1); //未付款
		//商品总价-session中取得
		Cart cart = (Cart) req.getSession().getAttribute("cart") ;
		if(cart == null) {
			req.setAttribute("msg", "您购物车中没有商品哟");
			return "/jsp/msg.jsp" ;
		}
		order.setTotal(cart.getTotal());
		//用户-session中取得
		User user = (User) req.getSession().getAttribute("existUser") ;
		if(user == null) {
			req.setAttribute("msg", "您还没有登录");
			return "/jsp/login.jsp" ;
		}
		order.setUser(user);
		//订单项
		for(CartItem cartItem : cart.getMap().values()){
			OrderItem orderItem = new OrderItem() ;
			orderItem.setItemId(UUIDUtils.getUUID());
			orderItem.setCount(cartItem.getCount());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			order.getList().add(orderItem) ;
		}
		
		//调用业务层完成封装
		OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
		
		orderService.submit(order) ;
		
		
		//清空购物侧
		cart.clearCart(); 
		
		//页面跳转
		req.setAttribute("order", order);
		return "/jsp/order_info.jsp" ;
	}
	
	/**
	 * 根据uid查找我的订单
	 */
	public String findByUid(HttpServletRequest req, HttpServletResponse resp) {
		try {
			//接收数据
			Integer currPage = Integer.parseInt(req.getParameter("currPage")) ;
			//获得用户信息
			User existUser = (User) req.getSession().getAttribute("existUser") ;
			//调用业务层
			OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
			PageBean<Order> pageBean = orderService.findByUid(existUser.getUid(),currPage) ;
//			System.out.println("订单测试" + pageBean);
			req.setAttribute("pageBean", pageBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//页面跳转
		return "/jsp/order_list.jsp" ;
	}
	
	/**
	 * 我的订单页面根据oid去结算页面
	 */
	public String findByOid(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String oid = req.getParameter("oid") ;
			
			OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
			Order order = orderService.findByOid(oid) ;
			
			req.setAttribute("order", order);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
		return "/jsp/order_info.jsp" ;
	}
	
	/**
	 * 订单支付
	 */
	public String payOrder(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String oid = req.getParameter("oid") ;
			String address = req.getParameter("address") ;
			String name = req.getParameter("name") ;
			String telephone = req.getParameter("telephone") ;
			String pd_FrpId = req.getParameter("pd_FrpId") ;
			
			//调用业务层，封装完整的order
			OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
			Order order = orderService.findByOid(oid) ;
			order.setAddress(address);
			order.setName(name);
			order.setTelephone(telephone);
			
			//更新数据库order数据
			orderService.updateOrder(order) ;
			
			//付款跳转到网银页面
			//设置参数
			String p0_Cmd = "Buy";
			String p1_MerId = "10001126856";
			String p2_Order = oid;
			String p3_Amt = "0.01";
			String p4_Cur = "CNY";
			String p5_Pid = "";
			String p6_Pcat = "";
			String p7_Pdesc = "";
			String p8_Url = "http://localhost:8080/store_v2.0/OrderServlet?method=callBack";
			String p9_SAF = "";
			String pa_MP = "";
			String pr_NeedResponse = "1"; //应答机制
			String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
			//签名数据
			String hmac = PaymentUtils.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue) ;
			
			//get方式提交（不够安全）
			StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?") ;
			sb.append("p0_Cmd=").append(p0_Cmd).append("&") ;
			sb.append("p1_MerId=").append(p1_MerId).append("&");
			sb.append("p2_Order=").append(p2_Order).append("&");
			sb.append("p3_Amt=").append(p3_Amt).append("&");
			sb.append("p4_Cur=").append(p4_Cur).append("&");
			sb.append("p5_Pid=").append(p5_Pid).append("&");
			sb.append("p6_Pcat=").append(p6_Pcat).append("&");
			sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
			sb.append("p8_Url=").append(p8_Url).append("&");
			sb.append("p9_SAF=").append(p9_SAF).append("&");
			sb.append("pa_MP=").append(pa_MP).append("&");
			//支付通道编码
			sb.append("pd_FrpId=").append(pd_FrpId).append("&");
			sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
			sb.append("hmac=").append(hmac);
			
			//get方式提交
			req.setAttribute("p0_Cmd", p0_Cmd);
			req.setAttribute("p1_MerId", p1_MerId);
			req.setAttribute("p2_Order", p2_Order);
			req.setAttribute("p3_Amt", p3_Amt);
			req.setAttribute("p4_Cur", p4_Cur);
			req.setAttribute("p5_Pid", p5_Pid);
			req.setAttribute("p6_Pcat", p6_Pcat);
			req.setAttribute("p7_Pdesc", p7_Pdesc);
			req.setAttribute("p8_Url", p8_Url);
			req.setAttribute("p9_SAF", p9_SAF);
			req.setAttribute("pa_MP", pa_MP);
			req.setAttribute("pd_FrpId", pd_FrpId);
			req.setAttribute("pr_NeedResponse", pr_NeedResponse);
			req.setAttribute("hmac", hmac);
			
//			resp.sendRedirect(sb.toString());
			return "/jsp/confirm.jsp" ;
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
		//页面跳转
	}
	public String callBack(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String oid = req.getParameter("r6_Order") ;
			String money = req.getParameter("r3_Amt") ;
			
			//修改订单状态
			OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
			Order order = orderService.findByOid(oid) ;
			order.setState(2);
			orderService.updateOrder(order);
			
			req.setAttribute("msg", "您的订单："+oid+"支付成功,支付金额为："+money);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/jsp/msg.jsp" ;
	}
	/**
	 * 确认收货
	 */
	public String confirmOrder(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String oid = req.getParameter("oid") ;
			OrderService orderService = (OrderService) BeanFactory.getBean("OrderService") ;
			Order order = orderService.findByOid(oid) ;
			order.setState(4); 
			orderService.updateOrder(order); 
			
			resp.sendRedirect(req.getContextPath() + "/OrderServlet?method=findByUid&currPage=1");
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
		return null ;
	}
}
