package com.bau.connect;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Server implements IOnMessage {

	ServerSocket socket;
	Integer port = 42328;
	Lecture lecture;
	static final Logger LOGGER = Logger.getLogger(Server.class.getName());

	public Server() throws IOException {
		socket = new ServerSocket(port);
	}

	Runnable serve = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Socket client = socket.accept();
					DataInputStream stream = new DataInputStream(client.getInputStream());
					String msgString = (String) stream.readUTF();
					var msg = new Message(msgString);
					onMessage(msg, client);
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, e.toString());
				} catch (InvalidMessageException ex) {
					LOGGER.log(Level.SEVERE, null, ex);
				}
			}

		}
	};

	public void run(){
		Thread serveThread = new Thread(serve);
		serveThread.start();
	}
	
	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}

	public void informInvalidMessage(Message msg, Socket client) {
		var pckt = Message.invalidMessage(msg);
		try {
			var os = new DataOutputStream(client.getOutputStream());
		} catch (IOException ex) {
			LOGGER.log(Level.WARNING, "Can not inform about invalid message", ex);
		}
	}

	/* private void sendMessage(Socket client, Message */
	@Override
	public void onMessage(Message message, Socket client) {
		List<String> args;
		String username;
		switch (message.operation) {
		case Message.LECTURE_JOIN:
			username = message.getarguments().get(0);
			var pass = message.arguments.get(1);
			if (lecture.checkPassword(pass)) {
				lecture.addUser(new User(client, username));
				try {
					var stream = new DataOutputStream(client.getOutputStream());
					stream.writeUTF(Message.joinSuccess(username));
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Cant inform the client about the correct password", e);
				}
			} else {
				try {
					java.io.DataOutputStream stream = new DataOutputStream(client.getOutputStream());
					stream.writeUTF(Message.joinFailure(username));
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Cant inform the client about the invalid password", e);
				}
			}
			break;
		case Message.LECTURE_LEAVE:
			username = message.getarguments().get(0);
			lecture.removeUser(username);
			break;
		case Message.CHAT:
			args = message.getarguments();
			username = args.get(0);
			var body = String.join(Message.SEPERATOR, args.subList(1, args.size()));
			lecture.chat.addEntry(username, body);
			/* TODO: send chat messages back to all users */
			break;
		case Message.INVALID_MESSAGE:
			var msg = message.getarguments();
			LOGGER.log(Level.SEVERE, "This is an invalid message we sent: {0}", msg);
		case Message.RAISE_HAND:
			username = message.getarguments().get(0);
			JOptionPane.showMessageDialog(null, username + " raises hand!");
		default:
			LOGGER.log(Level.WARNING, "Invalid operation code: {0} ", message.operation);
			break;
		}
	}

	/* private void sendMessage(Socket client */
	public void sendPolygon(Integer id, Color c, ArrayList<Integer> x, ArrayList<Integer> y) {
		broadCast(Message.shapePolygon(id, c, x, y));
	}

	public void sendText(Integer id, Color c, String s, Integer i, Integer x, Integer y, String text) {
		broadCast(Message.shapeText(id, c, s, i, x, y, text));
	}

	public void sendLine(Integer id, Color c, Integer x1, Integer y1, Integer x2, Integer y2) {
		broadCast(Message.shapeLine(id, c, x1, y1, x2, y2));
	}

	public void sendRect(Integer id, Color c, Integer x, Integer y, Integer w, Integer h) {
		broadCast(Message.shapeRect(id, c, x, y, w, h));
	}

	public void sendOval(Integer id, Color c, Integer x, Integer y, Integer w, Integer h) {
		broadCast(Message.shapeOval(id, c, x, y, w, h));
	}

	public void broadCast(String msg) {
		lecture.getUsers().forEach((User u) -> {
			try {
				DataOutputStream dos = new DataOutputStream(u.socket.getOutputStream());
				dos.writeUTF(msg);
			} catch (IOException ex) {
				LOGGER.log(Level.SEVERE, null, ex);
			}
		});
	}
}
