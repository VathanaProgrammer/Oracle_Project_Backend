package OneTransitionDemo.OneTransitionDemo.Controllers;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Services.TeacherSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/teacherSubject")
public class TeacherSubjectController {
     @Autowired
     private TeacherSubjectService teacherSubjectService;
     @GetMapping
     public ResponseEntity<?> TeacherSubject(@AuthenticationPrincipal User user){
         Map<String, Object> response = teacherSubjectService.getSubjectTeacher(user.getId());
         return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
     }
}
