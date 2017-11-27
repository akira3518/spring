package com.user.web;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.user.dao.DaoFactory;
import com.user.dao.UserDao;
import com.user.domain.User;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		@SuppressWarnings("resource")
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		ApplicationContext context = new GenericXmlApplicationContext("com/context/applicationContext.xml");
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml", UserDaoTest.class);
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		
		user.setId("odori");
		user.setName("오준석");
		user.setPassword("2486");
		
		dao.add(user);
		
		System.out.println("등록!");
		
		User user2 = dao.get(user);
		
		System.out.println("이름" + user2.getName());
		System.out.println("비밀번호 : " + user2.getPassword());
		System.out.println(user2.getId() + "조회성공!");
		
		dao.delete(user2.getId());
		System.out.println("삭제~!");
		
	}
}
