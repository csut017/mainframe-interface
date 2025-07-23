package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.*;
import mainframe.client.messages.customerAddress.List;
import mainframe.client.messages.customerAddress.Retrieve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CustomerAddressTests {
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

        // add an address
        AddAddress addMessage = sendMessage(
                connection,
                new AddAddress("Auckland", "New Zealand", customerNumber, false, false, "Line 1", "Line 2", null, "1234", "Onehunga", AddAddress.TYPE_POSTAL),
                "add");
        long id = addMessage.getId();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, id, "id should not be zero"),
                () -> Assertions.assertEquals("Auckland", addMessage.getCity(), "city does not match"),
                () -> Assertions.assertEquals("New Zealand", addMessage.getCountry(), "country does not match"),
                () -> Assertions.assertEquals("Line 1", addMessage.getLine1(), "line1 does not match"),
                () -> Assertions.assertEquals("Line 2", addMessage.getLine2(), "line2 does not match"),
                () -> Assertions.assertEquals("1234", addMessage.getPostCode(), "postcode does not match"),
                () -> Assertions.assertEquals("Onehunga", addMessage.getSuburb(), "suburb does not match"),
                () -> Assertions.assertEquals(false, addMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals(false, addMessage.getIsMailing(), "is mailing does not match"),
                () -> Assertions.assertEquals(AddAddress.TYPE_POSTAL, addMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals(null, addMessage.getOtherType(), "other type does not match")
        );

        // list the addresses
        List listMessage = sendMessage(
                connection,
                new List(customerNumber, 1, 50),
                "list");
        List.Item[] items = listMessage.getItems();
        Assertions.assertTrue(items.length > 0, "No items returned");

        // retrieve the address
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(id, customerNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals("Auckland", retrieveMessage.getCity(), "city does not match"),
                () -> Assertions.assertEquals("New Zealand", retrieveMessage.getCountry(), "country does not match"),
                () -> Assertions.assertEquals("Line 1", retrieveMessage.getLine1(), "line1 does not match"),
                () -> Assertions.assertEquals("Line 2", retrieveMessage.getLine2(), "line2 does not match"),
                () -> Assertions.assertEquals("1234", retrieveMessage.getPostCode(), "postcode does not match"),
                () -> Assertions.assertEquals("Onehunga", retrieveMessage.getSuburb(), "suburb does not match"),
                () -> Assertions.assertEquals(false, retrieveMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals(false, retrieveMessage.getIsMailing(), "is mailing does not match"),
                () -> Assertions.assertEquals(AddAddress.TYPE_POSTAL, retrieveMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals(null, retrieveMessage.getOtherType(), "other type does not match")
        );

        // update the address
        UpdateAddress updateMessage = sendMessage(
                connection,
                new UpdateAddress("Sydney", "Australia", customerNumber, id, true, true, "1 Nowhere Lane", "Somewhere", "Hidden", "4321", "Beverly Hills", UpdateAddress.TYPE_OTHER),
                "update");
        Assertions.assertAll(
                () -> Assertions.assertEquals("Sydney", updateMessage.getCity(), "city does not match"),
                () -> Assertions.assertEquals("Australia", updateMessage.getCountry(), "country does not match"),
                () -> Assertions.assertEquals("1 Nowhere Lane", updateMessage.getLine1(), "line1 does not match"),
                () -> Assertions.assertEquals("Somewhere", updateMessage.getLine2(), "line2 does not match"),
                () -> Assertions.assertEquals("4321", updateMessage.getPostCode(), "postcode does not match"),
                () -> Assertions.assertEquals("Beverly Hills", updateMessage.getSuburb(), "suburb does not match"),
                () -> Assertions.assertEquals(true, updateMessage.getIsPrimary(), "is primary does not match"),
                () -> Assertions.assertEquals(true, updateMessage.getIsMailing(), "is mailing does not match"),
                () -> Assertions.assertEquals(AddAddress.TYPE_OTHER, updateMessage.getType(), "type does not match"),
                () -> Assertions.assertEquals("Hidden", updateMessage.getOtherType(), "other type does not match")
        );

        // delete the address
        DeleteAddress deleteMessage = sendMessage(
                connection,
                new DeleteAddress(customerNumber, id),
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
