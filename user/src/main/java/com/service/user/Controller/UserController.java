package com.service.user.Controller;

import com.service.user.Model.User;
import com.service.user.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Optional<User> getAdminById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.getAllUsers();
        return ResponseEntity.ok(users); // 200 OK
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String result = service.registerUser(user);
        if (result.equals("User registered successfully!")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result); // 201 Created
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 Bad Request
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        String result = service.login(username, password);

        switch (result) {
            case "Login successful!":
                return ResponseEntity.ok(result); // 200 OK
            case "User not found!":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result); // 404 Not Found
            case "Invalid credentials!":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result); // 401 Unauthorized
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 Bad Request
        }
    }
}
