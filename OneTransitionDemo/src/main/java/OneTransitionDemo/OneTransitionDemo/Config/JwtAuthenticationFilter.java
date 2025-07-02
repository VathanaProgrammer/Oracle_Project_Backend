package OneTransitionDemo.OneTransitionDemo.Config;

import OneTransitionDemo.OneTransitionDemo.Services.JWTService;
import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        String email = null;

        // 1) Try Authorization header first
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("Token from header: " + token);
        }

        // 2) If no header, try the 'jwt' cookie
        if (token == null && request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("Token from cookie: " + token);
                    break;
                }
            }
        }

        // 3) If we have a token, validate and set authentication
        if (token != null) {
            try {
                email = jwtService.extractUsername(token);
            } catch (JwtException ex) {
                // invalid/expired token → skip authentication
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(email);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        }

        // 4) Continue the filter chain
        filterChain.doFilter(request, response);
    }

}


