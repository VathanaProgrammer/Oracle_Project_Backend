package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.AssignedToDTO;
import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;
import OneTransitionDemo.OneTransitionDemo.Services.TeachingAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private TeachingAssignmentService teachingAssignmentService;

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<AssignedToDTO>> getAssignmentsForTeacher(@PathVariable Long teacherId) {
        System.out.println("search for teacher id: "+ teacherId);
        List<AssignedToDTO> assignments = teachingAssignmentService.getAssignmentsForTeacher(teacherId);
        return ResponseEntity.ok(assignments);
    }
}
