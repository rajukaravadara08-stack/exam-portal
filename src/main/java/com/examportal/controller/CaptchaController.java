package com.examportal.controller;

import com.examportal.service.CaptchaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/captcha")
    public CaptchaService.Captcha getCaptcha() {
        return captchaService.generate();
    }
}
