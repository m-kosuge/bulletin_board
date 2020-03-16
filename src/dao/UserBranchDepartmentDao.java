package dao;

import static utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.UserBranchDepartment;
import exception.SQLRuntimeException;

public class UserBranchDepartmentDao {

	/**
	 *全ユーザー情報取得
	 * @param コネクション
	 * @return 全ユーザー情報
	 */
	public List<UserBranchDepartment> select(Connection connection) {

		PreparedStatement ps = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("	users.id,  ");
			sql.append("	users.account, ");
			sql.append("	users.password, ");
			sql.append("	users.name, ");
			sql.append("	users.is_stopped, ");
			sql.append("	users.branch_id, ");
			sql.append("	users.department_id, ");
			sql.append("	branches.name as branch_name, ");
			sql.append("	departments.name as department_name, ");
			sql.append("	users.created_date, ");
			sql.append("	users.updated_date ");
			sql.append("FROM users ");
			sql.append("INNER JOIN branches ");
			sql.append("ON users.branch_id = branches.id ");
			sql.append("INNER JOIN departments ");
			sql.append("ON users.department_id = departments.id ");
			sql.append("ORDER BY created_date ASC");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<UserBranchDepartment> users = toUsers(rs);
			return users;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 * DB取得結果をリストへ格納
	 * @param DB結果
	 * @return 全ユーザー情報
	 */
	private List<UserBranchDepartment> toUsers(ResultSet rs) throws SQLException{

		List<UserBranchDepartment> users = new ArrayList<UserBranchDepartment>();
		while (rs.next()) {
			UserBranchDepartment user = new UserBranchDepartment();
			user.setId(rs.getInt("id"));
			user.setAccount(rs.getString("account"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setIsStopped(rs.getInt("is_stopped"));
			user.setBranchId(rs.getInt("branch_id"));
			user.setDepartmentId(rs.getInt("department_id"));
			user.setBranchName(rs.getString("branch_name"));
			user.setDepartmentName(rs.getString("department_name"));
			user.setCreatedDate(rs.getTimestamp("created_date"));
			user.setUpdatedDate(rs.getTimestamp("updated_date"));

			users.add(user);
		}
		return users;
	}
}