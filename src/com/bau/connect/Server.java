import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket socket;
    Integer port = 42328;
    static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    Runnable serve = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Socket client = socket.accept();
                    // TODO Do what the client requested.
                    DataInputStream stream = new DataInputStream(
                            client.getInputStream());
                    String msgString = (String) stream.readUTF();
                    try {
                        // MessageHandler.onMessage(msg);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, e.toString());
                    }
                    client.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, e.toString());
                    continue;
                }
            }

        }
    };

    public Server() throws IOException {
        socket = new ServerSocket(port);
    }

}
