package model;

import java.util.ArrayList;

public class User {
	private String id;
	private String username;
	private String password;
	private String name;	
	private ArrayList<User> listFriend;
	
	public User() {
		super();
	}

	public User(String id, String username, String password, String name) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.listFriend = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<User> getListFriend() {
		return listFriend;
	}

	public void setListFriend(ArrayList<User> listFriend) {
		this.listFriend = listFriend;
	}
	
}
