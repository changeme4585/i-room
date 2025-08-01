package com.iroom.user.common.jwt;

import com.iroom.user.admin.entity.Admin;
import com.iroom.user.system.entity.SystemAccount;
import com.iroom.user.worker.entity.Worker;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component("userJwtTokenProvider")
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtTokenProvider(@Value("${JWT_SECRET}") String secretKey, @Value("${JWT_EXPIRATION}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String createAdminToken(Admin admin) {
        Claims claims = Jwts.claims().setSubject(admin.getId().toString());
        claims.put("email", admin.getEmail());
        claims.put("role", admin.getRole().getKey());
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createWorkerToken(Worker worker) {
        Claims claims = Jwts.claims().setSubject(worker.getId().toString());
        claims.put("email", worker.getEmail());
        claims.put("role", worker.getRole().getKey());
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createSystemToken(SystemAccount system) {
        Claims claims = Jwts.claims().setSubject(system.getId().toString());
        claims.put("name", system.getName());
        claims.put("role", system.getRole().getKey());
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
