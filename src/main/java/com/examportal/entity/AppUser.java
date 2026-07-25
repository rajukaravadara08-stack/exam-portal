package com.examportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    private String userId;          // login ID - PRN for students, username for admins

    private String passwordHash;

    private String role;            // "ADMIN" or "STUDENT"

    private String displayName;

    public AppUser() {}

    public AppUser(String userId, String passwordHash, String role, String displayName) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.role = role;
        this.displayName = displayName;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
