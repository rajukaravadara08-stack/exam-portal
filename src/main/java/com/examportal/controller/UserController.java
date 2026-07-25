package com.examportal.controller;

import com.examportal.dto.CreateUserRequest;
import com.examportal.entity.AppUser;
import com.examportal.repository.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AppUserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserController(AppUserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping
    public List<AppUser> list() {
        // never leak password hashes to the client
        List<AppUser> all = userRepo.findAll();
        all.forEach(u -> u.setPasswordHash(null));
        return all;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequest req) {
        if (userRepo.existsById(req.getUserId())) {
            return ResponseEntity.status(409).body(Map.of("error", "A user with this ID already exists."));
        }
        AppUser user = new AppUser(
                req.getUserId(),
                encoder.encode(req.getPassword()),
                req.getRole(),
                req.getDisplayName()
        );
        userRepo.save(user);
        user.setPasswordHash(null);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId) {
        if (!userRepo.existsById(userId)) return ResponseEntity.notFound().build();
        userRepo.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
