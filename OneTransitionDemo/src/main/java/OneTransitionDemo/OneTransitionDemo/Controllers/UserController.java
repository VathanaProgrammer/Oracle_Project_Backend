package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.UserDTO;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Models.UserSessionLog;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserSessionLogRepository;
import OneTransitionDemo.OneTransitionDemo.Request.loginRequest;
import OneTransitionDemo.OneTransitionDemo.Services.UserActionService;
import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import OneTransitionDemo.OneTransitionDemo.Services.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
import java.util.*;

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

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") String role,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {

        Map<String, Object> response;
        response = userService.registerUser(firstName, lastName, password, role, email, profileImage);

        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
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
            long expiryMillis = loginRequest.getRememberMe() ?
                    (1000L * 60 * 60 * 24 * 30) :  // 30 days
                    (1000L * 60 * 60);            // 1 hour

            String token = jwtService.generateToken(user.getEmail(), expiryMillis);
            System.out.println("LoginController: token generated: " + token);

            jwtService.setTokenCookie(response, token, expiryMillis / 1000); // Convert ms to seconds
            System.out.println("LoginController: cookie set.");

            UserSessionLog sessionLog = new UserSessionLog();
            sessionLog.setUser(user);
            sessionLog.setStartTime(LocalDateTime.now());
            userSessionLogRepository.save(sessionLog);

            UserAction recentAction = userActionService.recordAction(
                    user.getId(),
                    "login",
                    "Login",
                    user.getFirstname() +" "+  user.getLastname() + " just logined", user.getFirstname(), user.getLastname()
            );

            messagingTemplate.convertAndSend("/topic/api/actions/recent", recentAction);

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

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal User user,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") String role,
            @RequestParam("email") String email,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {

        System.out.println(user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        if (!user.getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }

        if (firstName == null || lastName == null || role == null || email == null) {
            return ResponseEntity.badRequest().body("Required fields cannot be null");
        }

        Map<String, Object> response;
        response = userService.updateUser(user.getId(), firstName, lastName, role, email, profileImage);

        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        // Define today && week start
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
                "id", user.getId(),
                "firstname", user.getFirstname(),
                "profile", user.getProfilePicture(),
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
    
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
