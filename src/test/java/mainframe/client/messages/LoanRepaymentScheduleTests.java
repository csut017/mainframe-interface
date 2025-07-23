package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.loan.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoanRepaymentScheduleTests {
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

        // list the repayment schedule
        mainframe.client.messages.loanRepaymentSchedule.List listMessage = sendMessage(
                connection,
                new mainframe.client.messages.loanRepaymentSchedule.List(loanNumber, 1, 50),
                "list");
        Assertions.assertTrue(listLoansMessage.getItems().length > 0, "No items returned");
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
