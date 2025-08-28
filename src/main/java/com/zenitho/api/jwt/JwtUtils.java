package com.zenitho.api.jwt;

import com.zenitho.api.entities.User;
import com.zenitho.api.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private UserRepository userRepository;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static final long JWT_EXPIRATION_MS = 604800000; // 7 días

    public String generateJwtToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Aquí puedes añadir logs para ver por qué falló la validación (token expirado, firma inválida, etc.)
            return false;
        }
    }

    // Nuevo método para obtener el ID del usuario del token
    public Long getUserIdFromToken(String token) {
        String email = getEmailFromJwtToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email from token: " + email));
        return user.getId();
    }

}
