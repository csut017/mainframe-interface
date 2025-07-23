package mainframe.client;

import java.io.IOException;

// a message that can be sent to the mainframe
public interface Message {
    // sends the message to the mainframe.
    Status send(Connection connection)
            throws IOException;
}
