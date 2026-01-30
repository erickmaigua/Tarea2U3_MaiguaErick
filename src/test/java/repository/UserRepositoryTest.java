package repository;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
    }

    @Test
    void testCreateUser() {
        User user = new User(1, "Erick");
        repository.create(user);

        Optional<User> found = repository.findById(1);
        assertTrue(found.isPresent());
        assertEquals("Erick", found.get().getName());
    }

    @Test
    void testFindById_UserExists() {
        User user = new User(2, "Juan");
        repository.create(user);

        Optional<User> result = repository.findById(2);
        assertTrue(result.isPresent());
    }

    @Test
    void testFindById_UserDoesNotExist() {
        Optional<User> result = repository.findById(99);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll() {
        repository.create(new User(1, "Ana"));
        repository.create(new User(2, "Luis"));

        List<User> users = repository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testDeleteUser() {
        User user = new User(3, "Carlos");
        repository.create(user);

        repository.delete(3);
        Optional<User> result = repository.findById(3);

        assertFalse(result.isPresent());
    }
}
