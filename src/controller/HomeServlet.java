package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.UserComment;
import beans.UserMessage;
import service.CommentService;
import service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class HomeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException {

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String category = request.getParameter("category");

		//メッセージ・コメント情報取得
		List<UserMessage> messages = new MessageService().select(startDate, endDate, category);
		List<UserComment> comments = new CommentService().select();

		//値の保持
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("category", category);

		request.setAttribute("messages", messages);
		request.setAttribute("comments", comments);

		request.getRequestDispatcher("/home.jsp").forward(request, response);
	}
}