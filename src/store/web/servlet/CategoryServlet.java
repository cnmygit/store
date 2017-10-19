package store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import store.domain.Category;
import store.service.CategoryService;
import store.service.UserService;
import store.service.impl.CategoryServiceImpl;
import store.utils.BaseServlet;
import store.utils.BeanFactory;

/**
 * 商品分类
 */
public class CategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 查询所有商品分类
	 * @param req
	 * @param resp
	 * @return
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp) {
		try {
			//调用业务层
			CategoryService categoryService = (CategoryService) BeanFactory.getBean("CategoryService") ;
			List<Category> list = categoryService.findAll() ;
			//将list转换为json
			JSONArray jsonArray = JSONArray.fromObject(list) ;
//			System.out.println(jsonArray.toString());
			
			resp.getWriter().println(jsonArray.toString());
		}catch(Exception e) {
			e.printStackTrace(); 
			throw new RuntimeException() ;
		}
		return null ;
	}
}
