package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import beans.Branch;
import beans.Department;
import beans.User;
import service.BranchService;
import service.DepartmentService;
import service.UserService;

@WebServlet(urlPatterns = { "/signup" })
public class SignupServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		List<Branch> branches = new ArrayList<Branch>();
		List<Department> departments = new ArrayList<Department>();

		//支社・部署情報取得
		branches = new BranchService().select();
		departments = new DepartmentService().select();

		request.setAttribute("branches", branches);
		request.setAttribute("departments", departments);

		request.getRequestDispatcher("/signup.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

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
			request.setAttribute("user", user);
			request.setAttribute("branches", branches);
			request.setAttribute("departments", departments);

			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("signup.jsp").forward(request, response);
			return;
		}

		//ユーザー登録
		new UserService().insert(user);
		response.sendRedirect("management");
	}

	/**
	 * @param リクエスト
	 * @return 入力情報
	 */
	private User getUser(HttpServletRequest request) throws IOException, ServletException {

		User user = new User();
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
			if (duplicateUser != null) {
				errorMessages.add("アカウントが重複しています");
			}
		}
		if (StringUtils.isBlank(name)) {
			errorMessages.add("名前を入力してください");
		} else if (10 < name.length()) {
			errorMessages.add("名前は10文字以下で入力してください");
		}
		if (StringUtils.isBlank(password)) {
			errorMessages.add("パスワードを入力してください");
		} else if(!password.matches("[ -~]{6,20}")){
			errorMessages.add("パスワードは6文字以上20文字以下の記号を含む全ての半角文字で入力してください");
		} else if (!(password.equals(confirmPassword))){
			errorMessages.add("確認用のパスワードが一致していません");
		}
		if (((branchId == 1) && (2 < departmentId)) ||
			((1 < branchId) && (departmentId < 3))) {
			errorMessages.add("支社と部署の組み合わせが一致していません");
		}

		if (errorMessages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}