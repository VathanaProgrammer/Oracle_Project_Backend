package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.DetailedResultDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.ResultDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.StudentExamSummaryDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Result;
import OneTransitionDemo.OneTransitionDemo.Models.TeachingAssignment;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.ResultRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeachingAssignmentRepository;
import OneTransitionDemo.OneTransitionDemo.Services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private TeachingAssignmentRepository teachingAssignmentRepository;

    @GetMapping
    public List<ResultDTO> getResults(@RequestParam Long assignmentId, @RequestParam Long examId) {
        return resultService.getResultsByAssignmentAndExam(assignmentId, examId);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<ResultDTO> getResultsByTeacher(@PathVariable Long teacherId) {
        // Find all assignments assigned to this teacher
        List<TeachingAssignment> assignments = teachingAssignmentRepository.findByTeacherId(teacherId);

        // Extract the assignment IDs
        List<Long> assignmentIds = assignments.stream()
                .map(a -> a.getAssignedTo().getId())
                .collect(Collectors.toList());

        // Fetch all results linked to these assignments
        List<Result> results = resultRepository.findByAssignmentIdIn(assignmentIds);

        // Map to DTO
        return results.stream()
                .map(ResultDTO::new)
                .toList();
    }

    @GetMapping("/student-summary")
    public ResponseEntity<List<StudentExamSummaryDTO>> getStudentSummary(@AuthenticationPrincipal User user) {
        Long studentId = user.getId();
        List<StudentExamSummaryDTO> studentExamSummaryDTOS = resultService.getStudentExamSummary(studentId);
        return ResponseEntity.ok(studentExamSummaryDTOS);
    }

}

