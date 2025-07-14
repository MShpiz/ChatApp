package com.example.AuthService.services;

import com.example.AuthService.storage.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey jwtSigningKey;

    public JwtService() {
        // генерируем ключ для подписи токенов
        jwtSigningKey = Jwts.SIG.HS256.key().build();
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
        }
        return generateToken(claims, userDetails);
    }

    // метод получения данных из полезной нагрузки токена
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims) // дополнительные данные
                .setSubject(userDetails.getUsername()) // имя пользователя которому выдан токен
                .setIssuedAt(new Date(System.currentTimeMillis())) // время создания токена
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24)) // когда токен будет не валидным
                .signWith(jwtSigningKey, SignatureAlgorithm.HS256) // подписываем токен
                .compact();
    }

    // проверка валидности токена
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (SignatureException e) {
            return true;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // получение всех данных из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSigningKey).build().parseClaimsJws(token).getBody();
    }
}
