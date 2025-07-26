package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.StudentDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Student;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

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

    public Map<String, Object> updateForAdmin(Long userId, String firstName, String lastName, String email, String phone, String gender) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }

        User user = optionalUser.get();

        // Optional: check if another user already uses the new email
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            return ResponseUtil.error("Email already in use by another user");
        }

        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setGender(gender);

        userRepository.save(user);  // <-- important step to persist changes

        return ResponseUtil.success("User information updated successfully!");
    }

}
