package mainframe.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpConnectionTests {

    @Test
    void isConnected() {
        // Arrange
        Connection connection = new HttpConnection(TestConstants.BASE_URL);

        // Act
        boolean isConnected = connection.isConnected();

        // Assert
        assertTrue(isConnected, "Expected isConnected to be true, unless the server is down");
    }
}