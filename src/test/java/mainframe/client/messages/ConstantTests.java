package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.constantCategory.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

public class ConstantTests {
    @Test
    void sendsToServer() throws IOException, InterruptedException {
        // login
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        connection.Login("admin", "admin");

        // create a new category
        Create createMessage = sendMessage(
                connection,
                new Create("test_" + Instant.now().toEpochMilli()),
                "create");
        long categoryId = createMessage.getId();

        // add an item
        sendMessage(
                connection,
                new AddItem(categoryId, "item", 1),
                "addItem");

        // list the categories
        mainframe.client.messages.constantItem.List listItemsMessage = sendMessage(
                connection,
                new mainframe.client.messages.constantItem.List(categoryId, 1, 20),
                "listItems");
        Assertions.assertTrue(listItemsMessage.getTotal() > 0, "Expected more than zero in the total");

        // delete an item
        sendMessage(
                connection,
                new DeleteItem(categoryId, "item"),
                "deleteItem");

        // update the category
        sendMessage(
                connection,
                new Update(categoryId, "updated_test_" + Instant.now().toEpochMilli()),
                "update");

        // list the categories
        List listMessage = sendMessage(
                connection,
                new List(1, 20),
                "list");
        Assertions.assertTrue(listMessage.getTotal() > 0, "Expected more than zero in the total");
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
