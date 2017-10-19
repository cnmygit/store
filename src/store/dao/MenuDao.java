package store.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import store.domain.Menu;

public interface MenuDao {

	List<Menu> findMenu() throws SQLException;


}
