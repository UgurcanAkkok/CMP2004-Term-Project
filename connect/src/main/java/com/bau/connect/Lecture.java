package com.bau.connect;
import java.util.List;

public class Lecture {
    User host;
    List<User> users;
    Chat chat;
    String name = "";
    String password = "";

    /* Add chat class here */
    Lecture(String name) {
        this.name = name;
        chat = new Chat();
    }

    public void setHost(User host) {
        this.host = host;
    }

    public User getHost() {
        return this.host;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void addUser(User u) {
        /* TODO edit attendance file */
        /* Maybe ask the lecturer for confirmation */
        this.users.add(u);
    }

    public void removeUser(String name) {
        this.users.removeIf(u -> (u.getUsername() == name));
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
        return this.password == password;
    }
}
