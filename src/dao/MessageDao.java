package dao;

import static utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import beans.Message;
import exception.SQLRuntimeException;

public class MessageDao {

	/**
	 *メッセージ登録
	 * @param コネクション
	 * @param メッセージ情報
	 */
	public void insert(Connection connection, Message message) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			sql.append("	title, ");
			sql.append("	text, ");
			sql.append("	category, ");
			sql.append("	user_id, ");
			sql.append("	created_date, ");
			sql.append("	updated_date ");
			sql.append(") VALUES ( ");
			sql.append("	?, ");									//title
			sql.append("	?, ");									//text
			sql.append("	?, ");									//category
			sql.append("	?, ");									//user_id
			sql.append("	CURRENT_TIMESTAMP, ");	//created_date
			sql.append("	CURRENT_TIMESTAMP ");		//updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, message.getTitle());
			ps.setString(2, message.getText());
			ps.setString(3, message.getCategory());
			ps.setInt(4, message.getUserId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	/**
	 *メッセージ削除
	 * @param コネクション
	 * @param メッセージID
	 */
	public void delete (Connection connection, int id) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM messages ");
			sql.append("WHERE id = ?");

			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, id);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
