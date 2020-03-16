package dao;

import static utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.UserComment;
import exception.SQLRuntimeException;

public class UserCommentDao {

	/**
	 *コメント情報取得
	 * @param コネクション
	 * @return  コメント情報
	 */
	public List<UserComment> select(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("	comments.id as id, ");
			sql.append("	comments.text, ");
			sql.append("	comments.message_id, ");
			sql.append("	users.id as user_id, ");
			sql.append("	users.name, ");
			sql.append("	comments.created_date, ");
			sql.append("	comments.updated_date ");
			sql.append("FROM comments ");
			sql.append("INNER JOIN users ");
			sql.append("ON comments.user_id = users.id ");
			sql.append("ORDER BY created_date ASC ");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<UserComment> comments = toComments(rs);
			return comments;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 * DB取得結果をリストへ格納
	 * @param DB結果
	 * @return コメント情報
	 */
	private List<UserComment> toComments(ResultSet rs) throws SQLException {

		List<UserComment> comments = new ArrayList<UserComment>();
		try {
			while (rs.next()) {
				UserComment comment = new UserComment();
				comment.setId(rs.getInt("id"));
				comment.setText(rs.getString("text"));
				comment.setUserId(rs.getInt("user_id"));
				comment.setMessageId(rs.getInt("message_id"));
				comment.setUserName(rs.getString("name"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));
				comment.setUpdatedDate(rs.getTimestamp("updated_date"));

				comments.add(comment);
			}
			return comments;
		} finally {
			close(rs);
		}
	}
}