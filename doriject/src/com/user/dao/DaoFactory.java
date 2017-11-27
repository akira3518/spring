package com.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class DaoFactory {

//	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(getConnectionMaker());;
		return userDao;
	}
	
	public ConnectionMaker getConnectionMaker() {
		return new DConnectionMaker();
	}
}
