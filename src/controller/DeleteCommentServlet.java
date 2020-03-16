package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CommentService;

@WebServlet(urlPatterns = {"/deleteComment"})
public class DeleteCommentServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		int commentId = Integer.parseInt(request.getParameter("id"));

		//コメント削除
		new CommentService().delete(commentId);
		response.sendRedirect("./");
	}
}
