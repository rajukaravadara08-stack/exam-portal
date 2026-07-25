package com.examportal.dto;

public class LoginResponse {
    private String token;
    private String userId;
    private String role;
    private String displayName;

    public LoginResponse(String token, String userId, String role, String displayName) {
        this.token = token;
        this.userId = userId;
        this.role = role;
        this.displayName = displayName;
    }

    public String getToken() { return token; }
    public String getUserId() { return userId; }
    public String getRole() { return role; }
    public String getDisplayName() { return displayName; }
}
