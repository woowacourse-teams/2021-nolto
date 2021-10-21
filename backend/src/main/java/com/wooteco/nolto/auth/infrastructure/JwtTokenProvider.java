package com.wooteco.nolto.auth.infrastructure;

import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.access-token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.access-token.expire-length}")
    private long accessTokenExpiredInMilliseconds;
    @Value("${security.jwt.refresh-token.expire-length}")
    private long refreshTokenExpiredInMilliseconds;

    private JwtTokenProvider() {
    }

    public TokenResponse createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpiredInMilliseconds);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return new TokenResponse(accessToken, accessTokenExpiredInMilliseconds);
    }

    public String getPayload(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public TokenResponse createRefreshToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenExpiredInMilliseconds);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return new TokenResponse(refreshToken, refreshTokenExpiredInMilliseconds);
    }
}
