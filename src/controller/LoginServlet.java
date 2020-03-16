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

import beans.User;
import service.UserService;

@WebServlet(urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		List<String> errorMessages = new ArrayList<String>();

		String account = request.getParameter("account");
		String password = request.getParameter("password");

		User loginUser = null;

		//入力が正しい場合
		if (isValid(account, password, errorMessages)) {
			//ユーザー情報取得
			loginUser = new UserService().select(account, password);

			//ユーザーが存在しない場合、アカウントが停止している場合
			if ((loginUser == null) || (loginUser.getIsStopped() != 0)) {
				errorMessages.add("ログインに失敗しました");
			}
		}

		//入力に誤りがある場合
		if (errorMessages.size() != 0) {
			//値の保持
			request.setAttribute("account", account);

			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}

		HttpSession session = request.getSession();
		session.setAttribute("loginUser", loginUser);
		response.sendRedirect("./");
	}

	/**
	 * 入力チェック
	 * @param 入力情報
	 * @param 入力情報
	 * @param エラーメッセージ格納用
	 * @return 入力が正しければtrue
	 */
	private boolean isValid(String account, String password, List<String> errorMessages) {

		if (StringUtils.isBlank(account)) {
			errorMessages.add("アカウントを入力してください");
		}
		if (StringUtils.isBlank(password)) {
			errorMessages.add("パスワードを入力してください");
		}

		if (errorMessages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}