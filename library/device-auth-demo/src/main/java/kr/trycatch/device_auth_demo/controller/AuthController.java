package kr.trycatch.device_auth_demo.controller;

import kr.trycatch.device_auth_demo.dto.TokenDto;
import kr.trycatch.device_auth_demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/device")
    public ResponseEntity<TokenDto> authenticateDevice(@RequestParam String deviceId) {
        return ResponseEntity.ok(authService.authenticate(deviceId));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
