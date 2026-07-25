package com.examportal.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaService {

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();

    // captchaId -> expected text (single-use, in-memory; fine for a demo/small deployment)
    private final Map<String, String> store = new ConcurrentHashMap<>();

    public record Captcha(String id, String svg) {}

    public Captcha generate() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            text.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        String id = UUID.randomUUID().toString();
        store.put(id, text.toString());

        StringBuilder svg = new StringBuilder();
        svg.append("<svg xmlns='http://www.w3.org/2000/svg' width='120' height='42'>");
        svg.append("<rect width='120' height='42' fill='#dde3ea'/>");
        for (int i = 0; i < 5; i++) {
            svg.append("<line x1='").append(random.nextInt(120)).append("' y1='").append(random.nextInt(42))
               .append("' x2='").append(random.nextInt(120)).append("' y2='").append(random.nextInt(42))
               .append("' stroke='#1c3a63' stroke-opacity='0.15'/>");
        }
        for (int i = 0; i < text.length(); i++) {
            int x = 12 + i * 20;
            int y = 27 + random.nextInt(8) - 4;
            int rotate = random.nextInt(30) - 15;
            svg.append("<text x='").append(x).append("' y='").append(y)
               .append("' transform='rotate(").append(rotate).append(" ").append(x).append(" ").append(y).append(")'")
               .append(" font-family='monospace' font-weight='700' font-size='22' fill='#1c3a63'>")
               .append(text.charAt(i)).append("</text>");
        }
        svg.append("</svg>");

        return new Captcha(id, svg.toString());
    }

    public boolean verify(String id, String text) {
        String expected = store.remove(id); // single use
        return expected != null && expected.equalsIgnoreCase(text == null ? "" : text.trim());
    }
}
