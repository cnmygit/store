package store.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BeanFactory {
	public static Object getBean(String id) {
		SAXReader reader = new SAXReader() ;
		try {
			Document document = reader.read(BeanFactory.class.getClassLoader().getResourceAsStream("applicationContext.xml")) ;
			Element beanEle = (Element) document.selectSingleNode("//bean[@id='"+id+"']") ;
			String value = beanEle.attributeValue("class") ;
			//反射获得实例对象
			Class clazz = Class.forName(value) ;
			final Object obj = clazz.newInstance() ;
			
			if(id.endsWith("Dao")) {
				Object proxyObj = Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
					
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//						System.out.println("权限校验");
						if(method.getName().startsWith("save")) {
							return method.invoke(obj, args) ;
						}
						return method.invoke(obj, args);
					}
				}) ;
				return proxyObj ;
			}
			return obj ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	/*public static void main(String[] args) {
		System.out.println(BeanFactory.getBean("UserDao"));
	}*/
}
