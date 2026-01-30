package model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userIsCreatedCorrectly() {
        User user = new User(1, "Juan");
        assertEquals(1, user.getId());
        assertEquals("Juan", user.getName());
    }
}
