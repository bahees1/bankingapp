// acts as a manager who decides what happens with users
// holds logic (rules, validation, calculations)
package com.sarujan.bankingapp.service;

import com.sarujan.bankingapp.model.User;
import com.sarujan.bankingapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection (preferred over @Autowired for testing & immutability)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CRUD operations
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get one user by ID (throws error if missing)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
