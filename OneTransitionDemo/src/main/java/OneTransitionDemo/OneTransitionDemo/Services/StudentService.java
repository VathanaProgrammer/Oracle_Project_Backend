package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.StudentDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Major;
import OneTransitionDemo.OneTransitionDemo.Models.Student;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.MajorRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
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
    private UserRepository userRepository;
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

    public Map<String, Object> updateForAdmin(Long userId, String firstName, String lastName, String email, String phone, String gender, Long major, MultipartFile profileImage) {
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

        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setGender(gender);

        student.setMajor(majorObj);

        studentRepository.save(student);

        userRepository.save(user);  // <-- important step to persist changes

        return ResponseUtil.success("User information updated successfully!");
    }

}
