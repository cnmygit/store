package store.web.servlet;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import net.sf.json.JSONObject;
import store.domain.Category;
import store.service.CategoryService;
import store.utils.BaseServlet;
import store.utils.BeanFactory;
import store.utils.UUIDUtils;

/**
 * 管理员：分类管理
 */
public class AdminCategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    /**
     * 找到category的分类
     * @param req
     * @param resp
     * @return
     */
    public String findAll(HttpServletRequest req, HttpServletResponse resp) {
    	try{
    		CategoryService categoryService = (CategoryService) BeanFactory.getBean("CategoryService") ;
    		List<Category> list = categoryService.findAll() ;
    		
    		req.setAttribute("list", list);
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw new RuntimeException() ;
    	}
    	return "/admin/category/list.jsp";
    }
    /**
     * 跳转到添加分类的页面
     */
    public String saveUI(HttpServletRequest req, HttpServletResponse resp) {
    	return "/admin/category/add.jsp" ;
    }
    /**
     * 添加分类的保存
     * @param req
     * @param resp
     * @return
     */
    public String save(HttpServletRequest req, HttpServletResponse resp) { 
    	try{
    		String cname = req.getParameter("cname") ;
    		
    		Category category = new Category() ;
    		category.setCname(cname);
    		category.setCid(UUIDUtils.getUUID());
    		
    		CategoryService categoryService = (CategoryService) BeanFactory.getBean("CategoryService") ;
    		categoryService.save(category) ;
    		
    		resp.sendRedirect(req.getContextPath() + "/AdminCategoryServlet?method=findAll" );
    	}catch(Exception e) {
    		e.printStackTrace(); 
    		throw new RuntimeException() ;
    	}
    	return null ;
    }
    /**
     * 分类编辑：根据cid查到对应的分类，并跳转
     */
    public String edit(HttpServletRequest req, HttpServletResponse resp) { 
    	try{
    		String cid = req.getParameter("cid") ;
    		
    		CategoryService categoryService = (CategoryService) BeanFactory.getBean("CategoryService") ;
    		Category category = categoryService.fandByCid(cid) ;
    		
    		req.setAttribute("category", category);
    		
    	}catch(Exception e) {
    		e.printStackTrace(); 
    		throw new RuntimeException() ;
    	}
    	return "/admin/category/edit.jsp" ;
    }
    /**
     * 编辑分类
     */
    public String update(HttpServletRequest req, HttpServletResponse resp) { 
    	try{
    		Map<String,String[]> map = req.getParameterMap() ;
    		
    		Category category = new Category() ;
    		BeanUtils.populate(category, map);
    		
    		CategoryService categoryService = (CategoryService) BeanFactory.getBean("CategoryService") ;
    		categoryService.update(category) ;
    		
    		resp.sendRedirect(req.getContextPath() + "/AdminCategoryServlet?method=findAll" );
    	}catch(Exception e) {
    		e.printStackTrace(); 
    		throw new RuntimeException() ;
    	}
    	return null ;
    }
    /**
     * 删除分类
     */
    public String delete(HttpServletRequest req, HttpServletResponse resp) { 
    	try{
    		String cid = req.getParameter("cid") ;
//    		System.out.println(cid);
    		
    		CategoryService categoryService = (CategoryService) BeanFactory.getBean("CategoryService") ;
    		categoryService.delete(cid) ;
    		JSONObject json = new JSONObject();
    		json.put("ret", 1);
//    		System.out.println(json.toString());
    		
    		resp.getWriter().println(json.toString());
    		
    		//resp.sendRedirect(req.getContextPath() + "/AdminCategoryServlet?method=findAll");
    	}catch(Exception e) {
    		e.printStackTrace(); 
    		throw new RuntimeException() ;
    	}
    	return null ;
    }
}
