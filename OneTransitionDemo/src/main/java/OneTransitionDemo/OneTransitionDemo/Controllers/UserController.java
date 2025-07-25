package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.UserDTO;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Models.UserSessionLog;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserSessionLogRepository;
import OneTransitionDemo.OneTransitionDemo.Request.ChangePasswordForAdminRequest;
import OneTransitionDemo.OneTransitionDemo.Request.ChangePasswordRequest;
import OneTransitionDemo.OneTransitionDemo.Request.loginRequest;
import OneTransitionDemo.OneTransitionDemo.Services.UserActionService;
import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import OneTransitionDemo.OneTransitionDemo.Services.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
            @RequestParam("gender") String gender,
            @RequestParam("role") String role,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {

        Map<String, Object> response;
        response = userService.registerUser(firstName, lastName, password, role, email, profileImage, gender);

        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword())
            );

            String ip = getClientIP(request); // STEP 2
            String location = getLocationFromIP(ip); // STEP 1
            String browser = detectBrowser(request.getHeader("User-Agent")); // STEP 3

            System.out.println("IP: " + ip);
            System.out.println("Location: " + location);
            System.out.println("Browser: " + browser);

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
            sessionLog.setBrowser(loginRequest.getBrowser());
            sessionLog.setDeviceType(loginRequest.getDeviceType());
            sessionLog.setDevice(loginRequest.getDevice());
            sessionLog.setLocation(loginRequest.getLocation());
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
    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String detectBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Brave")) return "Brave";
        if (userAgent.contains("Edg")) return "Edge";
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        return "Unknown";
    }

    public String getLocationFromIP(String ip) {
        try {
            URL url = new URL("http://ip-api.com/json/" + ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = conn.getInputStream();
            String json = new String(in.readAllBytes());
            JSONObject obj = new JSONObject(json);

            if ("success".equals(obj.getString("status"))) {
                return obj.getString("city") + ", " + obj.getString("country");
            } else {
                return "Unknown location";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown location";
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
            @RequestParam("gender") String gender,
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
        response = userService.updateUser(user.getId(), firstName, lastName, role, email, profileImage, gender);

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
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "10") int limit) {

        Role roleEnum = null;
        if (role != null && !role.equalsIgnoreCase("All Roles")) {
            try {
                roleEnum = Role.valueOf(role.toUpperCase());  // Convert String to Role enum
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Collections.emptyList()); // or custom error response
            }
        }

        List<UserDTO> users = userService.getAllUsers(roleEnum, search, limit);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/beforeDetail/{id}")
    public ResponseEntity<?> getUserSummary(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        Map<String, Object> response = userService.getUserSummary(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @PutMapping
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordRequest request)
    {
        Map<String, Object> response = userService.changeUserPassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @PutMapping("/changePasswordForAdmin")
    public ResponseEntity<?> changePasswordForAdmin(@AuthenticationPrincipal User user, @RequestBody ChangePasswordForAdminRequest request)
    {
        User admin = userService.findById(user.getId());
        Long adminId = admin.getId();
        Map<String, Object> response = userService.changePasswordForAdmin(request.getUserId(), adminId, request.getNewPassword());
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }
}
