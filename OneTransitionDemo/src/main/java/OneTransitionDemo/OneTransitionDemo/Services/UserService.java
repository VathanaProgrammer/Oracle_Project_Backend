package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.UserDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.UserSummaryDTO;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.Student;
import OneTransitionDemo.OneTransitionDemo.Models.Teacher;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeacherRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;
    private final UserActionService userActionService;

    @Value("${upload.path.profile}")
    private String uploadPath;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, TeacherRepository teacherRepo, StudentRepository studentRepo, PasswordEncoder passwordEncoder, UserActionService userActionService) {
        this.userRepo = userRepo;
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
        this.userActionService = userActionService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

//    public Optional<User> findByUsername(String username) {
//        return userRepo.findByUsername(username);
//    }

    public Optional<User> findByEmail(String Email){
        return userRepo.findByEmail((Email));
    }

    public Map<String, Object> registerUser(String firstName, String lastName, String password, String role, String email, MultipartFile profileImage, String gender) throws IOException {
        if (userRepo.existsByEmail(email)) {
            return ResponseUtil.error("This email is already use");
        }

        User user = new User();
        user.setFirstname(firstName);
        user.setGender(gender);
        user.setLastname(lastName);
        user.setRole(Role.valueOf(role.toUpperCase()));
        String encoder = passwordEncoder.encode(password);
        user.setPassword(encoder);
        user.setEmail(email);

        if (profileImage != null && !profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path imagePath = uploadDir.resolve(filename);
            Files.copy(profileImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture(filename);
        }

        User savedUser = userRepo.save(user);

        // Handle role-specific creation
        switch (user.getRole()) {
            case TEACHER:
                Teacher teacher = new Teacher();
                teacher.setUser(savedUser);
                teacherRepo.save(teacher);
                break;

            case STUDENT:
                Student student = new Student();
                student.setUser(savedUser);
                studentRepo.save(student);
                break;

            case ADMIN:
                // No extra entity needed
                break;

            default:
                throw new IllegalArgumentException("Unsupported role: " + role);
        }

        return ResponseUtil.success("User registered successfully");
    }


//    public List<UserDTO> getAllUsers() {
//        List<User> users = userRepo.findAll();
//        System.out.println("Users found: " + users.size()); // Log the size
//        return users.stream()
//                .map(UserDTO::new) // Convert User to UserDTO
//                .collect(Collectors.toList());
//    }

//    public List<UserDTO> getUserFirends(Long userId){
//        return userRepo.findFriendsOfUser(userId)
//                .stream()
//                .map(UserDTO::new)
//                .collect(Collectors.toList());
//    }
//
//    public List<UserDTO> getNoUserFriends(Long UserId){
//        return userRepo.findNotFriendsOfUser(UserId)
//                .stream()
//                .map(UserDTO::new)
//                .collect(Collectors.toList());
//    }


    public boolean existsById(Long receiverId) {
        return userRepo.findById(receiverId).isPresent();
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Map<String, Object> updateUser(Long id, String firstName, String lastName, String role, String email, MultipartFile profileImage, String gender) throws IOException {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!user.getEmail().equals(email) && userRepo.existsByEmail(email)) {
            return ResponseUtil.error("This email is already use");
        }

        Role oldRole = user.getRole();
        Role newRole = Role.valueOf(role.toUpperCase());

        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setGender(gender);
        user.setRole(newRole);
        user.setEmail(email);

        if (profileImage != null && !profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path imagePath = uploadDir.resolve(filename);
            Files.copy(profileImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture(filename);
        }

        userRepo.save(user);

        // Handle role-specific entity management
        if (!oldRole.equals(newRole)) {
            // Clean up old role entities
            switch (oldRole) {
                case TEACHER:
                    teacherRepo.findByUserId(id).ifPresent(teacherRepo::delete);
                    break;
                case STUDENT:
                    studentRepo.findByUserId(id).ifPresent(studentRepo::delete);
                    break;
                // No cleanup needed for ADMIN
            }

            // Create a new role entity if needed
            switch (newRole) {
                case TEACHER:
                    Teacher teacher = new Teacher();
                    teacher.setUser(user);
                    teacherRepo.save(teacher);
                    break;
                case STUDENT:
                    Student student = new Student();
                    student.setUser(user);
                    student.setName(firstName + " " + lastName);
                    studentRepo.save(student);
                    break;
                // No creation needed for ADMIN
            }
        }

        return ResponseUtil.success("User updated successfully");
    }


    public List<UserDTO> getAllUsers(Role role, String search, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<User> usersPage;

        if (role != null && search != null && !search.isBlank()) {
            usersPage = userRepo.searchByRole(role, search, pageable);
        } else if (role != null) {
            usersPage = userRepo.findByRole(role, pageable);
        } else if (search != null && !search.isBlank()) {
            usersPage = userRepo.searchAllRoles(search, pageable);
        } else {
            usersPage = userRepo.findAll(pageable);
        }

        return usersPage.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public Map<String, Object> getUserSummary(Long id){
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }

        UserSummaryDTO dto = new UserSummaryDTO(userOpt.get());
        return ResponseUtil.success("User found!", dto);
    }

    public Map<String, Object> changeUserPassword(Long id, String oldPassword, String newPassword){
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }
        User user = userOpt.get();

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            return ResponseUtil.error("Old password is incorrect!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return ResponseUtil.success("Password changed successfully!");
    }

    public Map<String, Object> changePasswordForAdmin(Long id, Long adminId, String newPassword){
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }
        User user = userOpt.get();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        userActionService.recordAction(user.getId(), "password_change", "Admin changed password for user", "Admin changed password for user", user.getFirstname(), user.getLastname());
        return ResponseUtil.success("Password changed successfully!");
    }

}
