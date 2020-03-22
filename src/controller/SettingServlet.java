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

import beans.Branch;
import beans.Department;
import beans.User;
import service.BranchService;
import service.DepartmentService;
import service.UserService;

@WebServlet(urlPatterns = { "/setting" })
public class SettingServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = null;
		String userId = request.getParameter("id");

		//ユーザーIDが数字の場合
		if (!StringUtils.isEmpty(userId) && userId.matches("[0-9]{1,9}")) {
			//ユーザー情報取得
			user = new UserService().select(Integer.parseInt(userId));
		}

		//ユーザー情報が存在しない場合
		if (user == null) {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("不正なURLです");

			HttpSession session = request.getSession();
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("management");
			return;
		}

		List<Branch> branches = new ArrayList<Branch>();
		List<Department> departments = new ArrayList<Department>();

		//支社・部署情報取得
		branches = new BranchService().select();
		departments = new DepartmentService().select();

		request.setAttribute("user", user);
		request.setAttribute("branches", branches);
		request.setAttribute("departments", departments);
		request.getRequestDispatcher("setting.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<String> errorMessages = new ArrayList<String>();

		User user = getUser(request);
		String confirmPassword = request.getParameter("confirmPassword");

		//入力に誤りがある場合
		if (!isValid(user, confirmPassword, errorMessages)) {
			List<Branch> branches = new ArrayList<Branch>();
			List<Department> departments = new ArrayList<Department>();

			//支社・部署情報取得
			branches = new BranchService().select();
			departments = new DepartmentService().select();

			//値の保持
			request.setAttribute("branches", branches);
			request.setAttribute("departments", departments);
			request.setAttribute("user", user);

			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("setting.jsp").forward(request, response);
			return;
		}

		//ユーザー更新
		new UserService().update(user);

		HttpSession session = request.getSession();
		User loginUser = (User)session.getAttribute("loginUser");

		//ログインユーザーを編集した場合
		if (loginUser.getId() == user.getId()) {
			session.setAttribute("loginUser", user);
		}
		response.sendRedirect("management");
	}

	/**
	 * @param リクエスト
	 * @return 入力情報
	 */
	private User getUser(HttpServletRequest request) throws IOException, ServletException {

		User user = new User();
		user.setId(Integer.parseInt(request.getParameter("id")));
		user.setAccount(request.getParameter("account"));
		user.setPassword(request.getParameter("password"));
		user.setName(request.getParameter("name"));
		user.setBranchId(Integer.parseInt(request.getParameter("branchId")));
		user.setDepartmentId(Integer.parseInt(request.getParameter("departmentId")));
		return user;
	}

	/**
	 * 入力チェック
	 * @param 入力情報
	 * @param 入力された確認用パスワード
	 * @param エラーメッセージ格納用
	 * @return 入力が正しければtrue
	 */
	private boolean isValid(User user, String confirmPassword, List<String> errorMessages) {

		int id = user.getId();
		String account = user.getAccount();
		String password = user.getPassword();
		String name = user.getName();
		int branchId = user.getBranchId();
		int departmentId = user.getDepartmentId();

		if (StringUtils.isBlank(account)) {
			errorMessages.add("アカウントを入力してください");
		} else if (!account.matches("[0-9a-zA-Z]{6,20}")){
			errorMessages.add("アカウントは6文字以上20文字以下の半角英数字で入力してください");
		} else {
			User duplicateUser = new UserService().select(user.getAccount());
			if ((duplicateUser != null) && (duplicateUser.getId() != id)) {
				errorMessages.add("アカウントが重複しています");
			}
		}
		if (StringUtils.isBlank(name)) {
			errorMessages.add("名前を入力してください");
		} else if (10 < name.length()) {
			errorMessages.add("名前は10文字以下で入力してください");
		}
		if (!StringUtils.isEmpty(password) && (!password.matches("[ -~]{6,20}"))) {
			errorMessages.add("パスワードは6文字以上20文字以下の記号を含む全ての半角文字で入力してください");
		}
		if (!password.equals(confirmPassword)) {
			errorMessages.add("確認用パスワードが一致していません");
		}
		if (((branchId == 1) && (2 < departmentId)) ||
			((1 < branchId) && (departmentId < 3))) {
			errorMessages.add("支社と部署の組み合わせが一致していません");
		}

		if (errorMessages.size() == 0) {
			return true;
		}
		return false;
	}
}
