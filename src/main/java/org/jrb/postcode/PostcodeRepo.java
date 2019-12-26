package org.jrb.postcode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostcodeRepo {

	private final Connection connection;
	private final PreparedStatement pstmt;

	private final static String SQL = "SELECT latitude,longitude FROM Postcode_NL WHERE postcode = ?";
	private final static String q = "\"";
	private final static String JSON = "{'lat':'%s', 'lng': '%s'}".replaceAll("'", q);

	public PostcodeRepo() {
		try {
			this.connection = this.getConnection();
			this.pstmt = this.connection.prepareStatement(SQL);
		} catch (SQLException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public String getLocation(String postcode) throws SQLException {
		String pc = postcode.toUpperCase();
		if (pc.length() != 6) {
			return "Invalid postcode";
		}
		try {
			this.pstmt.setString(1, pc);
			ResultSet rs = this.pstmt.executeQuery();
			if (rs.next()) {
				return String.format(JSON, rs.getFloat(1), rs.getFloat(2)) ;
			} else {
				return "Postcode not found";
			}			
		} catch(Exception ex) {
			return ex.getMessage();
		}
	}

	private Connection getConnection() {
		try {
			String url = "jdbc:sqlite:d:/my-dev/dbs/postcodes/postcode.db";
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
