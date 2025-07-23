package mainframe.client.messages;

import mainframe.client.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LoginTests {
    @Test
    void sendsToServer() throws IOException {
        // arrange
        Connection connection = new HttpConnection(TestConstants.BASE_URL);
        Login message = new Login();
        message.setUserName("test");
        message.setpassword("test");

        // act
        Status status = message.send(connection);

        // assert
        Assertions.assertAll(
                () -> Assertions.assertTrue(status.getWasSuccessful()),
                () -> Assertions.assertNotNull(status.getTimestamp()),
                () -> Assertions.assertNull(status.getErrorMessage()),
                () -> Assertions.assertTrue(message.getLastNumber() >= 0, "LastNumber has to be greater than 0")
        );
    }
}