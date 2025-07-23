package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CustomerStatusTests {
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

        // retrieve the customer
        mainframe.client.messages.customer.Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(customerNumber),
                "retrieve");

        String newStatus = UpdateStatus.STATUS_ACTIVE;
        if (retrieveMessage.getStatus() == UpdateStatus.STATUS_ACTIVE) newStatus = UpdateStatus.STATUS_INACTIVE;

        // update the status
        UpdateStatus updateMessage = sendMessage(
                connection,
                new UpdateStatus(customerNumber, newStatus),
                "update");
        Assertions.assertEquals(newStatus, updateMessage.getStatus(), "status does not match");

        // retrieve the customer again
        retrieveMessage = sendMessage(
                connection,
                new Retrieve(customerNumber),
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
