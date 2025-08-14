package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentRepository;
import OneTransitionDemo.OneTransitionDemo.Request.StudentForAdminRequest;
import OneTransitionDemo.OneTransitionDemo.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentInfo(@AuthenticationPrincipal User user,
                                            @PathVariable Long id){

        Map<String, Object> response = studentService.getStudentById(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

    @PutMapping("/update-for-admin")
    public ResponseEntity<?> updateStuInfo(
            @AuthenticationPrincipal User user,
            @RequestParam("userId") Long userId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("gender") String gender,
            @RequestParam("majorId") Long majorId,
            @RequestParam("shiftId") Long shiftId,
            @RequestParam("locationId") Long locationId,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        System.out.println("Location ID: " + locationId);
        Map<String, Object> response = studentService.updateForAdmin(
                userId, firstName, lastName, email, phone, gender, majorId, profilePicture, shiftId, locationId
        );
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }

}
