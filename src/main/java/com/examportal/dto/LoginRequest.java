package com.examportal.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String captchaId;
    @NotBlank
    private String captchaText;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCaptchaId() { return captchaId; }
    public void setCaptchaId(String captchaId) { this.captchaId = captchaId; }
    public String getCaptchaText() { return captchaText; }
    public void setCaptchaText(String captchaText) { this.captchaText = captchaText; }
}
