package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.AddRelationship;
import mainframe.client.messages.customer.DeleteRelationship;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.customer.UpdateRelationship;
import mainframe.client.messages.customerRelationship.List;
import mainframe.client.messages.customerRelationship.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CustomerRelationshipTests {
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
        Assertions.assertNotNull(customers.length > 2, "No customers returned");
        String customerNumber = customers[0].getNumber();
        String secondaryNumber = customers[1].getNumber();
        String tertiaryNumber = customers[1].getNumber();

        // add a relationship
        AddRelationship addMessage = sendMessage(
                connection,
                new AddRelationship(customerNumber, null, secondaryNumber, AddRelationship.TYPE_SPOUSE),
                "add");
        long id = addMessage.getId();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals(null, addMessage.getOtherType(), "other type does not match"),
                () -> Assertions.assertEquals(secondaryNumber, addMessage.getSecondaryCustomerNumber(), "secondary does not match"),
                () -> Assertions.assertEquals(AddRelationship.TYPE_SPOUSE, addMessage.getType(), "type does not match")
        );

        // list the relationships
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] items = listMessage.getItems();
        Assertions.assertTrue(items.length > 0, "No items returned");

        // retrieve the relationship
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, customerNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals(null, retrieveMessage.getOtherType(), "other type does not match"),
                () -> Assertions.assertEquals(secondaryNumber, retrieveMessage.getSecondaryCustomerNumber(), "secondary does not match"),
                () -> Assertions.assertEquals(AddRelationship.TYPE_SPOUSE, retrieveMessage.getType(), "type does not match")
        );

        // update the relationship
        UpdateRelationship updateMessage = sendMessage(
                connection,
                new UpdateRelationship(customerNumber, id, "Missing", tertiaryNumber, UpdateRelationship.TYPE_OTHER),
                "update");
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals("Missing", updateMessage.getOtherType(), "other type does not match"),
                () -> Assertions.assertEquals(tertiaryNumber, updateMessage.getSecondaryCustomerNumber(), "secondary does not match"),
                () -> Assertions.assertEquals(AddRelationship.TYPE_OTHER, updateMessage.getType(), "type does not match")
        );

        // delete the relationship
        DeleteRelationship deleteMessage = sendMessage(
                connection,
                new DeleteRelationship(customerNumber, id),
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
