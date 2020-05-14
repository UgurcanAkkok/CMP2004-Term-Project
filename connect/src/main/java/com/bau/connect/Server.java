package com.bau.connect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server implements IOnMessage {

	ServerSocket socket;
	Integer port = 42328;
	Lecture lecture;
	static final Logger LOGGER = Logger.getLogger(Server.class.getName());

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
					client.close();
				} catch (IOException | InvalidMessageException e) {
					LOGGER.log(Level.WARNING, e.toString());
					continue;
				}
			}

		}
	};

	public Server() throws IOException {
		socket = new ServerSocket(port);
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
		var inet = client.getInetAddress();
		List<String> args;
		String username;
		switch (message.operation) {
		case Message.LECTURE_JOIN:
			username = message.getarguments().get(0);
			var pass = message.arguments.get(1);
			if (lecture.checkPassword(pass)) {
				lecture.addUser(new User(inet, username));
			} else {
				try {
					try ( java.io.DataOutputStream stream = new DataOutputStream(client.getOutputStream())) {
						stream.writeUTF(Message.joinFailure(username));
					}
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Cant inform the client about the invalid password");
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
			break;
		case Message.INVALID_MESSAGE:
			var msg = message.getarguments();
			LOGGER.log(Level.SEVERE, "This is an invalid message: {0}", msg);
		default:
			LOGGER.log(Level.WARNING, "Invalid operation code: {0} ", message.operation);
			break;
		}
	}

	/* private void sendMessage(Socket client */
}
