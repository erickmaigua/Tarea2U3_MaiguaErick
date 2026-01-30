package service;


import repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void createUserSuccessfully() {
        UserService service = new UserService(new UserRepository());
        service.createUser(1, "Ana");
        assertEquals(1, service.getUsers().size());
    }

    @Test
    void createUserWithEmptyNameThrowsException() {
        UserService service = new UserService(new UserRepository());
        assertThrows(IllegalArgumentException.class,
                () -> service.createUser(1, ""));
    }
}
