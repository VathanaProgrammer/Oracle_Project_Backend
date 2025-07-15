package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    // Use a fixed secret key (at least 256 bits base64 encoded)
    private static final String MY_KEY = "kdx8T7V+PqZFd+mZepqT+W/UgC+xtq9jA0eI6q0jnp8=";

    public String generateToken(String username, long expiryMillis) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryMillis))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(MY_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void setTokenCookie(HttpServletResponse response, String token, long maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeSeconds) // <-- Use dynamic maxAge
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())  // same key used for signing
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}

