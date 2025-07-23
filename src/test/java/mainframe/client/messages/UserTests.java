package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.user.Create;
import mainframe.client.messages.user.List;
import mainframe.client.messages.user.Retrieve;
import mainframe.client.messages.user.Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

public class UserTests {
    @Test
    void sendsToServer() throws IOException, InterruptedException {
        // login
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        connection.Login("admin", "admin");

        // create a new user
        String name = "test_" + Instant.now().toEpochMilli();
        Create createMessage = sendMessage(
                connection,
                new Create(name, name, 2, name, 0),
                "create");
        long userId = createMessage.getId();

        // update the user
        String newName = "updated_test_" + Instant.now().toEpochMilli();
        sendMessage(
                connection,
                new Update(Long.toString(userId), newName, newName, 3, newName, 0),
                "update");

        // retrieve the user
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(userId),
                "retrieve");
        Assertions.assertEquals(userId, retrieveMessage.getId());
        Assertions.assertEquals(newName, retrieveMessage.getName());
        Assertions.assertEquals(newName, retrieveMessage.getLogin());
        Assertions.assertEquals("3", retrieveMessage.getOrganisation());

        // list the users
        List listMessage = sendMessage(
                connection,
                new List("2", 1, 25),
                "list");
        Assertions.assertTrue(listMessage.getItems().length > 0);
    }

    private <T extends Message> T sendMessage(Connection connection, T message, String name) throws IOException, InterruptedException {
        Status status = message.send(connection);
        Thread.sleep(500);     // give the server time to apply the updates

        Assertions.assertAll(
                () -> Assertions.assertTrue(status.getWasSuccessful(), name + " message failed"),
                () -> Assertions.assertNull(status.getErrorMessage(), name + " message failed")
        );

        return message;
    }
}

