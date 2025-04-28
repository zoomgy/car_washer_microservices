package com.service.admin.Service;

import com.service.admin.Model.Admin;
import com.service.admin.Repository.AdminRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Initialize encoder
    }

    // Get all admins
    public List<Admin> getAllAdmins() {
        return repository.findAll();
    }

    // Get admin by ID
    public Optional<Admin> getAdminById(Long id) {
        return repository.findById(id);
    }

    // Register (create) a new admin
    public String registerAdmin(Admin admin) {
        // Check if the username already exists
        Optional<Admin> existingUsername = repository.findByUsername(admin.getUsername());
        if (existingUsername.isPresent()) {
            return "Username already exists!";
        }

        // Check if the email already exists
        Optional<Admin> existingEmail = repository.findByEmail(admin.getEmail());
        if (existingEmail.isPresent()) {
            return "Email already exists!";
        }

        // Encrypt password before saving
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        // Save the new admin to the database
        repository.save(admin);
        return "Admin registered successfully!";
    }

    // Update admin
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        return repository.findById(id)
                .map(admin -> {
                    admin.setUsername(updatedAdmin.getUsername());
                    admin.setEmail(updatedAdmin.getEmail());
                    if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
                        admin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword())); // 🔥 Encrypt updated password
                    }
                    return repository.save(admin);
                })
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    // Delete admin
    public String deleteAdmin(Long id) {
        repository.deleteById(id);
        return "Admin deleted successfully.";
    }

    // Login Admin
    public String loginAdmin(String username, String rawPassword) {
        Optional<Admin> adminOptional = repository.findByUsername(username);

        if (!adminOptional.isPresent()) {
            return "Admin not found!";
        }

        Admin admin = adminOptional.get();

        // 🔥 Check hashed password
        if (passwordEncoder.matches(rawPassword, admin.getPassword())) {
            return "Login successful!";
        } else {
            return "Invalid credentials!";
        }
    }
}
