package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.*;
import mainframe.client.messages.customerEmail.List;
import mainframe.client.messages.customerEmail.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CustomerEmailTests {
    @Test
    void sendsToServer() throws IOException, InterruptedException {
        // login
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        connection.Login("test", "test");

        // search for a customer
        Search searchMessage = sendMessage(
                connection,
                new Search(1, "Smith", 25),
                "search");
        Search.Item[] customers = searchMessage.getItems();
        Assertions.assertNotNull(customers.length > 0, "No customers returned");
        String customerNumber = customers[0].getNumber();

        // add an email
        AddEmail addMessage = sendMessage(
                connection,
                new AddEmail("test@test.com", customerNumber, true, null, AddEmail.TYPE_PERSONAL),
                "add");
        long id = addMessage.getId();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals("test@test.com", addMessage.getAddress(), "address does not match"),
                () -> Assertions.assertEquals(true, addMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals(AddEmail.TYPE_PERSONAL, addMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals(null, addMessage.getOtherType(), "other type does not match")
        );

        // list the emails
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] items = listMessage.getItems();
        Assertions.assertTrue(items.length > 0, "No items returned");

        // retrieve the email
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, customerNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals("test@test.com", retrieveMessage.getAddress(), "address does not match"),
                () -> Assertions.assertEquals(true, retrieveMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals(AddEmail.TYPE_PERSONAL, retrieveMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals(null, retrieveMessage.getOtherType(), "other type does not match")
        );

        // update the email
        UpdateEmail updateMessage = sendMessage(
                connection,
                new UpdateEmail("new@test.com", customerNumber, id, false, "Something", UpdateEmail.TYPE_OTHER),
                "update");
        Assertions.assertAll(
                () -> Assertions.assertEquals("new@test.com", updateMessage.getAddress(), "address does not match"),
                () -> Assertions.assertEquals(false, updateMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals(UpdateEmail.TYPE_OTHER, updateMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals("Something", updateMessage.getOtherType(), "other type does not match")
        );

        // delete the email
        DeleteEmail deleteMessage = sendMessage(
                connection,
                new DeleteEmail(customerNumber, id),
                "delete");
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
