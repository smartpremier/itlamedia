package kr.trycatch.device_auth_demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProtectedResourceController {

    @GetMapping("/protected-resource")
    public ResponseEntity<Map<String, Object>> getProtectedResource(Authentication authentication) {
        String deviceId = (String)authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Protected resource accessed successfully");
        response.put("deviceId", deviceId);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}
