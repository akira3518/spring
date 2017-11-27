package com.user.dao_past;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.user.dao.ConnectionMaker;

public class DConnectionMaker implements ConnectionMaker {

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/ojs", "JUNSEOK", "2486");
	}

}
