package controller;



import model.User;
import service.UserService;

import java.util.List;

public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public User create(int id, String name) {
        return service.createUser(id, name);
    }

    public List<User> readAll() {
        return service.getUsers();
    }

    public void update(int id, String newName) {
        service.updateUser(id, newName);
    }

    public void delete(int id) {
        service.deleteUser(id);
    }
}
