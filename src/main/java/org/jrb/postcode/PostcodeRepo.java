package org.jrb.postcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostcodeRepo {

	private Connection  connection;
	private final static String SQL = "SELECT lat,lon FROM Postcode_NL WHERE postcode = ?";
	
	public Float[] getLocation(String postcode) {
//		this.getConnection().
		return null;
	}
	
	private Connection getConnection() {
		if (this.connection == null) {
			try {
				String url = "jdbc:sqlite:d:/my-dev/dbs/postcodes/postcode.db";
				this.connection = DriverManager.getConnection(url);
				System.out.println("Connection to SQLite has been established.");
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return this.connection;
	}
}
