package store.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用的servlet的编写
 * @author Leo
 *
 */
public class BaseServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//接收参数
		req.setCharacterEncoding("UTF-8");
		String methodName = req.getParameter("method") ;
		//判断，可不用
		resp.setContentType("text/html;charset=UTF-8");
		if(methodName == null || "".equals(methodName)) {
			resp.getWriter().println("method is null（空）");
			return ;
		}
		//获得子类对象
		Class clazz = this.getClass() ;
		try {
			Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class) ;
			String path = (String) method.invoke(this, req, resp) ;
			if(path != null) {
				req.getRequestDispatcher(path).forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException() ;
		}
	}
}
