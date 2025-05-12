package com.vfms.vfms.controller;

import com.vfms.vfms.entity.User;
import com.vfms.vfms.entity.Police;
import com.vfms.vfms.repository.UserRepository;
import com.vfms.vfms.repository.PoliceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private PoliceRepository policeRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/{role}/signup")
    public ResponseEntity<String> signup(@PathVariable String role, @RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = encoder.encode(payload.get("password"));

        if ("user".equalsIgnoreCase(role)) {
            if (userRepo.findByUsername(username).isPresent())
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            userRepo.save(new User(null, username, password));
        } else if ("police".equalsIgnoreCase(role)) {
            if (policeRepo.findByUsername(username).isPresent())
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Police already exists");
            policeRepo.save(new Police(null, username, password));
        } else {
            return ResponseEntity.badRequest().body("Invalid role");
        }

        return ResponseEntity.ok("Signup successful");
    }
}
