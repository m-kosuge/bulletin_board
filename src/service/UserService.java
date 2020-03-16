package service;

import static utils.CloseableUtil.*;
import static utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import beans.User;
import beans.UserBranchDepartment;
import dao.UserBranchDepartmentDao;
import dao.UserDao;
import utils.CipherUtil;

public class UserService {

	/**
	 * ユーザー登録
	 * @param ユーザー情報
	 */
	public void insert(User user) {

		Connection connection = null;
		try {
			//パスワード暗号化
			String encPassword = CipherUtil.encrypt(user.getPassword());
			user.setPassword(encPassword);

			connection = getConnection();
			new UserDao().insert(connection, user);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	/**
	 * 編集ユーザー情報取得
	 * @param ユーザーID
	 * @return ユーザー情報
	 */
	public User select(int userId) {

		Connection connection = null;
		try {
			connection = getConnection();
			User user = new UserDao().select(connection, userId);
			commit(connection);
			return user;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	/**
	 * ログインユーザー情報取得
	 * @param アカウント
	 * @param パスワード
	 * @return ユーザー情報
	 */
	public User select(String account, String password) {

		Connection connection = null;
		try {
			//パスワード暗号化
			String encPassword = CipherUtil.encrypt(password);

			connection = getConnection();
			User user = new UserDao().select(connection, account, encPassword);
			commit(connection);
			return user;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	/**
	 * 重複アカウントユーザー情報取得
	 * @param アカウント
	 * @return ユーザー情報
	 */
	public User select(String  account) {

		Connection connection = null;
		try {
			connection = getConnection();
			User user = new UserDao().select(connection, account);
			commit(connection);
			return user;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	/**
	 * ユーザー一覧情報取得
	 * @return 全ユーザー情報
	 */
	public List<UserBranchDepartment> select() {

		Connection connection = null;
		try {
			connection = getConnection();
			List<UserBranchDepartment> users = new UserBranchDepartmentDao().select(connection);
			commit(connection);
			return users;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	/**
	 * ユーザー情報更新
	 * @param ユーザー情報
	 */
	public void update(User user) {

		Connection connection = null;
		try {
			//パスワードが入力されていた場合
			if (!StringUtils.isEmpty(user.getPassword())) {
				//パスワードの暗号化
				String encPassword = CipherUtil.encrypt(user.getPassword());
				user.setPassword(encPassword);
			}

			connection = getConnection();
			new UserDao().update(connection, user);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	/**
	 * ユーザー停止・復活フラグ更新
	 * @param ユーザーID
	 * @param 停止・復活フラグ
	 */
	public void update(int id, int isStopped) {

		Connection connection = null;
		try {
			connection = getConnection();
			new UserDao().update(connection, id, isStopped);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}
}