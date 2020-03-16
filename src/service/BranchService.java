package service;

import java.sql.Connection;
import java.util.List;

import beans.Branch;
import dao.BranchDao;
import utils.CloseableUtil;
import utils.DBUtil;

public class BranchService {

	/**
	 * 支社情報取得
	 * @return 支社情報
	 */
	public List<Branch> select() {

		Connection connection = null;
		try {
			connection = DBUtil.getConnection();
			List<Branch> branches = new BranchDao().select(connection);
			DBUtil.commit(connection);
			return branches;
		} catch (RuntimeException e) {
			DBUtil.rollback(connection);
			throw e;
		} catch (Error e) {
			DBUtil.rollback(connection);
			throw e;
		} finally {
			CloseableUtil.close(connection);
		}
	}
}