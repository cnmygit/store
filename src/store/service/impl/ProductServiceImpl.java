package store.service.impl;

import java.sql.SQLException;
import java.util.List;

import store.dao.ProductDao;
import store.dao.UserDao;
import store.dao.impl.ProductDaoImpl;
import store.domain.PageBean;
import store.domain.Product;
import store.service.ProductService;
import store.utils.BeanFactory;

public class ProductServiceImpl implements ProductService {

	public List<Product> findByNew() throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("ProductDao") ;
		return productDao.findByNew();
	}

	public List<Product> findByHot() throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("ProductDao") ;
		return productDao.findByHot();
	}

	public PageBean<Product> findPageByCid(String cid, Integer currPage) throws SQLException {
		PageBean<Product> pageBean = new PageBean<Product>() ;
		//设置当前页
		pageBean.setCurrPage(currPage); 
		//每页显示的记录数
		Integer pageSize = 12 ;
		pageBean.setPageSize(pageSize);
		//总记录数
		ProductDao productDao = (ProductDao) BeanFactory.getBean("ProductDao") ;
		Integer totalCount = productDao.findCountByCid(cid) ;
		//总页数
		double tc = totalCount ;
		Double num = Math.ceil(tc / pageSize) ;
		pageBean.setTotalPage(num.intValue()); 
		//设置每页显示记录的集合
		int begin = (currPage - 1) * pageSize ;
		List<Product> list = productDao.findPageList(cid,begin,pageSize) ;
		pageBean.setList(list);
		return pageBean;
	}

	public Product findByPid(String pid) throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("ProductDao") ;
		return productDao.findByPid(pid);
	}
	/**
	 * 商品管理的分页显示
	 */
	public PageBean<Product> findByPage(Integer currPage) throws SQLException {
		PageBean<Product> pageBean = new PageBean<Product>() ;
		//设置当前页
		pageBean.setCurrPage(currPage); 
		//每页显示的记录数
		Integer pageSize = 10 ;
		pageBean.setPageSize(pageSize);
		//总记录数
		ProductDao productDao = (ProductDao) BeanFactory.getBean("ProductDao") ;
		Integer totalCount = productDao.findCount() ;
		pageBean.setTotalCount(totalCount);
//		System.out.println("totalCount：" + totalCount);
		//总页数
		double tc = totalCount ;
		Double num = Math.ceil(tc / pageSize) ;
		pageBean.setTotalPage(num.intValue()); 
		//设置每页显示记录的集合
		int begin = (currPage - 1) * pageSize ;
		List<Product> list = productDao.findPageList(begin,pageSize) ;
		pageBean.setList(list);
		return pageBean;
		
	}
	/**
	 * 管理员：添加商品的保存
	 */
	public void save(Product product) throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("ProductDao") ;
		productDao.save(product) ;
	}
	/**
	 * 管理员：商品下架
	 */
	public void update(Product product) throws SQLException {
		ProductDao productDao = (ProductDao) BeanFactory.getBean("ProductDao") ;
		productDao.update(product) ;
	}
	/**
	 * 找到下架的商品集合
	 */
	public PageBean<Product> findByPushDown(Integer currPage) throws SQLException {
		PageBean<Product> pageBean = new PageBean<Product>() ;
		//设置当前页
		pageBean.setCurrPage(currPage); 
		//每页显示的记录数
		Integer pageSize = 10 ;
		pageBean.setPageSize(pageSize);
		//总记录数
		ProductDao productDao = (ProductDao) BeanFactory.getBean("ProductDao") ;
		Integer totalCount = productDao.findPushDownCount() ;
		pageBean.setTotalCount(totalCount);
//		System.out.println("totalCount：" + totalCount);
		//总页数
		double tc = totalCount ;
		Double num = Math.ceil(tc / pageSize) ;
		pageBean.setTotalPage(num.intValue()); 
		//设置每页显示记录的集合
		int begin = (currPage - 1) * pageSize ;
		List<Product> list = productDao.findPushDownPageList(begin,pageSize) ;
		pageBean.setList(list);
		return pageBean;
	}

}
