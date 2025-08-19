package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.CompleteExamDTO;
import OneTransitionDemo.OneTransitionDemo.Models.CompleteExam;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.CompleteExamRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.ExamRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Services.CompleteExamService;
import OneTransitionDemo.OneTransitionDemo.Services.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/complete-exams")
public class CompletedExamController {
    @Autowired
    private  CompleteExamService completeExamService;
    @Autowired
    private  ExamRepository examRepository;
    @Autowired
    private  CompleteExamRepository completeExamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentAnswerService studentAnswerService;
    private CompleteExamDTO completeExamRequest;
    @PostMapping("/insert")
    public ResponseEntity<?> insertCompleteExam(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        Long examId = payload.get("examId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        CompleteExam completeExam = new CompleteExam();
        completeExam.setUser(user);
        completeExam.setExam(exam);
        completeExam.setCompletedAt(LocalDateTime.now());

        completeExamRepository.save(completeExam);

        return ResponseEntity.ok("CompleteExam saved successfully");
    }

    @GetMapping("/{examId}/submitted-students")
    public ResponseEntity<?> getSubmittedStudents(@AuthenticationPrincipal User user, @PathVariable Long examId) {
        Map<String, Object> res =  completeExamService.getAllCompletedExams(examId);
        return ResponseEntity.status((Boolean) res.get("success") ? 200 : 400).body(res);
    }

    @GetMapping("/{id}/viewAnswer/{userId}")
    public ResponseEntity<?> getAnswer(@AuthenticationPrincipal User user, @PathVariable Long id, @PathVariable Long userId){
        Map<String, Object> response = studentAnswerService.getStudentAnswersWithQuestions(userId, id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }
}
