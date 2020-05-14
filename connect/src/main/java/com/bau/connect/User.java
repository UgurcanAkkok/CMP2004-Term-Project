import java.net.InetAddress;

public class User {
    InetAddress address;
    String username;

    public InetAddress getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    User(InetAddress address, String username) {
        this.address = address;
        this.username = username;
    }
}
