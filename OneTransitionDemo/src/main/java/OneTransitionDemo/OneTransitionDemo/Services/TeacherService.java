package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.TeacherDTO;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.Department;
import OneTransitionDemo.OneTransitionDemo.Models.Teacher;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.DepartmentRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeacherRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class TeacherService {

    @Value("${upload.path.profile}")
    private String uploadPath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public Map<String, Object> getTeacherById(Long id){
        if(id == null){
            return ResponseUtil.error("Teacher not found!");
        }

        Optional<Teacher> teacher = teacherRepository.findByUserId(id);

        TeacherDTO dto = new TeacherDTO(teacher.get());
        return ResponseUtil.success("Teacher found!", dto);
    }

    @Transactional
    public Map<String, Object> updateForAdmin(
            Long userId,
            String firstName,
            String lastName,
            String email,
            String phone,
            String gender,
            List<Long> departmentIds,
            MultipartFile profileImage
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.TEACHER) {
            return ResponseUtil.error("User is not a teacher!");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            return ResponseUtil.error("Email already in use by another user");
        }

        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setGender(gender);

        if (profileImage != null && !profileImage.isEmpty()) {
            // handle image upload as before
        }

        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher record not found"));

        // Load current departments linked to the teacher
        Set<Department> currentDepartments = new HashSet<>(teacher.getDepartments());

        // Load new departments from departmentIds
        Set<Department> newDepartments = new HashSet<>(departmentRepository.findAllById(departmentIds));

        // 1. Remove teachers from departments that are no longer linked
        for (Department dept : currentDepartments) {
            if (!newDepartments.contains(dept)) {
                dept.getTeachers().remove(teacher);
                departmentRepository.save(dept);
            }
        }

        // 2. Add teachers to new departments
        for (Department dept : newDepartments) {
            if (!dept.getTeachers().contains(teacher)) {
                dept.getTeachers().add(teacher);
                departmentRepository.save(dept);
            }
        }

        // 3. Update teacher's departments collection to new ones
        teacher.setDepartments(newDepartments);

        // Save teacher and user
        teacherRepository.save(teacher);
        userRepository.save(user);

        return ResponseUtil.success("Teacher information updated successfully!");
    }

}
