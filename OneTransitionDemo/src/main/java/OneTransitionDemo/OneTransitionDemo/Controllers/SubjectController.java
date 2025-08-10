package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Request.SubjectRequest;
import OneTransitionDemo.OneTransitionDemo.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<?> getAllSubjects(@AuthenticationPrincipal User user){
        Map<String, Object> response = subjectService.getAllSubjects();
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400 ).body(response);
    }

    @PostMapping
    public ResponseEntity<?> registerNewSubject(@AuthenticationPrincipal User user, @RequestBody SubjectRequest request){
        Map<String, Object> res = subjectService.createSubjectWithAcceptedMajorOrNot(request);
        return ResponseEntity.status((Boolean) res.get("success") ? 200 : 400 ).body(res);
    }

    @PutMapping
    public ResponseEntity<?> updateSubject(@AuthenticationPrincipal User user, @RequestBody SubjectRequest request){
        Map<String, Object> res = subjectService.updateSubjectWithAcceptedMajorOrNot(request);
        return ResponseEntity.status((Boolean) res.get("success") ? 200 : 400).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@AuthenticationPrincipal User user, @PathVariable Long id){
        Map<String, Object> response = subjectService.deleteWithStatus(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }
}
