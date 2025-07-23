package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.AddPhoneNumber;
import mainframe.client.messages.customer.DeletePhoneNumber;
import mainframe.client.messages.customer.Search;
import mainframe.client.messages.customer.UpdatePhoneNumber;
import mainframe.client.messages.customerPhoneNumber.List;
import mainframe.client.messages.customerPhoneNumber.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CustomerPhoneNumberTests {
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

        // add a phone number
        AddPhoneNumber addMessage = sendMessage(
                connection,
                new AddPhoneNumber(true, "+64", customerNumber, true, "9-123-4567", null, AddPhoneNumber.TYPE_HOME),
                "add");
        long id = addMessage.getId();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals(true, addMessage.getCanTxt(), "can TXT does not match"),
                () -> Assertions.assertEquals(true, addMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals("+64", addMessage.getCountryCode(), "country code does not match"),
                () -> Assertions.assertEquals("9-123-4567", addMessage.getNumber(), "number does not match"),
                () -> Assertions.assertEquals(AddPhoneNumber.TYPE_HOME, addMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals(null, addMessage.getOtherType(), "other type does not match")
        );

        // list the phone numbers
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] items = listMessage.getItems();
        Assertions.assertTrue(items.length > 0, "No items returned");

        // retrieve the phone number
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, customerNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals(true, retrieveMessage.getCanTxt(), "can TXT does not match"),
                () -> Assertions.assertEquals(true, retrieveMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals("+64", retrieveMessage.getCountryCode(), "country code does not match"),
                () -> Assertions.assertEquals("9-123-4567", retrieveMessage.getNumber(), "number does not match"),
                () -> Assertions.assertEquals(AddPhoneNumber.TYPE_HOME, retrieveMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals(null, retrieveMessage.getOtherType(), "other type does not match")
        );

        // update the phone number
        UpdatePhoneNumber updateMessage = sendMessage(
                connection,
                new UpdatePhoneNumber(false, "+61", customerNumber, id, false, "1234-5678", "Overseas", UpdatePhoneNumber.TYPE_OTHER),
                "update");
        Assertions.assertAll(
                () -> Assertions.assertEquals(false, updateMessage.getCanTxt(), "can TXT does not match"),
                () -> Assertions.assertEquals(false, updateMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals("+61", updateMessage.getCountryCode(), "country code does not match"),
                () -> Assertions.assertEquals("1234-5678", updateMessage.getNumber(), "number does not match"),
                () -> Assertions.assertEquals(AddPhoneNumber.TYPE_OTHER, updateMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals("Overseas", updateMessage.getOtherType(), "other type does not match")
        );

        // delete the phone number
        DeletePhoneNumber deleteMessage = sendMessage(
                connection,
                new DeletePhoneNumber(customerNumber, id),
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
