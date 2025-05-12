package com.vfms.vfms.controller;

import com.vfms.vfms.entity.Fine;
import com.vfms.vfms.repository.FineRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class FineController {

    @Autowired 
    private FineRepository fineRepo;

    // Police endpoints
    @GetMapping("/api/police/fines")
    public ResponseEntity<List<Fine>> getAllFines(HttpSession session) {
        if (session.getAttribute("policeUsername") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(fineRepo.findAll());
    }

    @PostMapping("/api/police/fines")
    public ResponseEntity<?> addFine(@RequestBody Map<String, Object> payload, HttpSession session) {
        if (session.getAttribute("policeUsername") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Fine fine = new Fine();
            
            // Handle both snake_case from frontend and camelCase property names
            String vehicleNo = payload.containsKey("vehicleNo") ? 
                (String) payload.get("vehicleNo") : 
                (String) payload.get("vehicle_no");
                
            String ownerUsername = payload.containsKey("ownerUsername") ? 
                (String) payload.get("ownerUsername") : 
                (String) payload.get("owner_username");
            
            fine.setVehicleNo(vehicleNo);
            fine.setOwnerUsername(ownerUsername);
            fine.setOffense((String) payload.get("offense"));
            fine.setAmount(Integer.parseInt(payload.get("amount").toString()));
            fine.setPaid(false);
            
            // Handle date (could be sent as string)
            Object dateObj = payload.get("date");
            if (dateObj != null) {
                fine.setDate(LocalDate.parse((String) dateObj));
            } else {
                fine.setDate(LocalDate.now());
            }
            
            fineRepo.save(fine);
            return ResponseEntity.status(HttpStatus.CREATED).body(fine);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid fine data: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/police/fines/{id}")
    public ResponseEntity<?> deleteFine(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("policeUsername") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!fineRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        fineRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/police/fines/{id}")
    public ResponseEntity<?> updateFine(@PathVariable Long id, @RequestBody Map<String, Object> payload, HttpSession session) {
        if (session.getAttribute("policeUsername") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Optional<Fine> fineOpt = fineRepo.findById(id);
        if (fineOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Fine fine = fineOpt.get();
        
        if (payload.containsKey("offense")) {
            fine.setOffense((String) payload.get("offense"));
        }
        
        if (payload.containsKey("amount")) {
            fine.setAmount(Integer.parseInt(payload.get("amount").toString()));
        }
        
        if (payload.containsKey("paid")) {
            fine.setPaid((Boolean) payload.get("paid"));
        }
        
        fineRepo.save(fine);
        return ResponseEntity.ok(fine);
    }

    // User endpoints
    @GetMapping("/api/user/fines")
    public ResponseEntity<?> getFinesByVehicle(@RequestParam("vehicle_no") String vehicleNo) {
        List<Fine> fines = fineRepo.findByVehicleNo(vehicleNo);
        return ResponseEntity.ok(fines);
    }

    @PostMapping("/api/user/fines/{id}/pay")
    public ResponseEntity<?> payFine(@PathVariable Long id, @RequestBody Map<String, String> payload, HttpSession session) {
        String utr = payload.get("utr");
        if (utr == null || utr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("UTR number is required");
        }
        
        Optional<Fine> fineOpt = fineRepo.findById(id);
        if (fineOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Fine fine = fineOpt.get();
        if (fine.isPaid()) {
            return ResponseEntity.badRequest().body("Fine is already paid");
        }
        
        fine.setPaid(true);
        fineRepo.save(fine);
        
        return ResponseEntity.ok().build();
    }
}