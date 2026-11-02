package com.kokoro.controller;

import com.kokoro.model.User;
import com.kokoro.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public record SignupReq(String username, String email, String password) {}
    public record LoginReq(String username, String password) {}

    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository users) { this.users = users; }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReq r) {
        if (blank(r.username()) || blank(r.email()) || blank(r.password()))
            return ResponseEntity.badRequest().body(Map.of("error", "Please fill all fields"));
        if (users.existsByUsername(r.username().trim()))
            return ResponseEntity.status(409).body(Map.of("error", "Username already taken"));
        User u = new User();
        u.setUsername(r.username().trim());
        u.setEmail(r.email().trim());
        u.setPasswordHash(encoder.encode(r.password()));   // never store plain text
        users.save(u);
        return ResponseEntity.status(201).body(Map.of("message", "Account created"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq r) {
        var found = users.findByUsername(r.username() == null ? "" : r.username().trim());
        if (found.isEmpty() || !encoder.matches(r.password() == null ? "" : r.password(), found.get().getPasswordHash()))
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        User u = found.get();
        u.setToken(UUID.randomUUID().toString());
        users.save(u);
        return ResponseEntity.ok(Map.of("token", u.getToken(), "username", u.getUsername(), "checkedIn", u.isCheckedIn()));
    }

    private boolean blank(String s) { return s == null || s.isBlank(); }
}
