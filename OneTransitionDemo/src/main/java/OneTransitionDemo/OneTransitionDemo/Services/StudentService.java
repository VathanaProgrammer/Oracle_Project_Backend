package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.StudentDTO;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.*;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Value("${upload.path.profile}")
    private String uploadPath;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private BatchRepository  batchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private MajorRepository majorRepository;

    // Return all students as DTO
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }

    // Return one student by ID as DTO
    public Map<String, Object> getStudentById(Long id) {
        Optional<Student> studentOpt = studentRepository.findByUserId(id);

        if (studentOpt.isEmpty()) {
            return ResponseUtil.error("Student not found!");
        }

        StudentDTO student = new StudentDTO(studentOpt.get());
        return ResponseUtil.success("Student found!", student);
    }

    public Map<String, Object> updateForAdmin(Long userId, String firstName, String lastName, String email, String phone, String gender, Long major, MultipartFile profileImage, Long shiftId, Long locationId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }

        User user = optionalUser.get();

        Optional<Student> optionalStudent = studentRepository.findByUserId(userId);
        if (optionalStudent.isEmpty()) {
            return ResponseUtil.error("Student record not found!");
        }
        // Optional: check if another user already uses the new email
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            return ResponseUtil.error("Email already in use by another user");
        }

        Major majorObj = majorRepository.findById(major).isPresent() ? majorRepository.findById(major).get() : null;

        Student student = optionalStudent.get();

        // 4️⃣ Handle profile picture upload
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
                Path uploadDir = Paths.get(uploadPath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Path imagePath = uploadDir.resolve(filename);
                Files.copy(profileImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                user.setProfilePicture(filename);
            } catch (IOException e) {
                return ResponseUtil.error("Failed to save profile picture");
            }
        }

        Optional<Location> locationOp = locationRepository.findById(locationId);
        if (locationOp.isEmpty()) {
            return ResponseUtil.error("Location not found!");
        }
        Location location = locationOp.get();

        Optional<Shift> optionalShift = shiftRepository.findById(shiftId);
        if (optionalShift.isEmpty()) {
            return ResponseUtil.error("Shift not found!");
        }
        Shift shift = optionalShift.get();

        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setGender(gender);
        student.setShift(shift);
        student.setLocation(location);
        student.setMajor(majorObj);

        studentRepository.save(student);

        userRepository.save(user);  // <-- important step to persist changes

        return ResponseUtil.success("User information updated successfully!");
    }
    public Map<String, Object> updatePhoneAndProfile(Long userId, String phone, MultipartFile profileImage) throws IOException {
        if (userId == null) {
            return ResponseUtil.error("Student not found!");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseUtil.error("Student not found in database!");
        }

        User user = userOptional.get();

        // Update phone if provided
        if (phone != null && !phone.isEmpty()) {
            System.out.println("Updating phone to: " + phone);
            user.setPhone(phone);
        }

        // Handle profile image
        if (profileImage != null && !profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath);

            if (!Files.exists(uploadDir)) {
                System.out.println("Creating upload directory at: " + uploadDir.toAbsolutePath());
                Files.createDirectories(uploadDir);
            }

            Path imagePath = uploadDir.resolve(filename);
            System.out.println("Saving profile image to: " + imagePath.toAbsolutePath());

            Files.copy(profileImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture(filename);
        }

        userRepository.save(user);
        System.out.println("User updated successfully: " + user.getId());

        return ResponseUtil.success("Profile updated successfully");
    }


}
