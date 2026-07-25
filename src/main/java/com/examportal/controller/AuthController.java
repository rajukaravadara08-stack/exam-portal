package com.examportal.controller;

import com.examportal.config.JwtUtil;
import com.examportal.dto.LoginRequest;
import com.examportal.dto.LoginResponse;
import com.examportal.entity.AppUser;
import com.examportal.repository.AppUserRepository;
import com.examportal.service.CaptchaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final CaptchaService captchaService;

    public AuthController(AppUserRepository userRepo, PasswordEncoder encoder,
                           JwtUtil jwtUtil, CaptchaService captchaService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.captchaService = captchaService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {

        if (!captchaService.verify(req.getCaptchaId(), req.getCaptchaText())) {
            return ResponseEntity.status(401).body(Map.of("error", "Incorrect captcha. Please try again."));
        }

        Optional<AppUser> userOpt = userRepo.findById(req.getUserId());
        if (userOpt.isEmpty() || !encoder.matches(req.getPassword(), userOpt.get().getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid ID or password."));
        }

        AppUser user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUserId(), user.getRole());

        return ResponseEntity.ok(new LoginResponse(token, user.getUserId(), user.getRole(), user.getDisplayName()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!jwtUtil.isValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired session."));
        }
        var claims = jwtUtil.parseClaims(token);
        String userId = claims.getSubject();
        AppUser user = userRepo.findById(userId).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "role", user.getRole(),
                "displayName", user.getDisplayName()
        ));
    }
}
