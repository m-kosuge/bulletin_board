package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import beans.Comment;
import beans.User;
import service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		Comment comment = new Comment();
		comment.setText(request.getParameter("text"));
		comment.setMessageId(Integer.parseInt(request.getParameter("messageId")));

		//入力に誤りがある場合
		if (!isValid(comment.getText(), errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		User loginUser = (User) session.getAttribute("loginUser");
		comment.setUserId(loginUser.getId());

		//コメント登録
		new CommentService().insert(comment);
		response.sendRedirect("./");
	}

	/**
	 * 入力チェック
	 * @param 入力情報
	 * @param エラーメッセージ格納用
	 * @return 入力が正しければtrue
	 */
	private boolean isValid(String text, List<String> errorMessages) {

		if (StringUtils.isBlank(text)) {
			errorMessages.add("コメントを入力してください");
		} else if (500 < text.length()) {
			errorMessages.add("コメントは500文字以下で入力してください");
		}

		if (errorMessages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}