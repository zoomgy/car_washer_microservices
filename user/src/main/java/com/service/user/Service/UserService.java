package com.service.user.Service;

import com.service.user.Model.User;
import com.service.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Injecting repository and password encoder
    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // Fetching all admin users
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    // Registering a new user
    public String registerUser(User user) {
        // Check if user already exists (assuming 'username' is unique)
        Optional<User> existingUser = repository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return "Username already exists!";
        }
        Optional<User> existingEmail = repository.findByEmail(user.getEmail());
        if(existingEmail.isPresent()){
            return "Email already exists!";
        }

        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the new user to the database
        repository.save(user);
        return "User registered successfully!";
    }

    // User login
    public String login(String username, String password) {
        Optional<User> existingUser = repository.findByUsername(username);

        // Check if user exists
        if (!existingUser.isPresent()) {
            return "User not found!";
        }

        // Verify the password
        User user = existingUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid credentials!";
        }

        return "Login successful!";
    }
}
