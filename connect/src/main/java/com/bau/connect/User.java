package com.bau.connect;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    InetAddress address;
    String username;
	public Socket socket;

    public InetAddress getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
	
	public void close(){
		try {
			socket.close();
		} catch (IOException ex) {
			Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

    User(Socket s, String username) {
        this.socket = s;
		this.address = s.getInetAddress();
        this.username = username;
    }
}
