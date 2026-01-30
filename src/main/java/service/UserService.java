package service;

import model.User;
import repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(int id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        User user = new User(id, name);
        repository.create(user);
        return user;
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public void updateUser(int id, String newName) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(newName);
    }

    public void deleteUser(int id) {
        repository.delete(id);
    }
}
