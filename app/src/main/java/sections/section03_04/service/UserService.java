package sections.section03_04.service;

import java.util.*;
import sections.section03_04.domain.*;
import sections.section03_04.repository.*;

public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> findById(Long userId) {
        return userRepo.findById(userId);
    }

    public User signUp(User user) {
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    public User editUser(User user) {
        return userRepo.update(user);
    }
}
