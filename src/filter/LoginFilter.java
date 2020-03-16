package filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;

@WebFilter(filterName = "loginFilter" , urlPatterns = "/*")
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		List<String> errorMessages = new ArrayList<String>();
		HttpSession session = ((HttpServletRequest)request).getSession();
		User user = (User)session.getAttribute("loginUser");
		String servletPath = ((HttpServletRequest)request).getServletPath();

		//ログイン画面・CSS以外の場合
		if ((!servletPath.equals("/login")) && (!servletPath.contains(".css"))) {
			//ログインしていない場合
			if (user == null) {
				errorMessages.add("ログインしてください");
				session.setAttribute("errorMessages", errorMessages);
				((HttpServletResponse)response).sendRedirect("login");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}
}