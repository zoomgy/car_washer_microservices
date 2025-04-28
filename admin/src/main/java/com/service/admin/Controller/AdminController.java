package com.service.admin.Controller;

import com.service.admin.Model.Admin;
import com.service.admin.Model.User;
import com.service.admin.Service.AdminService;
import com.service.admin.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserClient userClient;

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    // Get all admins
    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = service.getAllAdmins();
        return ResponseEntity.ok(admins); // 200 OK
    }

    // Get admin by ID
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Optional<Admin> admin = service.getAdminById(id);
        return admin.map(ResponseEntity::ok) // 200 OK
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404 Not Found
    }

    // Register new admin
    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody Admin admin) {
        String result = service.registerAdmin(admin);
        if (result.equals("Admin registered successfully!")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result); // 201 Created
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 Bad Request
        }
    }

    // Update admin
    @PutMapping("/update/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin) {
        try {
            Admin admin = service.updateAdmin(id, updatedAdmin);
            return ResponseEntity.ok(admin); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found if admin not found
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        try {
            String result = service.deleteAdmin(id);
            return ResponseEntity.ok(result); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found!"); // 404 Not Found
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestParam String username, @RequestParam String password) {
        String result = service.loginAdmin(username, password);

        switch (result) {
            case "Login successful!":
                return ResponseEntity.ok(result);
            case "Admin not found!":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            case "Invalid credentials!":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/getallusers")
    public ResponseEntity<List<User>> findAllUsers(){
        return userClient.getAllUsers();
    }

    @GetMapping("/getuser/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        ResponseEntity<List<User>> response = userClient.getAllUsers();
        if (response.getStatusCode().is2xxSuccessful()) {
            List<User> users = response.getBody();
            return users.stream()
                    .filter(user -> user.getId().equals(id))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }
}
