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

@WebFilter(filterName = "authorityFilter" ,urlPatterns = {"/management", "/signup", "/setting"})
public class AuthorityFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		List<String> errorMessages = new ArrayList<String>();
		HttpSession session = ((HttpServletRequest)request).getSession();
		User user = (User)session.getAttribute("loginUser");

		//本社・総務人事部以外の場合
		if(user.getBranchId() != 1 || user.getDepartmentId() != 1) {
			errorMessages.add("権限がありません");
			session.setAttribute("errorMessages", errorMessages);
			((HttpServletResponse)response).sendRedirect("./");
			return;
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
