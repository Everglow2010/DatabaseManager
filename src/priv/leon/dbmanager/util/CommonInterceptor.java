package priv.leon.dbmanager.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CommonInterceptor extends HandlerInterceptorAdapter {
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		HttpSession session = request.getSession(true);
		String user = (String) session.getAttribute("LOGIN_USER_NAME");

		String url = request.getServletPath();

		if (url.indexOf("treesoft/login") != -1) {
			return true;
		}

		if (url.indexOf("treesoft/treesoft") != -1) {
			return true;
		}

		if (url.indexOf("static/css") != -1) {
			return true;
		}

		if (url.indexOf("static/images") != -1) {
			return true;
		}

		if (url.indexOf("static/plugins") != -1) {
			return true;
		}

		if (url.indexOf("treesoft/logout") != -1) {
			return true;
		}

		if (url.indexOf("logout") != -1) {
			return true;
		}

		if (url.indexOf("system/login") != -1) {
			return true;
		}
		if (url.indexOf("treesoft/loginVaildate") != -1) {
			return true;
		}

		if (user == null) {
			System.out.println(request.getContextPath());
			PrintWriter out = response.getWriter();
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");

			builder.append("window.top.location.href=\"");
			builder.append(request.getContextPath());
			builder.append("/index.jsp\";</script>");
			out.print(builder.toString());
			out.close();
			return false;
		}

		return true;
	}
}