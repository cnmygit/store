package store.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import store.dao.UserDao;
import store.domain.User;
import store.utils.JDBCUtils;

/**
 * 用户数据层
 * @author Leo
 *
 */
public class UserDaoImpl implements UserDao {
	/**
	 * 异步校验用户名
	 * @throws SQLException 
	 */
	public User findByUsername(String username) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from user where username = ?" ;
		User user = queryRunner.query(sql, new BeanHandler<User>(User.class),username) ;
		return user;
	}
	/**
	 * 用户注册
	 */
	public void save(User user) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "insert into user values (?,?,?,?,?,?,?,?,?,?)" ;
		Object[] params = {user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),
						user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode()} ;
		queryRunner.update(sql,params) ;
	}
	/**
	 * 更具code邮箱激活
	 */
	public User findByCode(String code) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from user where code = ?" ;
		User user = queryRunner.query(sql, new BeanHandler<User>(User.class),code) ;
		return user;
	}
	/**
	 * 更新数据库
	 */
	public void update(User user) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "update user set username=?, password=?, name=?, email=?, telephone=?, birthday=?, sex=?, state=?, code=? where uid = ? " ;
		Object[] params = {user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),
				user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode(),user.getUid()} ;
		queryRunner.update(sql,params) ;
	}
	public User login(User user) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource()) ;
		String sql = "select * from user where username=? and password=? and state=?" ;
		User existUser = queryRunner.query(sql, new BeanHandler<User>(User.class), user.getUsername(), user.getPassword(), 2) ;
		return existUser;
	}
}
