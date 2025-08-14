package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.AssignedToDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.TeachingAssignmentDTO;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Services.TeachingAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teaching-assignments")
public class TeachingAssignmentController {

    @Autowired
    private TeachingAssignmentService teachingAssignmentService;

    @GetMapping
    public List<TeachingAssignmentDTO> getALLTeachingAssignMent(@AuthenticationPrincipal User user){
        List<TeachingAssignmentDTO> assignments = teachingAssignmentService.getAssignments();
        return assignments;
    }

    @PostMapping
    public ResponseEntity<?> changeTeacherAssignment(@AuthenticationPrincipal User user, @RequestBody Map<String, Long> assignmentMap){
        System.out.println("change teacher assignment: " + assignmentMap);
        Map<String, Object> response = teachingAssignmentService.changeTeacherAssignment(assignmentMap.get("teachingAssignmentId"), assignmentMap.get("teacherId"));
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }
}
