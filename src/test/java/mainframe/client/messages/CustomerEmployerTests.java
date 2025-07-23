package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.AddEmployer;
import mainframe.client.messages.customer.DeleteEmployer;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.customer.UpdateEmployer;
import mainframe.client.messages.customerEmployer.List;
import mainframe.client.messages.customerEmployer.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CustomerEmployerTests {
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

        // add an employer
        AddEmployer addMessage = sendMessage(
                connection,
                new AddEmployer(customerNumber, "Joe's Widgets", "Test Dummy", 1234.56, AddEmployer.TYPE_PRIMARY),
                "add");
        long id = addMessage.getId();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals("Joe's Widgets", addMessage.getName(), "name does not match"),
                () -> Assertions.assertEquals("Test Dummy", addMessage.getPosition(), "position does not match"),
                () -> Assertions.assertEquals(1234.56, addMessage.getSalary(), "salary does not match"),
                () -> Assertions.assertEquals(AddEmployer.TYPE_PRIMARY, addMessage.getType(), "type does not match")
        );

        // list the employers
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] items = listMessage.getItems();
        Assertions.assertTrue(items.length > 0, "No items returned");

        // retrieve the employer
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, customerNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals("Joe's Widgets", retrieveMessage.getName(), "name does not match"),
                () -> Assertions.assertEquals("Test Dummy", retrieveMessage.getPosition(), "position does not match"),
                () -> Assertions.assertEquals(1234.56, retrieveMessage.getSalary(), "salary does not match"),
                () -> Assertions.assertEquals(AddEmployer.TYPE_PRIMARY, retrieveMessage.getType(), "type does not match")
        );

        // update the employer
        UpdateEmployer updateMessage = sendMessage(
                connection,
                new UpdateEmployer(customerNumber, id, "Jane's Widgets", "Scrap Dummy", 543.21, UpdateEmployer.TYPE_SECONDARY),
                "update");
        Assertions.assertAll(
                () -> Assertions.assertEquals("Jane's Widgets", updateMessage.getName(), "name does not match"),
                () -> Assertions.assertEquals("Scrap Dummy", updateMessage.getPosition(), "position does not match"),
                () -> Assertions.assertEquals(543.21, updateMessage.getSalary(), "salary does not match"),
                () -> Assertions.assertEquals(AddEmployer.TYPE_SECONDARY, updateMessage.getType(), "type does not match")
        );

        // delete the employer
        DeleteEmployer deleteMessage = sendMessage(
                connection,
                new DeleteEmployer(customerNumber, id),
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
