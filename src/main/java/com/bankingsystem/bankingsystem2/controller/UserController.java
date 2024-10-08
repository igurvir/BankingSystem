package com.bankingsystem.bankingsystem2.controller;

import com.bankingsystem.bankingsystem2.model.User;
import com.bankingsystem.bankingsystem2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Validate required fields
        if (user.getName() == null || user.getName().isEmpty()) {
            return ResponseEntity.status(400).body("Name is required.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.status(400).body("Email is required.");
        }
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.status(400).body("Invalid email format.");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return ResponseEntity.status(400).body("Password must be at least 6 characters long.");
        }
        if (!user.getPassword().matches(".*[A-Za-z0-9].*")) {
            return ResponseEntity.status(400).body("Password must contain letters and numbers.");
        }

        // Check if the email already exists
        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(400).body("User with this email already exists.");
        }

        // Create the user if validations pass
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
