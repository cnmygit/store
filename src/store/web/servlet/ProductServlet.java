package store.web.servlet;

import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import store.domain.PageBean;
import store.domain.Product;
import store.service.ProductService;
import store.service.impl.ProductServiceImpl;
import store.utils.BaseServlet;
import store.utils.BeanFactory;
import store.utils.CookieUtils;

/**
 * 商城商品
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    
	/**
	 * 商品更具cid不同分类显示的页面
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findByCid(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String cid = req.getParameter("cid") ;
			Integer currPage = Integer.parseInt(req.getParameter("currPage")) ;
			
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			PageBean<Product> pageBean = productService.findPageByCid(cid,currPage) ;
//			System.out.println("测试" + pageBean);
			req.setAttribute("PageBean", pageBean);
			return "/jsp/product_list.jsp" ;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
	}
	/**
	 * 根据商品id查找具体商品详情
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findByPid(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String pid = req.getParameter("pid") ;
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			Product product = productService.findByPid(pid) ;
			
			//根据pid统计游览记录
			Cookie[] cookies = req.getCookies() ;
			Cookie cookie = CookieUtils.findCookie(cookies, "history") ;
			LinkedList<String> list = null ;
			if(cookie == null) {
				//没有游览记录
				cookie = new Cookie("history", pid) ;
				cookie.setPath("store_v2.0");
				cookie.setMaxAge(7 * 24 * 60 * 60);
			}else {
				//有游览记录
				String value = cookie.getValue() ;
				String[] ids = value.split("-") ;
				list = new LinkedList<String>(Arrays.asList(ids)) ;
				if(list.contains(pid)) {
					//游览过
					list.remove(pid) ;
				}else {
					//没有游览过
					if(list.size() > 6){
						list.removeLast() ;
					}
				}
				list.addFirst(pid);
				
				StringBuffer sb = new StringBuffer() ;
				for(String id : list) {
					sb.append(id).append("-") ;
				}
				String visitID = sb.toString().substring(0, sb.length() - 1 ) ;
				cookie = new Cookie("history", visitID) ;
			}
			resp.addCookie(cookie);
			
			req.setAttribute("product", product);
			return "/jsp/product_info.jsp" ;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
	}
}
