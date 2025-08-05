package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.loan.Create;
import mainframe.client.messages.loan.List;
import mainframe.client.messages.loan.Retrieve;
import mainframe.client.messages.loan.Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

public class LoanTests {
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

        // create a new loan
        String name = "test_" + Instant.now().toEpochMilli();
        Create createMessage = sendMessage(
                connection,
                new Create(customerNumber, 100.00, Create.PAYMENTFREQUENCY_WEEKLY, 1234.56, 2.34, Create.RATEFREQUENCY_WEEKLY, Create.RATETYPE_FIXED, "2030-01-02", "2025-01-02"),
                "create");
        String loanNumber = createMessage.getLoanNumber();
        Assertions.assertAll(
                () -> Assertions.assertEquals(customerNumber, createMessage.getCustomerNumber(), "customer number does not match"),
                () -> Assertions.assertEquals(100.00, createMessage.getPaymentAmount(), "payment amount does not match"),
                () -> Assertions.assertEquals(Create.PAYMENTFREQUENCY_WEEKLY, createMessage.getPaymentFrequency(), "payment frequency does not match"),
                () -> Assertions.assertEquals(1234.56, createMessage.getPrincipal(), "principal does not match"),
                () -> Assertions.assertEquals(2.34, createMessage.getRateAmount(), "rate amount does not match"),
                () -> Assertions.assertEquals(Create.RATEFREQUENCY_WEEKLY, createMessage.getRateFrequency(), "rate frequency does not match"),
                () -> Assertions.assertEquals(Create.RATETYPE_FIXED, createMessage.getRateType(), "rate type does not match"),
                () -> Assertions.assertEquals("2030-01-02", createMessage.getRenewalDate(), "renewal date does not match"),
                () -> Assertions.assertEquals("2025-01-02", createMessage.getStartDate(), "start date does not match"),
                () -> Assertions.assertEquals("New", createMessage.getStatus(), "status does not match")
        );

        // list the loans
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] items = listMessage.getItems();
        Assertions.assertAll(
                () -> Assertions.assertTrue(items.length > 0, "No items returned"),
                () -> Assertions.assertTrue(Arrays.stream(listMessage.getItems()).map(i -> i.getLoanNumber()).anyMatch(loanNumber::equals), "items does not contain number")
        );

        // retrieve the loan
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(loanNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals(customerNumber, retrieveMessage.getCustomerNumber(), "customer number does not match"),
                () -> Assertions.assertEquals(100.00, retrieveMessage.getPaymentAmount(), "payment amount does not match"),
                () -> Assertions.assertEquals(Create.PAYMENTFREQUENCY_WEEKLY, retrieveMessage.getPaymentFrequency(), "payment frequency does not match"),
                () -> Assertions.assertEquals(1234.56, retrieveMessage.getPrincipal(), "principal does not match"),
                () -> Assertions.assertEquals(2.34, retrieveMessage.getRateAmount(), "rate amount does not match"),
                () -> Assertions.assertEquals(Create.RATEFREQUENCY_WEEKLY, retrieveMessage.getRateFrequency(), "rate frequency does not match"),
                () -> Assertions.assertEquals(Create.RATETYPE_FIXED, retrieveMessage.getRateType(), "rate type does not match"),
                () -> Assertions.assertEquals("2030-01-02", retrieveMessage.getRenewalDate(), "renewal date does not match"),
                () -> Assertions.assertEquals("2025-01-02", retrieveMessage.getStartDate(), "start date does not match"),
                () -> Assertions.assertEquals("New", retrieveMessage.getStatus(), "status does not match")
        );

        // update the loan
        Update updateMessage = sendMessage(
                connection,
                new Update(customerNumber, loanNumber, 200.00, Update.PAYMENTFREQUENCY_FORTNIGHTLY, 2345.67, 2.43, Update.RATEFREQUENCY_DAILY, Update.RATETYPE_FLOATING, "2030-02-03", "2025-02-03"),
                "update");
        Assertions.assertAll(
                () -> Assertions.assertEquals(200.00, updateMessage.getPaymentAmount(), "payment amount does not match"),
                () -> Assertions.assertEquals(Create.PAYMENTFREQUENCY_FORTNIGHTLY, updateMessage.getPaymentFrequency(), "payment frequency does not match"),
                () -> Assertions.assertEquals(2345.67, updateMessage.getPrincipal(), "principal does not match"),
                () -> Assertions.assertEquals(2.43, updateMessage.getRateAmount(), "rate amount does not match"),
                () -> Assertions.assertEquals(Create.RATEFREQUENCY_DAILY, updateMessage.getRateFrequency(), "rate frequency does not match"),
                () -> Assertions.assertEquals(Create.RATETYPE_FLOATING, updateMessage.getRateType(), "rate type does not match"),
                () -> Assertions.assertEquals("2030-02-03", updateMessage.getRenewalDate(), "renewal date does not match"),
                () -> Assertions.assertEquals("2025-02-03", updateMessage.getStartDate(), "start date does not match"),
                () -> Assertions.assertEquals("New", updateMessage.getStatus(), "status does not match")
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

    @Test
    void listLoansForCustomerWithoutLoans() throws IOException, InterruptedException {
        // login
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        connection.Login("test", "test");

        // create a new customer
        String name = "test_" + Instant.now().toEpochMilli();
        mainframe.client.messages.customer.Create createMessage = sendMessage(
                connection,
                new mainframe.client.messages.customer.Create("2001-02-03", "Doe", name, "New Zealand", 1),
                "create");
        String customerNumber = createMessage.getNumber();

        // list the loans
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] items = listMessage.getItems();
        Assertions.assertEquals(0, listMessage.getTotal(), "total should be zero");
    }
}
