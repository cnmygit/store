package store.service.impl;

import java.sql.SQLException;

import store.dao.UserDao;
import store.domain.User;
import store.service.UserService;
import store.utils.BeanFactory;
import store.utils.MailUtils;
import store.utils.UUIDUtils;


public class UserServiceImpl implements UserService {

	public User findByUsername(String username) throws SQLException {
		UserDao userDao = (UserDao) BeanFactory.getBean("UserDao") ;
		return userDao.findByUsername(username);
	}

	public void save(User user) throws SQLException {
		UserDao userDao = (UserDao) BeanFactory.getBean("UserDao") ;
		user.setUid(UUIDUtils.getUUID());
		user.setState(1); //1代表未激活，2代表激活
		user.setCode(UUIDUtils.getUUID()+UUIDUtils.getUUID());
		System.out.println(user);
		userDao.save(user);
		
		//发送激活邮件
		MailUtils.sendMail(user.getEmail(), user.getCode());
	}
	/**
	 * 根据code邮箱激活
	 */
	public User findByCode(String code) throws SQLException {
		UserDao userDao = (UserDao) BeanFactory.getBean("UserDao") ;
		return userDao.findByCode(code);
	}

	public void update(User existUser) throws SQLException {
		UserDao userDao = (UserDao) BeanFactory.getBean("UserDao") ;
		userDao.update(existUser) ;
	}

	public User login(User user) throws SQLException {
		UserDao userDao = (UserDao) BeanFactory.getBean("UserDao") ;
		return userDao.login(user);
	}
	

}
