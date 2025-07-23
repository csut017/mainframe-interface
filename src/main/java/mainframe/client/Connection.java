package mainframe.client;

import java.io.IOException;

// a connection to a mainframe
public interface Connection {
    // close the connection and clean up any resources
    void close() throws IOException;

    // checks if the connection can communicate with the remote destination
    boolean isConnected();

    // sends some data to the mainframe and receives a response
    MessageResponse send(MessageRequest request);

    // attempts to log in using a username and password
    Status Login(String username, String password) throws IOException;

    // logs out the current user
    void Logout();
}
