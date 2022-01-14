package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.User;

public class UserDAO extends BaseDAO {

	public User getUserByUsername(String username, String password) {
		Connection connection = getConnection();
        String sql = "SELECT * FROM User WHERE Username = ? AND Password = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
        	pstmt = connection.prepareStatement(sql);
        	pstmt.setString(1, username);
        	pstmt.setString(2, password);
        	rs = pstmt.executeQuery();
        	
        	User user = null;
        	
        	if (rs.next()) {
        		user = new User();
        		user.setId(rs.getString("Id"));
        		user.setPassword(rs.getString("Password"));
        		user.setUsername(rs.getString("Username"));
        		user.setName(rs.getString("Name"));
        	} 
        	
        	return user;

        } catch (SQLException e) {	
        	e.printStackTrace();
        } finally {
        	closeConnection(connection, pstmt, rs);
        }
        
        return null;
	}
	
	public User getUserByUsername(String username) {
		Connection connection = getConnection();
        String sql = "SELECT * FROM User WHERE Username = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
        	pstmt = connection.prepareStatement(sql);
        	pstmt.setString(1, username);
        	rs = pstmt.executeQuery();
        	
        	User user = null;
        	
        	if (rs.next()) {
        		user = new User();
        		user.setId(rs.getString("Id"));
        		user.setPassword(rs.getString("Password"));
        		user.setUsername(rs.getString("Username"));
        		user.setName(rs.getString("Name"));
        	} 
        	
        	return user;

        } catch (SQLException e) {	
        	e.printStackTrace();
        } finally {
        	closeConnection(connection, pstmt, rs);
        }
        
        return null;
	}
	
	public User getUserById(String id) {
		Connection connection = getConnection();
        String sql = "SELECT * FROM User WHERE Id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
        	pstmt = connection.prepareStatement(sql);
        	pstmt.setString(1, id);
        	rs = pstmt.executeQuery();
        	
        	User user = null;
        	
        	if (rs.next()) {
        		user = new User();
        		user.setId(rs.getString("Id"));
        		user.setPassword(rs.getString("Password"));
        		user.setUsername(rs.getString("Username"));
        		user.setName(rs.getString("Name"));
        	} 
        	
        	return user;

        } catch (SQLException e) {	
        	e.printStackTrace();
        } finally {
        	closeConnection(connection, pstmt, rs);
        }
        
        return null;
	}
	
	public ArrayList<User> getFriendUser(String idUser) {
		Connection connection = getConnection();
        String sql = "SELECT * FROM FriendUser WHERE IdUser = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        ArrayList<User> returnList = new ArrayList<>();

        try {
        	pstmt = connection.prepareStatement(sql);
        	pstmt.setString(1, idUser);
        	rs = pstmt.executeQuery();
        	
        	User user = null;
        	
        	while (rs.next()) {
        		user = getUserById(rs.getString("IdFriend"));
        		
        		returnList.add(user);
        	} 
        	
        	return returnList;

        } catch (SQLException e) {	
        	e.printStackTrace();
        } finally {
        	closeConnection(connection, pstmt, rs);
        }
        
        return null;
	}

	public int addFriend(String idUser, String name) {
		User friend = getUserByUsername(name);
		if (friend != null) {
			Connection connection = getConnection();
	        String sql = "INSERT INTO FriendUser VALUES (?, ?)";
	        PreparedStatement pstmt = null;

	        try {
	        	pstmt = connection.prepareStatement(sql);
	        	pstmt.setString(1, idUser);
	        	pstmt.setString(2,friend.getId());
	        	pstmt.executeUpdate();
	        	
	        	return 0;

	        } catch (SQLException e) {	
	        	e.printStackTrace();
	        } finally {
	        	closeConnection(connection, pstmt, null);
	        }
	        
	        return 2;
		} else {
			return 1;
		}
		
	}
	
	public int isFriend(String usernameUser, String usernameFriend) {
		int kt = 0;
		String idUser = getUserByUsername(usernameUser).getId();
		String idFriend = getUserByUsername(usernameFriend).getId();
		Connection connection = getConnection();
        String sql = "select * from frienduser where IdUser = ? and IdFriend = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
        	pstmt = connection.prepareStatement(sql);
        	pstmt.setString(1, idUser);
        	pstmt.setString(2,idFriend);
        	rs = pstmt.executeQuery();
            if(rs.next()) {
            	kt = 1;
            }
        } catch (SQLException e) {	
        	e.printStackTrace();
        } finally {
        	closeConnection(connection, pstmt, rs);
        }
        
        return kt;
	}

}

