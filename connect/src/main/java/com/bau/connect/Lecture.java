package com.bau.connect;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

public class Lecture {

	User host;
	ArrayList<User> users;
	Chat chat;
	String name = "";
	String password = "";
	public DefaultListModel<String> participants = null; // list for gui to show

	/* Add chat class here */
	Lecture(String name) {
		this.name = name;
		chat = new Chat();
		users = new ArrayList<>();
		participants = new DefaultListModel<>();
		try {
			Files.deleteIfExists(Paths.get("attendance.txt"));
			Files.write(Paths.get("attendance.txt"), ("Start of lecture " + name + "\n").getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.CREATE);
		} catch (IOException ex) {
			Logger.getLogger(Lecture.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void setHost(User host) {
		this.host = host;
	}

	public User getHost() {
		return this.host;
	}

	public void setTeacher(String t) {
		participants.add(0, t + " (Teacher)");
	}

	public ArrayList<User> getUsers() {
		return this.users;
	}

	public ArrayList<String> getUserNames() {
		ArrayList<String> temp = new ArrayList<>();
		users.forEach((u) -> {
			temp.add(u.getUsername());
		});
		return temp;
	}

	public void addUser(User u) {
		String attendance = u.username + "\t" + Date.from(Instant.now()).toString() + "\n";
		try {
			Files.write(Paths.get("attendance.txt"), attendance.getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.APPEND);
		} catch (IOException ex) {
			Logger.getLogger(Lecture.class.getName()).log(Level.SEVERE, null, ex);
		}
		participants.addElement(u.username + " (Student)");
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
