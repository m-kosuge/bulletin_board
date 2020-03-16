package dao;

import static utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import beans.User;
import exception.SQLRuntimeException;

public class UserDao {

	/**
	 * ユーザー登録
	 * @param コネクション
	 * @param ユーザー情報
	 */
	public void insert(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO users ( ");
			sql.append("	account, ");
			sql.append("	password, ");
			sql.append("	name, ");
			sql.append("	branch_id, ");
			sql.append("	department_id, ");
			sql.append("	is_stopped, ");
			sql.append("	created_date, ");
			sql.append("	updated_date ");
			sql.append(") VALUES ( ");
			sql.append("	?, ");									//account
			sql.append("	?, ");									//password
			sql.append("	?, ");									//name
			sql.append("	?, ");									//brandh_id
			sql.append("	?, ");									//department_id
			sql.append("	0, ");									//is_stopped
			sql.append("	CURRENT_TIMESTAMP, ");	//created_date
			sql.append("	CURRENT_TIMESTAMP ");	//updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, user.getAccount());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getName());
			ps.setInt(4, user.getBranchId());
			ps.setInt(5, user.getDepartmentId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 * ログインユーザー情報取得
	 * @param コネクション
	 * @param アカウント
	 * @param パスワード
	 * @return ユーザー情報
	 */
	public User select(Connection connection, String account, String password) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE account = ? AND password = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, account);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			List<User> users = toUsers(rs);

			//ユーザー情報が取得できなかった場合
			if (users.isEmpty()) {
				return null;
			//ユーザー情報が複数取得できた場合
			} else if (2 <= users.size()) {
				throw new IllegalStateException("ユーザーが重複しています");
			//ユーザー情報が正常に取得できた場合
			} else {
				return users.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 * 編集ユーザー情報取得
	 * @param コネクション
	 * @param ユーザーID
	 * @return ユーザー情報
	 */
	public User select(Connection connection, int id) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			List<User> users = toUsers(rs);

			//ユーザー情報が取得できなかった場合
			if (users.isEmpty() == true) {
				return null;
			//ユーザー情報が複数取得できた場合
			} else if (2 <= users.size()) {
				throw new IllegalStateException("ユーザーが重複しています");
			//ユーザー情報が正常に取得できた場合
			} else {
				return users.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 * 重複アカウントユーザー情報取得
	 * @param コネクション
	 * @param アカウント
	 * @return ユーザー情報
	 */
	public User select(Connection connection, String  account) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE account = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, account);

			ResultSet rs = ps.executeQuery();
			List<User> users = toUsers(rs);

			//ユーザー情報が取得できなかった場合
			if (users.isEmpty() == true) {
				return null;
			//ユーザー情報が複数取得できた場合
			} else if (2 <= users.size()) {
				throw new IllegalStateException("ユーザーが重複しています");
			//ユーザー情報が正常に取得できた場合
			} else {
				return users.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 * ユーザー更新
	 * @param コネクション
	 * @param ユーザー情報
	 */
	public void update(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET");
			sql.append("	account = ?, ");
			sql.append("	name = ?, ");
			sql.append("	branch_id = ?, ");
			sql.append("	department_id = ?, ");
			//パスワードが入力されていた場合
			if (!StringUtils.isEmpty(user.getPassword())) {
				sql.append("	password = ?, ");
			}
			sql.append("	updated_date = CURRENT_TIMESTAMP ");
			sql.append("WHERE id = ?");

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, user.getAccount());
			ps.setString(2, user.getName());
			ps.setInt(3, user.getBranchId());
			ps.setInt(4, user.getDepartmentId());
			//パスワードが入力されていなかった場合
			if (StringUtils.isEmpty(user.getPassword())) {
				ps.setInt(5, user.getId());
			} else {
				ps.setString(5, user.getPassword());
				ps.setInt(6, user.getId());
			}

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	* ユーザー停止・復活フラグ更新
	* @param コネクション
	* @param ユーザーID
	* @param 停止復活フラグ
	*/
	public void update(Connection connection, int id, int isStopped) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET is_stopped = ? ");
			sql.append("WHERE id = ?");

			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, isStopped);
			ps.setInt(2, id);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 * DB取得結果をリストへ格納
	 * @param DB結果
	 * @return ユーザー情報
	 */
	private List<User> toUsers(ResultSet rs) throws SQLException {

		List<User> users = new ArrayList<User>();
		try {
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setAccount(rs.getString("account"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setBranchId(rs.getInt("branch_id"));
				user.setDepartmentId(rs.getInt("department_id"));
				user.setIsStopped(rs.getInt("is_stopped"));
				user.setCreatedDate(rs.getTimestamp("created_date"));
				user.setUpdatedDate(rs.getTimestamp("updated_date"));

				users.add(user);
			}
			return users;
		} finally {
			close(rs);
		}
	}
}