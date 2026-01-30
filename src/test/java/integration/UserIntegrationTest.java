package integration;


import controller.UserController;
import repository.UserRepository;
import service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIntegrationTest {

    @Test
    void fullCrudFlow() {
        UserRepository repo = new UserRepository();
        UserService service = new UserService(repo);
        UserController controller = new UserController(service);

        controller.create(1, "Carlos");
        controller.update(1, "Pedro");
        controller.delete(1);

        assertEquals(0, controller.readAll().size());
    }
}
