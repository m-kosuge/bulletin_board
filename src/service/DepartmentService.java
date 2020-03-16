package service;

import java.sql.Connection;
import java.util.List;

import beans.Department;
import dao.DepartmentDao;
import utils.CloseableUtil;
import utils.DBUtil;

public class DepartmentService {

	/**
	 * 部署情報取得
	 * @return 部署情報
	 */
	public List<Department> select() {

		Connection connection = null;
		try {
			connection = DBUtil.getConnection();
			List<Department> divisions = new DepartmentDao().select(connection);
			DBUtil.commit(connection);
			return divisions;
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