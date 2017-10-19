package store.service.impl;

import java.sql.SQLException;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import store.dao.CategoryDao;
import store.dao.ProductDao;
import store.dao.impl.CategoryDaoImpl;
import store.domain.Category;
import store.service.CategoryService;
import store.utils.BeanFactory;

public class CategoryServiceImpl implements CategoryService {
	/**
	 * 商品分类查询
	 * @throws SQLException 
	 */
	public List<Category> findAll() throws SQLException {
		/*CategoryDao categoryDao = new CategoryDaoImpl() ;
		return categoryDao.findAll();*/
		
		//配置文件
		CacheManager cacheManager = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml")) ;
		//从配置文件获得名称为categoryCache的缓存区
		Cache cache = cacheManager.getCache("categoryCache") ;
		//判断缓存中是否有list
		Element element = cache.get("list") ;
		List<Category> list = null ;
		if(element == null) {
			//缓存中没有数据
//			System.out.println("get data from Data Base");
			CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("CategoryDao") ;
			list = categoryDao.findAll() ;
			element = new Element("list", list) ;
			cache.put(element); 
		}else {
			//缓存中有数据
//			System.out.println("get data from cache");
			list = (List<Category>) element.getObjectValue() ;
		}
		return list ;
	}

	public void save(Category category) throws SQLException {
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("CategoryDao") ;
		categoryDao.save(category) ;
		
		//清空缓存，避免添加后，跳转会分类页面，页面不刷新问题
		CacheManager cacheManager = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml")) ;
		Cache cache = cacheManager.getCache("categoryCache") ;
		cache.remove("list") ;
	}
	/**
	 * 通过cid找到分类
	 */
	public Category fandByCid(String cid) throws SQLException {
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("CategoryDao") ;
		return categoryDao.fandByCid(cid);
	}
	/**
	 * 分类编辑；更新分类
	 */
	public void update(Category category) throws SQLException {
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("CategoryDao") ;
		categoryDao.update(category) ;
		
		//清空缓存，避免添加后，跳转会分类页面，页面不刷新问题
		CacheManager cacheManager = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml")) ;
		Cache cache = cacheManager.getCache("categoryCache") ;
		cache.remove("list") ;
	}
	/**
	 * 根据cid删除对应分类
	 */
	public void delete(String cid) throws SQLException {
		CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("CategoryDao") ;
		categoryDao.delete(cid) ;
		
		//清除缓存
		CacheManager cacheManager = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml"));
		// 从配置文件中获取名称为categoryCache缓存区
		Cache cache = cacheManager.getCache("categoryCache");
		// 从缓存中移除:
		cache.remove("list");
	}

}
