package OneTransitionDemo.OneTransitionDemo.Config;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Services.JWTService;
import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTService jwtService;
    private final UserService userService;

    public JwtHandshakeInterceptor(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        System.out.println("SimpleLoggingInterceptor: handshake attempt to " + request.getURI());
        try {
            if (!(request instanceof ServletServerHttpRequest)) {
                System.out.println("Handshake rejected: Not a ServletServerHttpRequest" + request.getURI());
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return false;
            }

            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

            // Extract token from Authorization header or cookie
            String token = null;
            String authHeader = servletRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                System.out.println("Token from Authorization header: " + token);
            }

            if (token == null && servletRequest.getCookies() != null) {
                for (Cookie cookie : servletRequest.getCookies()) {
                    if ("jwt".equals(cookie.getName())) {
                        token = cookie.getValue();
                        System.out.println("Token from cookie: " + token);
                        break;
                    }
                }
            }

            if (token == null) {
                System.out.println("Handshake rejected: No JWT token found");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // Extract username from token
            String username = null;
            try {
                username = jwtService.extractUsername(token);
            } catch (Exception e) {
                System.out.println("Handshake rejected: Failed to extract username from token");
                e.printStackTrace();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            if (username == null || username.trim().isEmpty()) {
                System.out.println("Handshake rejected: Username is missing or empty in token");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // Find user in DB
            Optional<User> optionalUser = userService.findByEmail(username);
            if (optionalUser.isEmpty()) {
                System.out.println("Handshake rejected: User not found in DB for username " + username);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
            User user = optionalUser.get();

            // Validate token
            boolean validToken = jwtService.isTokenValid(token, user);
            if (!validToken) {
                System.out.println("Handshake rejected: Token is invalid or expired");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // Success: add username attribute for WebSocket session
            attributes.put("username", username);
            System.out.println("Handshake successful: User " + username + " authenticated");
            return true;

        } catch (Exception ex) {
            // Catch any unexpected exceptions and reject handshake
            System.out.println("Handshake rejected: Unexpected error");
            ex.printStackTrace();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return false;
        }
    }


    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // Nothing needed here
    }
}
