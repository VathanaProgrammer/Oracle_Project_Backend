package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.AnswerFeedbackDTO;
import OneTransitionDemo.OneTransitionDemo.Models.AnswerFeedback;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import OneTransitionDemo.OneTransitionDemo.Services.AnswerFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/answer-feedback")
public class AnswerFeedBackController {
    @Autowired
    private AnswerFeedbackService answerFeedbackService;

    @PostMapping("/create")
    public ResponseEntity<List<AnswerFeedback>> createAnswerFeedback(
            @RequestBody List<AnswerFeedbackDTO> feedbackList) {

        List<AnswerFeedback> savedFeedbacks = answerFeedbackService.createMultipleFeedbacks(feedbackList);

        if (!savedFeedbacks.isEmpty()) {
            return ResponseEntity.ok(savedFeedbacks);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}


