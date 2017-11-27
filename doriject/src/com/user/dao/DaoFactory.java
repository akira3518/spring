package com.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class DaoFactory {

	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource(); 
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/ojs");
		dataSource.setUsername("JUNSEOK");
		dataSource.setPassword("2486");
		
		return dataSource; 
		
	}
	
	
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setDataSource(dataSource());;
		return userDao;
	}
}
