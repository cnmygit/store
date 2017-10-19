package store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import store.domain.Category;
import store.domain.PageBean;
import store.domain.Product;
import store.service.CategoryService;
import store.service.ProductService;
import store.utils.BaseServlet;
import store.utils.BeanFactory;

/**
 * 管理员：商品管理
 */
public class AdminProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    /**
     * 商品管理的分页显示
     * @param req
     * @param resp
     * @return
     */
	public String findByPage(HttpServletRequest req, HttpServletResponse resp) {
		try{
			Integer currPage = Integer.parseInt(req.getParameter("currPage")) ;
			
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			PageBean<Product> pageBean = productService.findByPage(currPage) ;

			req.setAttribute("pageBean", pageBean);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
		return "/admin/product/list.jsp" ;
	}
	
	/**
	 * 跳转的商品添加页面的方法
	 */
	public String saveUI(HttpServletRequest req, HttpServletResponse resp) {
		try{
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("CategoryService") ;
			List<Category> category = categoryService.findAll() ;
			req.setAttribute("category", category);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
		return "/admin/product/add.jsp" ;
	}
	/**
	 * 商品下架
	 */
	public String pushDown(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String pid = req.getParameter("pid") ;
			
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			Product product = productService.findByPid(pid) ;
			product.setPflag(1); //1 表示下架
			
			productService.update(product) ;
			
			resp.sendRedirect(req.getContextPath() + "/AdminProductServlet?method=findByPage&currPage=1");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
		return null ;
	}
	/**
	 * 找到下架商品：调到商品上架页面
	 */
	public String findByPushDown(HttpServletRequest req, HttpServletResponse resp) {
		try{
			Integer currPage = Integer.parseInt(req.getParameter("currPage")) ;
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			
			PageBean<Product> pageBean = productService.findByPushDown(currPage) ;
			
			req.setAttribute("pageBean", pageBean);
			
			return "/admin/product/pushDown_list.jsp" ;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
	}
	/**
	 * 商品上架
	 */
	public String pushUp(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String pid = req.getParameter("pid") ;
			
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			Product product = productService.findByPid(pid) ;
			product.setPflag(0); //1 表示下架
			
			productService.update(product) ;
			
			resp.sendRedirect(req.getContextPath() + "/AdminProductServlet?method=findByPage&currPage=1");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
		return null ;
	}
}
