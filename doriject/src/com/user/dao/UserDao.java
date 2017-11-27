package com.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.user.domain.User;


public class UserDao {
	
	ConnectionMaker connectionMaker;
	
	
	public void setConnectionMaker(ConnectionMaker connectionMaker){
		this.connectionMaker = connectionMaker;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection c = connectionMaker.makeConnection();
		PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(ID, NAME, PASSWORD)VALUES(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
		
	}
	
	public User get(User user) throws ClassNotFoundException, SQLException {
		Connection c = connectionMaker.makeConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
		ps.setString(1, "odori");
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		User user2 = new User();
		
		user2.setId(rs.getString("id"));
		user2.setName(rs.getString("name"));
		user2.setPassword(rs.getString("password"));

		rs.close();
		ps.close();
		c.close();
		
		return user2;
	}
	
	
	public void delete(String id) throws ClassNotFoundException, SQLException {
		Connection c = connectionMaker.makeConnection();
		PreparedStatement ps = c.prepareStatement("DELETE FROM USERS WHERE ID = ?");
		ps.setString(1, id);
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}

}
