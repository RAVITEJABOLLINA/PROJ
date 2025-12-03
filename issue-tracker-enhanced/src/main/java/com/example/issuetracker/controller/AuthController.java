package com.example.issuetracker.controller;

import com.example.issuetracker.dto.AuthRequest;
import com.example.issuetracker.dto.AuthResponse;
import com.example.issuetracker.model.User;
import com.example.issuetracker.repository.UserRepository;
import com.example.issuetracker.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, UserRepository userRepo,
                          BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        if (req == null || req.getUsername() == null || req.getPassword() == null) {
            return ResponseEntity.badRequest().body("invalid_request");
        }
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("username_taken");
        }
        // default role: REPORTER
        User u = new User(req.getUsername(), passwordEncoder.encode(req.getPassword()), "ROLE_REPORTER");
        userRepo.save(u);
        return ResponseEntity.ok("registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            Optional<User> ou = userRepo.findByUsername(req.getUsername());
            if (ou.isEmpty()) return ResponseEntity.status(500).body("user_missing");
            String token = jwtUtil.generateToken(ou.get().getUsername(), ou.get().getRoles());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("invalid_credentials");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("error");
        }
    }
}
