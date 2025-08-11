package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Request.AdminDepartmentRequest;
import OneTransitionDemo.OneTransitionDemo.Request.AdminMajorRequest;
import OneTransitionDemo.OneTransitionDemo.Services.AdminDepartmentService;
import OneTransitionDemo.OneTransitionDemo.Services.AdminMajorService;
import OneTransitionDemo.OneTransitionDemo.Services.TeacherService;
import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private AdminDepartmentService adminDepartmentService;
    @Autowired
    private AdminMajorService adminMajorService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminInfo(@AuthenticationPrincipal User user,@PathVariable Long id){
        Map<String, Object> response = userService.getAdminById(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @GetMapping("/dashboard-stats")
    public Map<String, Object> getDashboardStats(@AuthenticationPrincipal User user) {
        Map<String, Object> result = new HashMap<>();

        result.put("totalStudents", userRepository.countByRole(Role.STUDENT));
        result.put("totalTeachers", userRepository.countByRole(Role.TEACHER));
        result.put("totalAdmins", userRepository.countByRole(Role.ADMIN));

        // Get first 4 teacher profile pics
        List<String> teacherPics = userRepository.findTop4ByRoleOrderByIdDesc(Role.TEACHER)
                .stream()
                .map(User::getProfilePicture)
                .toList();

        List<String> adminPics = userRepository.findTop4ByRoleOrderByIdDesc(Role.ADMIN)
                        .stream()
                        .map(User::getProfilePicture)
                        .toList();
        List<String> studentPics = userRepository.findTop4ByRoleOrderByIdDesc(Role.STUDENT)
                .stream()
                .map(User::getProfilePicture)
                .toList();

        result.put("teacherPics", teacherPics);
        result.put("adminPics", adminPics);
        result.put("studentPics", studentPics);

        return result;
    }

    @GetMapping("/getTeacherForAddingDepartment")
    public ResponseEntity<?> getTeacherForAddingDepartment(){
        Map<String, Object> response = teacherService.getTeacherToAddWithDepartment();
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @PostMapping("/createDepartment")
    public ResponseEntity<?> createDepartment(@AuthenticationPrincipal User user, @RequestBody AdminDepartmentRequest request){
        Map<String, Object> res = adminDepartmentService.createDepartmentWithAcceptNullTeacherOrMajor(request);
        return ResponseEntity.status((Boolean) res.get("success") ? 200 : 400).body(res);
    }

    @PostMapping("/createMajor")
    public ResponseEntity<?> createMajor(@AuthenticationPrincipal User user, @RequestBody AdminMajorRequest request){
        Map<String, Object> response = adminMajorService.createMajor(request);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @PutMapping("/updateMajor")
    public ResponseEntity<?> updateMajor(@AuthenticationPrincipal User user, @RequestBody AdminMajorRequest request){
        Map<String, Object> response = adminMajorService.updateMajor(request);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @DeleteMapping("/deleteMajor/{id}")
    public ResponseEntity<?> deleteMajor(@AuthenticationPrincipal User user, @PathVariable Long id){
        Map<String, Object> response = adminMajorService.deleteMajor(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }
}
