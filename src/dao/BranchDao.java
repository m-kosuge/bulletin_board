package dao;

import static utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Branch;
import exception.SQLRuntimeException;
import utils.CloseableUtil;

public class BranchDao {

	/**
	 * 支社情報取得
	 * @param コネクション
	 * @return 支社情報
	 */
	public List<Branch> select (Connection connection) {

		PreparedStatement ps = null;

		try {
			String sql = "SELECT * FROM branches";
			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<Branch> branches = toBranches(rs);
			return branches;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			CloseableUtil.close(ps);
		}
	}

	/**
	 * DB取得結果をリストへ格納
	 * @param DB結果
	 * @return 支社情報
	 */
	private List<Branch> toBranches(ResultSet rs) throws SQLException {

		List<Branch> branches = new ArrayList<Branch>();
		try {
			while (rs.next()) {
				Branch branch = new Branch();
				branch.setId(rs.getInt("id"));
				branch.setName(rs.getString("name"));
				branch.setCreatedDate(rs.getTimestamp("created_date"));
				branch.setUpdatedDate(rs.getTimestamp("updated_date"));

				branches.add(branch);
			}
			return branches;
		} finally {
			close(rs);
		}
	}
}