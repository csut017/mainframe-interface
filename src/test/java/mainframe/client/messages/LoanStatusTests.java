package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.loan.List;
import mainframe.client.messages.loan.Retrieve;
import mainframe.client.messages.loan.UpdateStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class LoanStatusTests {
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
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] loans = listMessage.getItems();
        String loanNumber = loans[0].getLoanNumber();

        // retrieve the loan
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(loanNumber),
                "retrieve");

        String newStatus = UpdateStatus.STATUS_NEW;
        if (retrieveMessage.getStatus() == UpdateStatus.STATUS_NEW) newStatus = UpdateStatus.STATUS_PENDING;

        // update the status
        UpdateStatus updateMessage = sendMessage(
                connection,
                new UpdateStatus(loanNumber, newStatus),
                "update");
        Assertions.assertEquals(newStatus, updateMessage.getStatus(), "status does not match");

        // retrieve the loan again
        retrieveMessage = sendMessage(
                connection,
                new Retrieve(loanNumber),
                "retrieve");
        Assertions.assertEquals(newStatus, retrieveMessage.getStatus(), "status does not match");
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
