package service;

import static utils.CloseableUtil.*;
import static utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import beans.Comment;
import beans.UserComment;
import dao.CommentDao;
import dao.UserCommentDao;

public class CommentService {

	/**
	 * コメント登録
	 * @param コメント情報
	 */
	public void insert(Comment comment) {

		Connection connection = null;
		try {
			connection = getConnection();
			new CommentDao().insert(connection, comment);
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
	 * コメント情報取得
	 * @return コメント情報
	 */
	public List<UserComment> select() {

		Connection connection = null;
		try {
			connection = getConnection();
			List<UserComment> comments = new UserCommentDao().select(connection);
			commit(connection);
			return comments;
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
	 * コメント削除
	 * @param コメントID
	 */
	public void delete(int id) {

		Connection connection = null;
		try {
			connection = getConnection();
			new CommentDao().delete(connection, id);
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