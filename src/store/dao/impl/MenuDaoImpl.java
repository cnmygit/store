package store.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import store.dao.MenuDao;
import store.domain.Menu;
import store.utils.JDBCUtils;

public class MenuDaoImpl implements MenuDao {

	public List<Menu> findMenu() throws SQLException {
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from menu" ;
		List<Menu> list = qr.query(sql, new BeanListHandler<Menu>(Menu.class)) ;
		return list;
	}

}
