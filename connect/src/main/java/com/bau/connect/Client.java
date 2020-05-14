package com.bau.connect;

import java.awt.Color;
/* import javax.swing.text.Style; */
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements IOnMessage {
    static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    Socket socket;
    Integer serverPort = 42328;
    String serverAddress;
    Whiteboard board;
    

    public Client(String host) throws IOException, UnknownHostException {
        /*
         * Create a client socket, may throw IOException or UnknownHostException if
         * Socket(addr,port) doesnt succeed.
         */
        serverAddress = host;
        socket = new Socket(serverAddress, serverPort);
    }

    @Override
    public void onMessage(Message message, Socket remote) {
        var args = message.getarguments();
        switch (message.operation) {
        case Message.ADDSHAPE:
            switch (args.get(1)) {
                case Message.RECT:
                    var c = new Color(Integer.valueOf(args.get(2)));
					var t = args.get(3).split(",");
					var x = Float.valueOf(t[0]);
					var y = Float.valueOf(t[1]); 
					var w = Float.valueOf(t[2]);
					var h = Float.valueOf(t[3]);
                    board.drawRect(c, x, y, w, h);
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
