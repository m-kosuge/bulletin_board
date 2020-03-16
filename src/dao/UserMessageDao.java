package dao;

import static utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import beans.UserMessage;
import exception.SQLRuntimeException;

public class UserMessageDao {

	/**
	 * メッセージ情報取得
	 * @param コネクション
	 * @param 開始日時
	 * @param 終了日時
	 * @param カテゴリー
	 * @return メッセージ情報
	 */
	public List<UserMessage> select(Connection connection,
			String startDateTime, String endDateTime, String category) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("	messages.id, ");
			sql.append("	messages.title, ");
			sql.append("	messages.text, ");
			sql.append("	messages.category, ");
			sql.append("	users.id as user_id, ");
			sql.append("	users.name, ");
			sql.append("	messages.created_date, ");
			sql.append("	messages.updated_date ");
			sql.append("FROM messages ");
			sql.append("INNER JOIN users ");
			sql.append("ON messages.user_id = users.id ");
			sql.append("WHERE messages.created_date BETWEEN ? AND ? ");
			if (!StringUtils.isEmpty(category)) {
				sql.append("AND category LIKE ? ");
			}
			sql.append("ORDER BY messages.created_date DESC ");

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, startDateTime);
			ps.setString(2, endDateTime);
			if (!StringUtils.isEmpty(category)) {
				ps.setString(3, "%" + category + "%");
			}

			ResultSet rs = ps.executeQuery();
			List<UserMessage> messages = toMessages(rs);
			return messages;
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 * DB取得結果をリストへ格納
	 * @param DB結果
	 * @return メッセージ情報
	 */
	private List<UserMessage> toMessages(ResultSet rs) throws SQLException {

		List<UserMessage> messages = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				UserMessage message = new UserMessage();
				message.setId(rs.getInt("id"));
				message.setTitle(rs.getString("title"));
				message.setText(rs.getString("text"));
				message.setCategory(rs.getString("category"));
				message.setUserId(rs.getInt("user_id"));
				message.setUserName(rs.getString("name"));
				message.setCreatedDate(rs.getTimestamp("created_date"));
				message.setUpdatedDate(rs.getTimestamp("updated_date"));

				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}
	}
}