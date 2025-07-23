package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.loan.AddCoborrower;
import mainframe.client.messages.loan.DeleteCoborrower;
import mainframe.client.messages.loan.List;
import mainframe.client.messages.loanCoborrower.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoanCoborrowerTests {
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
        Assertions.assertNotNull(customers.length > 1, "No customers returned");
        String customerNumber = customers[0].getNumber();
        String secondaryNumber = customers[1].getNumber();

        // search for a loan
        List listLoansMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] loans = listLoansMessage.getItems();
        String loanNumber = loans[0].getLoanNumber();

        // add a coborrower
        AddCoborrower addMessage = sendMessage(
                connection,
                new AddCoborrower(secondaryNumber, loanNumber, AddCoborrower.TYPE_COBORROWER),
                "add");
        long id = addMessage.getId();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals(secondaryNumber, addMessage.getCoborrowerNumber(), "coborrower number does not match"),
                () -> Assertions.assertEquals(AddCoborrower.TYPE_COBORROWER, addMessage.getType(), "type does not match")
        );

        // list the coborrowers
        mainframe.client.messages.loanCoborrower.List listMessage = sendMessage(
                connection,
                new mainframe.client.messages.loanCoborrower.List(loanNumber, 1, 50),
                "list");
        Assertions.assertTrue(listLoansMessage.getItems().length > 0, "No items returned");

        // retrieve the coborrower
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, loanNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals(secondaryNumber, retrieveMessage.getCoborrowerNumber(), "coborrower number does not match"),
                () -> Assertions.assertEquals(AddCoborrower.TYPE_COBORROWER, retrieveMessage.getType(), "type does not match")
        );

        // delete the coborrower
        DeleteCoborrower deleteMessage = sendMessage(
                connection,
                new DeleteCoborrower(id, loanNumber),
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
