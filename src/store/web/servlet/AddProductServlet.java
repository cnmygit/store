package store.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import store.domain.Category;
import store.domain.Product;
import store.service.ProductService;
import store.utils.BeanFactory;
import store.utils.UUIDUtils;

/**
 * 管理员：添加商品
 */
public class AddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			// 创建一个Product对象
			Product product = new Product() ;
			// 创建磁盘文件项工厂
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory() ;
			// 设置缓存区大小，超出则产生临时文件
			diskFileItemFactory.setSizeThreshold(3 * 1024 * 1024);
			// 设置临时文件存放的路劲
//			diskFileItemFactory.setRepository(repository); 
			// 获得核心解析类
			ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory) ;
			fileUpload.setHeaderEncoding("UTF-8"); // 解决中文文件名上传乱码
//			fileUpload.setFileSizeMax(fileSizeMax); // 设置单个文件的大小
//			fileUpload.setSizeMax(sizeMax); // 设置表单中所有文件项的文件的总大小
			//解析request 返回list对象
			List<FileItem> list = fileUpload.parseRequest(request) ;
			//获得每个部分将便利的值存入map
			Map<String,String> map = new HashMap<String,String>() ;
			String fileName = null ;
			for(FileItem fileItem : list) {
				//判断文件项是普通项还是文件上传项
				if(fileItem.isFormField()) {
					// 普通项
					String name = fileItem.getFieldName() ;
					String value = fileItem.getString("UTF-8") ; // 解决普通项的中文乱码问题
					System.out.println(name + "    " + value);
					map.put(name, value) ;
				}else {
					// 文件上传项
					fileName = fileItem.getName() ;
					System.out.println("文件名：" + fileName);
					// 获得文件输入流
					InputStream is = fileItem.getInputStream() ;
					// 获得文件上传的路径
					String path = this.getServletContext().getRealPath("/products/1") ;
					OutputStream os = new FileOutputStream(path + "/" + fileName) ;
					int len = 0 ;
					byte[] b = new byte[1024] ;
					while((len = is.read(b)) != -1) {
						os.write(b, 0, len);
					}
					is.close();
					os.close();
				}
			}
			//封装数据
			BeanUtils.populate(product, map); 
			product.setPid(UUIDUtils.getUUID());
			product.setPdate(new Date());
			product.setPflag(0);
			product.setPimage("products/1/" + fileName);
			Category category = new Category() ;
			category.setCid(map.get("cid"));
			product.setCategory(category);
			
			//存入到数据库
			ProductService productService = (ProductService) BeanFactory.getBean("ProductService") ;
			productService.save(product) ;
			
			//页面跳转
			response.sendRedirect(request.getContextPath() + "/AdminProductServlet?method=findByPage&currPage=1");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
