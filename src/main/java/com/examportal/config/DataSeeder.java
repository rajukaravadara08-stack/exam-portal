package com.examportal.config;

import com.examportal.entity.AppUser;
import com.examportal.repository.AppUserRepository;
import com.examportal.repository.ExamRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository userRepo;
    private final ExamRecordRepository recordRepo;
    private final PasswordEncoder encoder;

    @Value("${app.seed.admin-id}")
    private String adminId;
    @Value("${app.seed.admin-password}")
    private String adminPassword;

    public DataSeeder(AppUserRepository userRepo, ExamRecordRepository recordRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.recordRepo = recordRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // Only the admin login is auto-created so there's a way to sign in on first run.
        // No student accounts and no exam records are seeded — the admin adds every
        // student login and every exam record by hand from the admin panel.
        if (userRepo.count() == 0) {
            userRepo.save(new AppUser(adminId, encoder.encode(adminPassword), "ADMIN", "Administrator"));
        }
    }
}
