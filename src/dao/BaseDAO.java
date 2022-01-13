package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDAO {
	private static String hostName = "localhost"; //127.0.0.1
    private static String password = "12345";
    private static String usename = "root";
    private static String database = "chat";

    public Connection getConnection() {
    	Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + database;
			conn = (Connection) DriverManager.getConnection(connectionURL, usename, password);
//			System.out.println("Connected!");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        return conn;
    }
    
    public void closeConnection(Connection conn, PreparedStatement ps, ResultSet rs) {
    	try {
    		if (rs != null) {
    			rs.close();
    		}
    		
    		if (ps != null) {
    			ps.close();
    		}
    		
    		if (conn != null) {
    			conn.close();
    		}
    		
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    }
    
//    public static void main(String[] args) {
//         DAO abc = new DAO();
//         abc.getConnection();
//    }
}
