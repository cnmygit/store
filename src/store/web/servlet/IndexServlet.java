package store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import store.domain.Product;
import store.service.ProductService;
import store.service.impl.ProductServiceImpl;
import store.utils.BaseServlet;

/**
 * Servlet implementation class IndexServlet
 */
public class IndexServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	public String index(HttpServletRequest request, HttpServletResponse response) {
		try{
			//最新商品
			ProductService productService = new ProductServiceImpl() ;
			List<Product> newList = productService.findByNew() ;
			//热门商品
			List<Product> hotList = productService.findByHot() ;
			System.out.println(hotList);
			request.setAttribute("newList", newList);
			request.setAttribute("hotList", hotList);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
		
		return "/jsp/index.jsp" ;
	}

}
