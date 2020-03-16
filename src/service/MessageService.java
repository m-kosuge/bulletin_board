package service;

import static utils.CloseableUtil.*;
import static utils.DBUtil.*;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import beans.Message;
import beans.UserMessage;
import dao.MessageDao;
import dao.UserMessageDao;

public class MessageService {

	/**
	 * メッセージ登録
	 * @param メッセージ情報
	 */
	public void insert(Message message) {

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().insert(connection, message);
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
	 * メッセージ情報取得
	 * @param 絞り込み開始日
	 * @param 絞り込み終了日
	 * @param カテゴリー
	 * @return メッセージ情報
	 */
	public List<UserMessage> select(String startDate, String endDate, String category) {

		String startDateTime = "2020-01-01 00:00:00";
		String endDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		Connection connection = null;
		try {
			connection = getConnection();

			//絞り込み開始日が入力されている場合
			if (!StringUtils.isEmpty(startDate)) {
				startDateTime = startDate + " 00:00:00";
			}
			//絞り込み終了日が入力されている場合
			if (!StringUtils.isEmpty(endDate)) {
				endDateTime = endDate + " 23:59:59";
			}

			List<UserMessage> messages = new UserMessageDao().select(
					connection, startDateTime, endDateTime, category);
			commit(connection);
			return messages;
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
	 * メッセージ削除
	 * @param メッセージID
	 */
	public void delete(int id) {

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().delete(connection, id);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch(Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}
}