package com.auth.springsecurity;


import com.auth.domain.Roles.entity.Role;
import com.auth.domain.Users.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class JwtService {

    private final SecretKey key;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;
    private final String issuer;


    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        List<String> roles = user.getRoles() == null ? List.of() :
                user.getRoles().stream().map(Role::getRoleName).toList();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .claims(Map.of(
                        "email", user.getEmail(),
                        "roles", roles,
                        "typ", "access"
                ))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // generate refreshotken.
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTtlSeconds)))
                .claim("typ", "refresh")
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    //parse the token

    public Jws<Claims> parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public boolean isAccessToken(String token) {
        Claims c = parse(token).getPayload();
        return "access".equals(c.get("typ"));
    }


    public boolean isRefreshToken(String token) {
        Claims c = parse(token).getPayload();
        return "refresh".equals(c.get("typ"));
    }

    public UUID getUserId(String token) {
        Claims c = parse(token).getPayload();
        return UUID.fromString(c.getSubject());
    }

    public String getJti(String token) {
        return parse(token).getPayload().getId();
    }

    public List<String> getRoles(String token) {
        Claims c = parse(token).getPayload();
        return (List<String>) c.get("roles");
    }

    public String getEmail(String token) {
        Claims c = parse(token).getPayload();
        return (String) c.get("email");
    }

}
