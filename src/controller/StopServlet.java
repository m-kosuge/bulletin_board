package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.UserService;

@WebServlet(urlPatterns = {"/stop"})
public class StopServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		int id = Integer.parseInt(request.getParameter("id"));
		int isStopped = Integer.parseInt(request.getParameter("isStopped"));

		//ユーザー停止・復活
		new UserService().update(id, isStopped);

		response.sendRedirect("management");
	}
}
