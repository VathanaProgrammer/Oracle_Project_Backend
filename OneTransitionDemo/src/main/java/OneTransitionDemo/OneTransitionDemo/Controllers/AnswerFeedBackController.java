package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.AnswerFeedbackDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.DetailedResultDTO;
import OneTransitionDemo.OneTransitionDemo.Models.AnswerFeedback;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import OneTransitionDemo.OneTransitionDemo.Services.AnswerFeedbackService;
import OneTransitionDemo.OneTransitionDemo.Services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/answer-feedback")
public class AnswerFeedBackController {
    @Autowired
    private AnswerFeedbackService answerFeedbackService;
    @Autowired
    private ResultService resultService;

    @PostMapping("/create")
    public ResponseEntity<?>  createAnswerFeedback(
            @RequestBody List<AnswerFeedbackDTO> feedbackList) {

        Map<String, Object> savedFeedbacks = answerFeedbackService.createMultipleFeedbacks(feedbackList);

       return ResponseEntity.status((Boolean) savedFeedbacks.get("success") ? 200 : 400).body(savedFeedbacks);
    }

    @GetMapping("/student/{studentId}/exam/{examId}")
    public ResponseEntity<DetailedResultDTO> getStudentExamFeedbacks(
            @PathVariable Long studentId,
            @PathVariable Long examId) {

        DetailedResultDTO feedback = resultService.getDetailedResult(studentId, examId);
        return ResponseEntity.ok(feedback);
    }

}
