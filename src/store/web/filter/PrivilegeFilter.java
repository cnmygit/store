package store.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import store.domain.User;

public class PrivilegeFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request ;
		User existUser = (User) req.getSession().getAttribute("existUser") ;
		if(existUser == null) {
			req.setAttribute("msg", "你以为自己很帅？请先登录");
			req.getRequestDispatcher("/jsp/login.jsp").forward(req, response);
			return ;
		}
		chain.doFilter(req, response);
	}

	public void destroy() {
		
	}

}
