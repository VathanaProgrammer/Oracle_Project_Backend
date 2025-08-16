package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.CompleteExamRequest;
import OneTransitionDemo.OneTransitionDemo.Models.CompleteExam;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.CompleteExamRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.ExamRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Services.CompleteExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/complete-exams")
public class CompletedExamController {
    private final CompleteExamService completeExamService;
    @Autowired
    private final ExamRepository examRepository;
    @Autowired
    private  CompleteExamRepository completeExamRepository;
    @Autowired
    private UserRepository userRepository;
    public CompletedExamController(CompleteExamService completeExamService, ExamRepository examRepository){
         this.completeExamService = completeExamService;
         this.examRepository = examRepository;
    }
    private CompleteExamRequest completeExamRequest;
   @GetMapping
   public List<CompleteExam>  completeExamList(){
        return completeExamService.getAllCompletedExams();
   }
    @PostMapping(value = "/insert", consumes = "multipart/form-data")
    public ResponseEntity<?> insertCompleteExam(
            @RequestParam Long userId,
            @RequestParam Long examId) {

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


}
