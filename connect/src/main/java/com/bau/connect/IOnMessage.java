package com.bau.connect;
import java.net.Socket;

public interface IOnMessage {
    void onMessage(Message message, Socket remote);
}
