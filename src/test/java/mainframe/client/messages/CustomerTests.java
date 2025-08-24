package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.customer.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.Arrays;

public class CustomerTests {
    @Test
    void sendsToServer() throws IOException, InterruptedException {
        // login
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        connection.Login("test", "test");

        // create a new customer
        String name = "test_" + Instant.now().toEpochMilli();
        Create createMessage = sendMessage(
                connection,
                new Create("2001-02-03", "Doe", name, "New Zealand", 1, "Mr"),
                "create");
        String customerNumber = createMessage.getNumber();
        Assertions.assertAll(
                () -> Assertions.assertEquals("Doe", createMessage.getFamilyName(), "familyName does not match"),
                () -> Assertions.assertEquals(name, createMessage.getFirstName(), "firstName does not match"),
                () -> Assertions.assertEquals("Active", createMessage.getStatus(), "status does not match"),
                () -> Assertions.assertEquals("Mr", createMessage.getTitle(), "title does not match"),
                () -> Assertions.assertEquals("New Zealand", createMessage.getNationality(), "nationality does not match"),
                () -> Assertions.assertEquals("2001-02-03", createMessage.getDateOfBirth(), "dateOfBirth does not match")
        );

        // search for the customer
        Search searchMessage = sendMessage(
                connection,
                new Search(1, name, 25),
                "search");
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, searchMessage.getTotal(), "total should be one"),
                () -> Assertions.assertTrue(Arrays.stream(searchMessage.getItems()).map(i -> i.getNumber()).anyMatch(customerNumber::equals), "items does not contain number")
        );

        // retrieve the customer
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(customerNumber),
                "retrieve");
        Assertions.assertAll(
                () -> Assertions.assertEquals("Doe", retrieveMessage.getFamilyName(), "familyName does not match"),
                () -> Assertions.assertEquals("Mr", createMessage.getTitle(), "title does not match"),
                () -> Assertions.assertEquals(name, retrieveMessage.getFirstName(), "firstName does not match"),
                () -> Assertions.assertEquals("Active", retrieveMessage.getStatus(), "status does not match"),
                () -> Assertions.assertEquals("New Zealand", retrieveMessage.getNationality(), "nationality does not match"),
                () -> Assertions.assertEquals("2001-02-03", retrieveMessage.getDateOfBirth(), "dateOfBirth does not match")
        );

        // update the customer
        String newName = "new_" + name;
        Update updateMessage = sendMessage(
                connection,
                new Update("2002-03-04", "Smith", newName, "Australia", customerNumber, "Dr"),
                "update");
        Assertions.assertAll(
                () -> Assertions.assertEquals("Smith", updateMessage.getFamilyName(), "familyName does not match"),
                () -> Assertions.assertEquals("Dr", createMessage.getTitle(), "title does not match"),
                () -> Assertions.assertEquals(newName, updateMessage.getFirstName(), "firstName does not match"),
                () -> Assertions.assertEquals("Active", updateMessage.getStatus(), "status does not match"),
                () -> Assertions.assertEquals("Australia", updateMessage.getNationality(), "nationality does not match"),
                () -> Assertions.assertEquals("2002-03-04", updateMessage.getDateOfBirth(), "dateOfBirth does not match")
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
    void createHandlesMissingDateOfBirth() throws  IOException, InterruptedException {
        // Arrange
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        connection.Login("test", "test");

        // Act
        Create createMessage = sendMessage(
                connection,
                new Create(null, null, "Petra", null, 0, null),
                "create");

        // Assert
        Assertions.assertNotNull(createMessage.getNumber(), "number should not be null");
    }

    @Test
    void searchHandlesNoData() throws IOException, InterruptedException {
        // Arrange
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        connection.Login("test", "test");

        // Act
        Search searchMessage = sendMessage(
                connection,
                new Search(1, "Does not exist", 25),
                "search");

        // Assert
        Assertions.assertEquals(0, searchMessage.getTotal(), "total should be zero");
    }
}
