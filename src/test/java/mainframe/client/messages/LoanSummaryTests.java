package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.loan.List;
import mainframe.client.messages.loanSummary.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoanSummaryTests {
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

        // retrieve the payment
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(loanNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertTrue( retrieveMessage.getFinalPaymentAmount() > 0, "final payment amount should be greater than zero"),
                () -> Assertions.assertTrue( retrieveMessage.getTotalAmountPaid() > 0, "total amount should be greater than zero"),
                () -> Assertions.assertTrue( retrieveMessage.getNumberOfPayments() > 0, "number of payments should be greater than zero"),
                () -> Assertions.assertTrue( retrieveMessage.getTotalInterestPaid() > 0, "total interest paid should be greater than zero")
        );
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
