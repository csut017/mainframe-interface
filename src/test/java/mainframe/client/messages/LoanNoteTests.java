package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.loanNote.Retrieve;
import mainframe.client.messages.loan.AddNote;
import mainframe.client.messages.loan.DeleteNote;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.loan.UpdateNote;
import mainframe.client.messages.loan.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoanNoteTests {
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

        // add a note
        AddNote addMessage = sendMessage(
                connection,
                new AddNote("Line 1\nLine 2", loanNumber,"Test note"),
                "add");
        long id = addMessage.getId();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals("test", addMessage.getAuthor(), "author does not match"),
                () -> Assertions.assertEquals("Line 1\nLine 2", addMessage.getLines(), "lines does not match"),
                () -> Assertions.assertEquals("Test note", addMessage.getType(), "type does not match")
        );

        // list the notes
        mainframe.client.messages.loanNote.List listMessage = sendMessage(
                connection,
                new mainframe.client.messages.loanNote.List(loanNumber, 1, 50),
                "list");
        Assertions.assertTrue(listLoansMessage.getItems().length > 0, "No items returned");

        // retrieve the note
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, loanNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals("test", retrieveMessage.getAuthor(), "author does not match"),
                () -> Assertions.assertEquals("Line 1\nLine 2", retrieveMessage.getLines(), "lines does not match"),
                () -> Assertions.assertEquals("Test note", retrieveMessage.getType(), "type does not match")
        );

        // update the note
        UpdateNote updateMessage = sendMessage(
                connection,
                new UpdateNote(id, "First Line\nSecond Line", loanNumber, "Yet Another Note"),
                "update");
        Assertions.assertAll(
                () -> Assertions.assertEquals("test", updateMessage.getAuthor(), "author does not match"),
                () -> Assertions.assertEquals("First Line\nSecond Line", updateMessage.getLines(), "lines does not match"),
                () -> Assertions.assertEquals("Yet Another Note", updateMessage.getType(), "type does not match")
        );

        // delete the note
        DeleteNote deleteMessage = sendMessage(
                connection,
                new DeleteNote(id, loanNumber),
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
