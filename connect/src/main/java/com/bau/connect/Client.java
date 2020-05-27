package com.bau.connect;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
/* import javax.swing.text.Style; */
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements IOnMessage {

	static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	Socket socket;
	Integer serverPort = 42328;
	String serverAddress;
	Whiteboard board;
	String username;
	Chat chat;

	public Client(String host, String username, Whiteboard board) throws IOException, UnknownHostException {
		/*
         * Create a client socket, may throw IOException or UnknownHostException if
         * Socket(addr,port) doesnt succeed.
		 */
		serverAddress = host;
		this.username = username;
		this.board = board;
		socket = new Socket(serverAddress, serverPort);
		chat = new Chat();
	}

	Runnable listen = () -> {
		while (true) {
			try {
				var dis = new DataInputStream(socket.getInputStream());
				var data = dis.readUTF();
				Message msg = new Message(data);
				if (msg.operation.equals(Message.LECTURE_END)) {
					dis.close();
					return;
				}
				onMessage(msg, socket);
			} catch (IOException | InvalidMessageException e) {
				LOGGER.severe(e.toString());
			}
		}
	};

	void send(String msg) throws IOException {
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		dos.writeUTF(msg);
		
	}

	public Boolean joinLecture(String password) throws IOException {
		try {
			send(Message.lectureJoin(username, password));
			var dis = new DataInputStream(socket.getInputStream());
			var data = dis.readUTF();
			var msg = new Message(data);
			switch (msg.operation) {
			case Message.JOIN_FAILURE:
				return false;
			case Message.JOIN_SUCCESS:
				var t = new Thread(listen);
				t.start();
				return true;
			default:
				throw new InvalidMessageException(msg.operation
						+ " Can not determine if we are able to join");
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Can not establish connection with the class:", e);
			throw e;
		} catch (InvalidMessageException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
			return false;
		}
	}
	
	public void chatWrite(String text){
		var msg = Message.chat(username, text);
		for (var i = 0; i < 3; i++){
			try {
				send(msg);
				break;
			} catch (IOException e){
				LOGGER.log(Level.SEVERE, e.toString());
				if (i == 2){
					LOGGER.warning("Can not send chat message");
				}
			}
		}
	}
	
	public void raiseHand(){
		var msg = Message.raiseHand(username);
		try {
			send(msg);
		} catch (IOException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void onMessage(Message message, Socket remote) {
		var args = message.getarguments();
		switch (message.operation) {
		case Message.CHAT:
			if (!args.get(0).equals(username)){
				chat.addEntry(args.get(0), args.get(1));
			}
			break;
		case Message.ADDSHAPE:
			switch (args.get(1)) {
			case Message.RECT:
				var id = Integer.valueOf(args.get(0));
				var c = new Color(Integer.valueOf(args.get(2)));
				var t = args.get(3).split(",");
				var x = Integer.valueOf(t[0]);
				var y = Integer.valueOf(t[1]);
				var w = Integer.valueOf(t[2]);
				var h = Integer.valueOf(t[3]);
				board.addRect(id, c, x, y, w, h);
				break;

			case Message.OVAL:
				id = Integer.valueOf(args.get(0));
				c = new Color(Integer.valueOf(args.get(2)));
				t = args.get(3).split(",");
				x = Integer.valueOf(t[0]);
				y = Integer.valueOf(t[1]);
				w = Integer.valueOf(t[2]);
				h = Integer.valueOf(t[3]);
				board.addOval(id, c, x, y, w, h);
				break;
			case Message.LINE:
				id = Integer.valueOf(args.get(0));
				c = new Color(Integer.valueOf(args.get(2)));
				t = args.get(3).split(",");
				var x1 = Integer.valueOf(t[0]);
				var y1 = Integer.valueOf(t[1]);
				var x2 = Integer.valueOf(t[2]);
				var y2 = Integer.valueOf(t[3]);
				board.addLine(id, c, x1, y1, x2, y2);
				break;
			case Message.POLYGON:
				id = Integer.valueOf(args.get(0));
				c = new Color(Integer.valueOf(args.get(2)));
				var _x = args.get(3).split(",");
				ArrayList<Integer> xVector = new ArrayList<>();
				for (var i : _x) {
					xVector.add(Integer.valueOf(i));
				}
				var _y = args.get(4).split(",");
				ArrayList<Integer> yVector = new ArrayList<>();
				for (var i : _y) {
					yVector.add(Integer.valueOf(i));
				}
				board.addPolygon(id, c, xVector, yVector);
				break;
			case Message.TEXT:
				id = Integer.valueOf(args.get(0));
				c = new Color(Integer.valueOf(args.get(2)));
				var style = args.get(3);
				var size = Integer.valueOf(args.get(4));
				t = args.get(5).split(",");
				x = Integer.valueOf(t[0]);
				y = Integer.valueOf(t[1]);
				var text = args.get(6);
				board.addText(id, c, style, size, x, y, text);
				break;
			default:
				break;
			}
			break;
		default:
			LOGGER.log(Level.WARNING, "Invalid operation code: {0} ", message.operation);
			break;

		}

	}

}
