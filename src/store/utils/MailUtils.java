package store.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

/**
 * 发送激活邮件的工具类
 * @author Leo
 *
 */
public class MailUtils {
	public static void sendMail(String to, String code) {
		//获得连接
		Properties props = new Properties() ;
		Session session = Session.getInstance(props, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication("service@store.com","111");
			}
			
		}) ;
		try{
			//构建邮件
			Message message = new MimeMessage(session) ;
			message.setFrom(new InternetAddress("service@store.com"));
			//收件人
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			//主题
			message.setSubject("来自黑马官方商城的激活邮件");
			//正文
			message.setContent("<h1>来自黑马官方商城的激活邮件：请点击下面链接激活</h1><h1><a href='http://localhost:8080/store_v2.0/UserServlet?method=active&code="+code+"'>http://localhost:8080/store_v2.0/UserServlet?method=active&code="+code+"</a></h1>", "text/html;charset=UTF-8") ;
			//发送邮件
			Transport.send(message);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
