package store.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import store.dao.MenuDao;
import store.domain.Menu;
import store.service.MenuService;
import store.utils.BeanFactory;
/**
 * 管理员：menu
 * @author Leo
 *
 */
public class MenuServiceImpl implements MenuService {

	public List<Menu> findMenu() throws SQLException {
		MenuDao menuDao = (MenuDao) BeanFactory.getBean("MenuDao") ;
		return menuDao.findMenu();
	}


}
