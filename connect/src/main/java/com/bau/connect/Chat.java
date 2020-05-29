package com.bau.connect;
import java.util.ArrayList;
import javax.swing.JTextArea;

public class Chat {
	public String chatText = "";
	JTextArea chatScreen = null;
    public class ChatMessage {
        public String username;
        public String body;

        ChatMessage(String u, String body) {
            this.username = u;
            this.body = body;
        }
    }
	
    ArrayList<ChatMessage> entry;

    public Chat() {
		entry = new ArrayList<>();
        entry.add(new ChatMessage("System", "This is the start of an engaging class.."));
		chatText = "System:\nThis is the start of an engaging class..\n";
	}

	public void setChatScreen(JTextArea chatArea){
		this.chatScreen = chatArea;
	}
    public void addEntry(String name, String body) {
        entry.add(new ChatMessage(name, body));
		chatText = chatText + name + ":" + body + "\n";
		if (chatScreen != null){
			chatScreen.setText(chatText);
		}
    }
}
