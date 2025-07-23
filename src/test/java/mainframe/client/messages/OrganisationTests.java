package mainframe.client.messages;

import mainframe.client.*;
import mainframe.client.messages.organisation.Create;
import mainframe.client.messages.organisation.List;
import mainframe.client.messages.organisation.Retrieve;
import mainframe.client.messages.organisation.Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

public class OrganisationTests {
    @Test
    void sendsToServer() throws IOException, InterruptedException {
        // login
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        connection.Login("admin", "admin");

        // create a new organisation
        Create createMessage = sendMessage(
                connection,
                new Create("test_" + Instant.now().toEpochMilli()),
                "create");
        long organisationId = createMessage.getId();

        // update the organisation
        String newName = "updated_test_" + Instant.now().toEpochMilli();
        sendMessage(
                connection,
                new Update(organisationId, newName),
                "update");

        // retrieve the organisation
        Retrieve retrieveMessage = sendMessage(
                connection,
                new Retrieve(organisationId),
                "retrieve");
        Assertions.assertEquals(organisationId, retrieveMessage.getId());
        Assertions.assertEquals(newName, retrieveMessage.getName());

        // list the organisations
        List listMessage = sendMessage(
                connection,
                new List(1, 25),
                "list");
        Assertions.assertTrue(listMessage.getItems().length > 0);
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

