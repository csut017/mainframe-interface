package mainframe.client;

import mainframe.client.messages.customer.Search;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

/* This is a demonstration unit test to show how to use StubConnection to unit test a connection message. */
public class SimulateListCustomers {
    @Test
    void sendsToServer() throws IOException, InterruptedException {
        // Arrange
        StubConnection connection = new StubConnection();
        HashMap<String, String> values = new HashMap<>();
        values.put("total", "0");
        values.put("items", "0");
        MessageResponse response = new MessageResponse(
                new Status(1),
                values);
        connection.setResponse(response);

        // Act
        Search searchMessage = new Search(1, "Bob", 25);

        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, searchMessage.getTotal(), "total should be zero"),
                () -> Assertions.assertTrue(searchMessage.getItems().length == 0, "items should be empty")
        );
    }
}
