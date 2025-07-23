package mainframe.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusTests {

    @Test
    void successConstructorSetsRelevantFields() {
        // act
        Status status = new Status(1234);

        // assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(1234, status.getTransactionId()),
                () -> Assertions.assertTrue(status.getWasSuccessful()),
                () -> Assertions.assertNotNull(status.getTimestamp()),
                () -> Assertions.assertEquals(0, status.getErrorCode()),
                () -> Assertions.assertNull(status.getErrorMessage())
        );
    }

    @Test
    void failureConstructorSetsRelevantFields() {
        // act
        Status status = new Status(567, "An error", 1234);

        // assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(1234, status.getTransactionId()),
                () -> Assertions.assertFalse(status.getWasSuccessful()),
                () -> Assertions.assertNotNull(status.getTimestamp()),
                () -> Assertions.assertEquals(567, status.getErrorCode()),
                () -> Assertions.assertEquals("An error", status.getErrorMessage())
        );
    }
}