package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.AddDocument;
import mainframe.client.messages.customer.DeleteDocument;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.customerDocument.List;
import mainframe.client.messages.customerDocument.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CustomerDocumentTests {
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

        // add a document
        AddDocument addMessage = sendMessage(
                connection,
                new AddDocument(customerNumber, "test.pdf", "application/pdf", 123, "Proof of Identity"),
                "add");
        long id = addMessage.getId();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals("application/pdf", addMessage.getFileType(), "file type does not match"),
                () -> Assertions.assertEquals(123, addMessage.getSize(), "size does not match"),
                () -> Assertions.assertEquals("test.pdf", addMessage.getFileName(), "file name does not match"),
                () -> Assertions.assertEquals("Proof of Identity", addMessage.getType(), "type does not match")
        );

        // list the documents
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] items = listMessage.getItems();
        Assertions.assertTrue(items.length > 0, "No items returned");

        // retrieve the document
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, customerNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals("application/pdf", retrieveMessage.getFileType(), "file type does not match"),
                () -> Assertions.assertEquals(123, retrieveMessage.getSize(), "size does not match"),
                () -> Assertions.assertEquals("test.pdf", retrieveMessage.getFileName(), "file name does not match"),
                () -> Assertions.assertEquals("Proof of Identity", retrieveMessage.getType(), "type does not match")
        );

        // delete the document
        DeleteDocument deleteMessage = sendMessage(
                connection,
                new DeleteDocument(customerNumber, id),
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
