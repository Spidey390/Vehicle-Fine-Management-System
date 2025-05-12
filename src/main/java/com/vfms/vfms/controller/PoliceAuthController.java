package com.vfms.vfms.controller;

import com.vfms.vfms.dto.LoginRequest;
import com.vfms.vfms.entity.Police;
import com.vfms.vfms.repository.PoliceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/police")
public class PoliceAuthController {

    @Autowired private PoliceRepository policeRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        // For admin login
        if ("admin".equals(username) && "admin123".equals(password)) {
            session.setAttribute("policeUsername", username);
            return ResponseEntity.ok().build(); // 200 OK
        }
        
        // For regular police users
        Optional<Police> policeOpt = policeRepo.findByUsername(username);
        if (policeOpt.isPresent() && passwordEncoder.matches(password, policeOpt.get().getPassword())) {
            session.setAttribute("policeUsername", username);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        if (policeRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Police officer already exists");
        }
        
        Police police = new Police();
        police.setUsername(username);
        police.setPassword(passwordEncoder.encode(password));
        policeRepo.save(police);
        
        return ResponseEntity.ok("Police officer registered successfully");
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.removeAttribute("policeUsername");
        return ResponseEntity.ok().build();
    }
}