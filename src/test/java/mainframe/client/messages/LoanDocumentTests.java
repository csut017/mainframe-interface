package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.loan.AddDocument;
import mainframe.client.messages.loan.DeleteDocument;
import mainframe.client.messages.loan.List;
import mainframe.client.messages.loanDocument.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoanDocumentTests {
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

        // search for a loan
        List listLoansMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] loans = listLoansMessage.getItems();
        String loanNumber = loans[0].getLoanNumber();

        // add a document
        AddDocument addMessage = sendMessage(
                connection,
                new AddDocument("test.pdf", "application/pdf", loanNumber, 123, "Proof of Identity"),
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
        mainframe.client.messages.loanDocument.List listMessage = sendMessage(
                connection,
                new mainframe.client.messages.loanDocument.List(loanNumber, 1, 50),
                "list");
        Assertions.assertTrue(listLoansMessage.getItems().length > 0, "No items returned");

        // retrieve the document
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, loanNumber),
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
                new DeleteDocument(id, loanNumber),
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
