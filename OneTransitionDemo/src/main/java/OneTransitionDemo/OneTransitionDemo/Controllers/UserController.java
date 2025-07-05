package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.UserSessionLog;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserSessionLogRepository;
import OneTransitionDemo.OneTransitionDemo.Request.loginRequest;
import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import OneTransitionDemo.OneTransitionDemo.Services.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserSessionLogRepository userSessionLogRepository;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") String role,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {


        User registeredUser = userService.registerUser(firstName, lastName, password, role, email, profileImage);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword())
            );

            User user = userService.findByEmail(loginRequest.getEmail()).orElseThrow();
            System.out.println("LoginController: found user id:" +user.getId()+ "\n firstname: "+user.getFirstname()+"\n" +" lastname: "+ user.getLastname());
            System.out.println("Generate token...");
            String token = jwtService.generateToken(user.getEmail());
            System.out.println("LoginController: token generated: " + token);

            jwtService.setTokenCookie(response, token); // Set as HttpOnly cookie
            System.out.println("LoginController: cookie set.");

            UserSessionLog sessionLog = new UserSessionLog();
            sessionLog.setUser(user);
            sessionLog.setStartTime(LocalDateTime.now());
            userSessionLogRepository.save(sessionLog);

            return ResponseEntity.ok(token); // Return only the token
        }  catch (BadCredentialsException ex) {             // <— Print full stack trace
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));  // <— Include exception message
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal User user) {
        // Clear the cookie
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", deleteCookie.toString());

        // ✅ Log session end
        if (user != null) {
            userSessionLogRepository
                    .findTopByUserIdAndEndTimeIsNullOrderByStartTimeDesc(user.getId())
                    .ifPresent(sessionLog -> {
                        sessionLog.setEndTime(LocalDateTime.now()); // ✅ correct setter
                        userSessionLogRepository.save(sessionLog);
                    });
        }

        return ResponseEntity.ok(Map.of("message", "logout success!"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        // Define today & week start
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime weekStart = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();

        // Query total time in seconds
        Long secondsToday = userSessionLogRepository.getTotalSecondsSpent(user.getId(), todayStart, now);
        Long secondsWeek = userSessionLogRepository.getTotalSecondsSpent(user.getId(), weekStart, now);

        // Format h/m
        String todayFormatted = formatTime(secondsToday);
        String weeklyFormatted = formatTime(secondsWeek);

        // Build response
        Map<String, Object> userData = Map.of(
                "firstname", user.getFirstname(),
                "lastname", user.getLastname(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "timeSpentToday", todayFormatted,
                "timeSpentThisWeek", weeklyFormatted
        );

        return ResponseEntity.ok(userData);
    }

    private String formatTime(Long seconds) {
        if (seconds == null || seconds <= 0) return "0h 0m";
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return hours + "h " + minutes + "m";
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test(@AuthenticationPrincipal User user){
        User existUser = userRepository.findById(user.getId()).orElseThrow();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userinfo :",  existUser);
        return ResponseEntity.ok(userInfo);
    }
}
