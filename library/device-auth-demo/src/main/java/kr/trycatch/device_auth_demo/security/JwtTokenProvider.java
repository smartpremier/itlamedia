package kr.trycatch.device_auth_demo.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long ACCESS_TOKEN_VALIDITY = 3600000; // 1 hour
    private final long REFRESH_TOKEN_VALIDITY = 2592000000L; // 30 days
//    private final long ACCESS_TOKEN_VALIDITY = 3600; // 1 min
//    private final long REFRESH_TOKEN_VALIDITY = 7200; // 2 min

    @PostConstruct
    protected void init() {
        // TODO: getBytes(StandardCharsets.UTF_8)와 그냥 getBytes()의 차이는 무엇인가?
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String deviceId) {
        return createToken(deviceId, ACCESS_TOKEN_VALIDITY);
    }

    public String createRefreshToken(String deviceId) {
        return createToken(deviceId, REFRESH_TOKEN_VALIDITY);
    }

    private String createToken(String deviceId, long validityInMilliseconds) {
        Claims claims = Jwts.claims().setSubject(deviceId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getDeviceId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
