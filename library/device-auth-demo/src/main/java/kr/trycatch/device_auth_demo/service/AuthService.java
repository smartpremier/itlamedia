package kr.trycatch.device_auth_demo.service;

import jakarta.transaction.Transactional;
import kr.trycatch.device_auth_demo.dto.TokenDto;
import kr.trycatch.device_auth_demo.exception.DeviceNotFoundException;
import kr.trycatch.device_auth_demo.exception.InvalidTokenException;
import kr.trycatch.device_auth_demo.model.Device;
import kr.trycatch.device_auth_demo.repository.DeviceRepository;
import kr.trycatch.device_auth_demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final DeviceRepository deviceRepository;

    @Transactional
    public TokenDto authenticate(String deviceId) {
        Device device = deviceRepository.findByDeviceId(deviceId)
                .orElseGet(() -> deviceRepository.save(new Device(deviceId)));

        String accessToken = jwtTokenProvider.createAccessToken(deviceId);
        String refreshToken = jwtTokenProvider.createRefreshToken(deviceId);

        device.updateRefreshToken(refreshToken);

        return new TokenDto(accessToken, refreshToken);
    }

    @Transactional
    public TokenDto refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid!");
        }

        String deviceId = jwtTokenProvider.getDeviceId(refreshToken);
        Device device = deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found!"));

        if (!refreshToken.equals(device.getRefreshToken())) {
            throw new InvalidTokenException("Refresh token is mismatch");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(deviceId);
        return new TokenDto(newAccessToken, refreshToken);
    }

}
