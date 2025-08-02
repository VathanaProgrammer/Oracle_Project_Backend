package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentRepository;
import OneTransitionDemo.OneTransitionDemo.Request.StudentForAdminRequest;
import OneTransitionDemo.OneTransitionDemo.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> updateStuInfo(@AuthenticationPrincipal User user, @RequestBody StudentForAdminRequest request){
        System.out.println("userId: " + request.getUserId());
        Map<String, Object> response = studentService.updateForAdmin(request.getUserId(), request.getFirstName(), request.getLastName(),request.getEmail(), request.getPhone(), request.getGender());
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }



}
