package store.web.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import store.domain.User;
import store.service.UserService;
import store.service.impl.UserServiceImpl;
import store.utils.CookieUtils;



public class AutoLoginFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//判断session中是否有用户信息
		HttpServletRequest req = (HttpServletRequest) request ;
		HttpSession session = req.getSession() ;
		User existUser = (User) session.getAttribute("existUser") ;
		if(existUser != null) {
			//session 中有用户信息
			chain.doFilter(req, response);
		}else {
			//session 中没有用户信息
			//获得Cookie数据
			Cookie[] cookies = req.getCookies() ;
			Cookie cookie = CookieUtils.findCookie(cookies, "autologin") ;
			if(cookie == null) {
				//没有携带cookie信息
				chain.doFilter(req, response);
			}else {
				//携带了cookie信息
				String value = cookie.getValue() ;
				String username = value.split("#")[0] ;
				String password = value.split("#")[1] ;
				//封装数据
				User user = new User() ;
				user.setUsername(username);
				user.setPassword(password);
				//调用业务层
				UserService userService = new UserServiceImpl() ;
				try {
					existUser = userService.login(user) ;
					if(existUser == null) {
						//用户名或密码错误：cookie可能被篡改了
						chain.doFilter(req, response);
					}else {
						session.setAttribute("existUser", existUser);
						chain.doFilter(req, response);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void destroy() {
		
	}
       
    

}
