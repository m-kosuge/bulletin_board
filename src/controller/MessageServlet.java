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

import beans.Message;
import beans.User;
import service.MessageService;

@WebServlet(urlPatterns = { "/message" })
public class MessageServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.getRequestDispatcher("/message.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		List<String> errorMessages = new ArrayList<String>();

		Message message = new Message();
		message.setText(request.getParameter("text"));
		message.setTitle(request.getParameter("title"));
		message.setCategory(request.getParameter("category"));

		//入力に誤りがある場合
		if (!isValid(message, errorMessages)) {
			//値の保持
			request.setAttribute("message", message);

			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			return;
		}

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		message.setUserId(loginUser.getId());

		//メッセージ登録
		new MessageService().insert(message);
		response.sendRedirect("./");
	}

	/**
	 * 入力チェック
	 * @param 入力情報
	 * @param エラーメッセージ格納用
	 * @return 入力が正しければtrue
	 */
	private boolean isValid(Message messages, List<String> errorMessages) {

		String text = messages.getText();
		String title = messages.getTitle();
		String category = messages.getCategory();

		if (StringUtils.isBlank(title)) {
			errorMessages.add("件名を入力してください");
		} else if (30 < title.length()) {
			errorMessages.add("件名は30文字以下で入力してください");
		}
		if (StringUtils.isBlank(text)) {
			errorMessages.add("本文を入力してください");
		} else if (1000 < text.length()) {
			errorMessages.add("本文は1000文字以下で入力してください");
		}
		if (StringUtils.isBlank(category)) {
			errorMessages.add("カテゴリーを入力してください");
		} else if(10 < category.length()) {
			errorMessages.add("カテゴリーは10文字以下で入力してください");
		}

		if (errorMessages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}