package store.web.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import store.domain.Menu;
import store.service.MenuService;
import store.utils.BaseServlet;
import store.utils.BeanFactory;

/**
 * 管理员：文档树的加载（menu）
 */
public class MenuServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
      
	/**
	 * menu加载
	 */
	public String findMenu(HttpServletRequest req, HttpServletResponse resp) {
		try{
			MenuService menuService = (MenuService) BeanFactory.getBean("MenuService") ;
			List<Menu> list =  menuService.findMenu() ;
//			System.out.println(list);
			
			resp.setContentType("text/html;charset=UTF-8");
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
