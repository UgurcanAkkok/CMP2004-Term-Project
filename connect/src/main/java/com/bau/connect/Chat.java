package com.bau.connect;
import java.util.ArrayList;

public class Chat {
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
    }

    public void addEntry(String name, String body) {
        entry.add(new ChatMessage(name, body));
    }
}
