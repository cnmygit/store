package store.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import store.domain.Menu;

public interface MenuService {

	List<Menu> findMenu() throws SQLException;


}
