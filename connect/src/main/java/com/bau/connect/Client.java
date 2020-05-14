import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;
    private Integer serverPort = 42328;
    private String serverAddress;

    public Client(String host) throws IOException, UnknownHostException{
        /*
         * Create a client socket, may throw IOException or UnknownHostException if
         * Socket(addr,port) doesnt succeed.
         */
        serverAddress = host;
        socket = new Socket(serverAddress, serverPort);
    }

}
