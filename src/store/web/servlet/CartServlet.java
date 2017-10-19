package store.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Product;
import store.service.ProductService;
import store.utils.BaseServlet;
import store.utils.BeanFactory;

/**
 * 购物车
 */
public class CartServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * 购物车添加商品
	 */
	public String addCart(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String pid = req.getParameter("pid") ;
			Integer count = Integer.parseInt(req.getParameter("count")) ;
			//封装CartItem
			CartItem cartItem = new CartItem() ;
			cartItem.setCount(count);
			//调用业务层
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			Product product = productService.findByPid(pid) ;
			cartItem.setProduct(product);
			//调用cart
			Cart cart = getCart(req, resp) ;
			cart.addCartItem(cartItem);
			
			//页面跳转
			resp.sendRedirect(req.getContextPath() + "/jsp/cart.jsp");
			return null ;
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
	}
	/**
	 * 修改订单项数量，刷新整个页面
	 * @param req
	 * @param resp
	 * @return
	 */
	/*public String upCartItem(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String pid = req.getParameter("pid") ;
			Integer count = Integer.parseInt(req.getParameter("count")) ;
			System.out.println("pid" + pid);
			System.out.println("count" + count);
			//封装CartItem
			CartItem cartItem = new CartItem() ;
			cartItem.setCount(count);
			//调用业务层
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			Product product = productService.findByPid(pid) ;
			cartItem.setProduct(product);
			//调用cart
			Cart cart = getCart(req, resp) ;
			cart.updateCartItem(cartItem);
			
			//页面跳转
			resp.sendRedirect(req.getContextPath() + "/jsp/cart.jsp");
			return null ;
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
	}*/
	/**
	 * 修改订单项数量，异步刷新部分页面
	 */
	public String upCartItem(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//接收参数
			String pid = req.getParameter("pid") ;
			Integer count = Integer.parseInt(req.getParameter("count")) ;
//			System.out.println("pid" + pid);
//			System.out.println("count" + count);
			//封装CartItem
			CartItem cartItem = new CartItem() ;
			cartItem.setCount(count);
			//调用业务层
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			Product product = productService.findByPid(pid) ;
			cartItem.setProduct(product);
			//调用cart
			Cart cart = getCart(req, resp) ;
			cart.updateCartItem(cartItem);
			
			resp.setContentType("text/html;charset=UTF-8");
			JSONObject jsonObject = new JSONObject() ;
			jsonObject.accumulate("count", count) ;
			jsonObject.accumulate("total", cart.getTotal()) ;
			jsonObject.accumulate("subTotal", cart.getMap().get(pid).getSubtotal()) ;
			
			resp.getWriter().println(jsonObject.toString());
			//页面跳转
			return null ;
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
	}
	/**
	 * 获得session中存储的cart
	 */
	public Cart getCart(HttpServletRequest req, HttpServletResponse resp) {
		Cart cart = (Cart) req.getSession().getAttribute("cart") ;
		if(cart == null) {
			cart = new Cart() ;
			req.getSession().setAttribute("cart", cart);
		}
		return cart ;
	}
	
	/**
	 * 清空购物车
	 */
	public String clearCart(HttpServletRequest req, HttpServletResponse resp) {
		try{
			//从session中获得cart
			Cart cart = getCart(req, resp) ;
			cart.clearCart();
			//页面跳转
			resp.sendRedirect(req.getContextPath() + "/jsp/cart.jsp");
			return null ;
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
	
			
	}
	/**
	 * 根据pid删除购物车中某个商品
	 */
	public String clearCartItem(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String pid = req.getParameter("pid") ;
			//session中或cart
			Cart cart = getCart(req, resp) ;
			//调用方法移除对应商品
			cart.removeCartItem(pid);
			//页面跳转
			resp.sendRedirect(req.getContextPath() + "/jsp/cart.jsp");
			return null ;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
	}
}
