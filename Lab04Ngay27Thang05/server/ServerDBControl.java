package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 
 * Lop cai dat cac ham tim trong CSDL de: (i) kiem tra dang nhap
 * (ii) cap nhat trang thai
 *
 */
public class ServerDBControl {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
	private static final String USER = "root";
	private static final String PASS = "ukr@2402";
	private static final String QUERY = "SELECT status FROM Users WHERE ";
	
	/*
	 * Ham getAllUsers thuc hien viec lay toan bo cac Users cua he thong
	 */
	public static ArrayList<User> getAllUsers(){
		String tempQuery = "SELECT * from Users";
		ArrayList<User> result = new ArrayList<User>();
		
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(tempQuery);
		) {		
			while(rs.next()){
				 result.add(new User(rs.getString(1), rs.getString(2), rs.getString(3)));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return result;
	}
}
