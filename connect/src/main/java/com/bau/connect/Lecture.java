package com.bau.connect;
import java.util.ArrayList;

public class Lecture {
    User host;
    ArrayList<User> users;
    Chat chat;
    String name = "";
    String password = "";

    /* Add chat class here */
    Lecture(String name) {
        this.name = name;
        chat = new Chat();
		users = new ArrayList<>();
    }

    public void setHost(User host) {
        this.host = host;
    }

    public User getHost() {
        return this.host;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }
	
	public ArrayList<String> getUserNames(){
		ArrayList<String> temp = new ArrayList<>();
		users.forEach((u) -> {
			temp.add(u.getUsername());
		});
		return temp;
	}

    public void addUser(User u) {
        /* TODO edit attendance file */
        /* Maybe ask the lecturer for confirmation */
        this.users.add(u);
    }

    public void removeUser(String name) {
        this.users.removeIf(u -> (u.getUsername().equals(name)));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public Boolean checkPassword(String password) {
        return (this.password == null ? password == null : this.password.equals(password));
    }
}
