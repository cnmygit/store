package store.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import store.dao.CategoryDao;
import store.domain.User;
import store.service.UserService;
import store.service.impl.UserServiceImpl;
import store.utils.BaseServlet;
import store.utils.BeanFactory;
import store.utils.MyDateConverter;

/**
 * 用户模块的Servlet
 */
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    
	public String registUI(HttpServletRequest request, HttpServletResponse response) {
		return "/jsp/regist.jsp" ;
	}
	/**
	 * 异步校验用户名
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String checkUsername(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		try {
			//接收参数
			request.setCharacterEncoding("UTF-8");
			String username = request.getParameter("username") ;
			
			//调用业务层
			UserService userService = (UserService) BeanFactory.getBean("UserService") ;
			User existUser = userService.findByUsername(username) ;
			
			if(existUser == null) {
				response.getWriter().println("1");
			}else {
				response.getWriter().println("2");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
		return null ;
	}
	/**
	 * 用户注册的执行的方法:regist
	 */
	public String regist(HttpServletRequest req,HttpServletResponse resp){
		try{
			// 接收数据:
			Map<String,String[]> map = req.getParameterMap();
			// 封装数据:
			User user = new User();
			ConvertUtils.register(new MyDateConverter(), Date.class);
			BeanUtils.populate(user, map);
			// 调用业务层:
			UserService userService = (UserService) BeanFactory.getBean("UserService") ;
			userService.save(user);
			// 页面跳转:
			req.setAttribute("msg", "注册成功！请去邮箱激活!");
			return "/jsp/msg.jsp";
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	/**
	 * 邮箱激活
	 * @param req
	 * @param resp
	 * @return
	 */
	public String active(HttpServletRequest req,HttpServletResponse resp){
		try{
			String code = req.getParameter("code") ;
			//根据激活码激活
			UserService userService = (UserService) BeanFactory.getBean("UserService") ;
			User existUser = userService.findByCode(code) ;
			
			if(existUser == null) {
				//激活码有误
				req.setAttribute("msg", "激活码有误，请重新激活");
			}else {
				//激活操作
				existUser.setState(2); 
				existUser.setCode(null);
				userService.update(existUser) ;
				req.setAttribute("msg", "激活成功，可以登录了");
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return "/jsp/msg.jsp" ;
	}
	public String loginUI(HttpServletRequest req,HttpServletResponse resp){
		return "jsp/login.jsp" ;
	}
	/**
	 * 用户登录页面
	 */
	public String login(HttpServletRequest req,HttpServletResponse resp){
		try{
			//一次性验证码程序
			String code1 = req.getParameter("code") ;
			String code2 = (String) req.getSession().getAttribute("code") ;
			req.getSession().removeAttribute("code");
			if(!code1.equalsIgnoreCase(code2)) {
				req.setAttribute("msg", "验证码输错误");
				return "/jsp/login.jsp" ;
			}
			
			Map<String,String[]> map = req.getParameterMap() ;
			
			User user = new User() ;
			BeanUtils.populate(user, map);
			
			UserService userService = (UserService) BeanFactory.getBean("UserService") ;
			User existUser = userService.login(user) ;
			
			if(existUser == null) {
				req.setAttribute("msg", "用户名或密码错误或用户未激活");
				return "/jsp/login.jsp" ;
			}else {
				//登录成功：自动登录
				String autoLogin = req.getParameter("autologin") ;
				if("true".equals(autoLogin)) {
					Cookie cookie = new Cookie("autoLogin", existUser.getUsername()+"#"+existUser.getPassword()) ;
					cookie.setPath("/store_v2.0");
					cookie.setMaxAge(7 * 24 * 60 * 60);
					resp.addCookie(cookie);
				}
				
				//记住用户名
				String remeberun = req.getParameter("remeberun") ;
				if("true".equals(remeberun)) {
					Cookie cookie = new Cookie("username", existUser.getUsername()) ;
					cookie.setPath("/store_v2.0");
					cookie.setMaxAge(24 * 60 * 60);
					resp.addCookie(cookie);
				}
					
				req.getSession().setAttribute("existUser", existUser);
				resp.sendRedirect(req.getContextPath() + "/index.jsp");
				return null ;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
	}
	/**
	 * 用户退出
	 * @param req
	 * @param resp
	 * @return
	 */
	public String logOut(HttpServletRequest req,HttpServletResponse resp){
		try {
			//销毁session
			req.getSession().invalidate();
			//如果有自动登录，autoLogin也要消除
			Cookie cookie = new Cookie("autoLogin", "") ;
			cookie.setPath("/store_v2.0");
			cookie.setMaxAge(0);
			resp.addCookie(cookie);
			 
			resp.sendRedirect(req.getContextPath() + "/index.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null ;
	}
}
