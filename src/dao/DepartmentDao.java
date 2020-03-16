package dao;

import static utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Department;
import exception.SQLRuntimeException;
import utils.CloseableUtil;

public class DepartmentDao {

	/**
	 * 部署情報取得
	 * @param コネクション
	 * @return 部署情報
	 */
	public List<Department> select (Connection connection) {

		PreparedStatement ps = null;

		try {
			String sql = "SELECT * FROM departments";
			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<Department> departments = toDepartments(rs);
			return departments;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			CloseableUtil.close(ps);
		}
	}
	/**
	 * DB取得結果をリストへ格納
	 * @param DB結果
	 * @return 部署情報
	 */
	private List<Department> toDepartments(ResultSet rs) throws SQLException {

		List<Department> departments = new ArrayList<Department>();
		try {
			while (rs.next()) {
				Department department = new Department();
				department.setId(rs.getInt("id"));
				department.setName(rs.getString("name"));
				department.setCreatedDate(rs.getTimestamp("created_date"));
				department.setUpdatedDate(rs.getTimestamp("updated_date"));

				departments.add(department);
			}
			return departments;
		} finally {
			close(rs);
		}
	}
}