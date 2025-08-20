package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.AnswerFeedbackDTO;
import OneTransitionDemo.OneTransitionDemo.Models.AnswerFeedback;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Models.Question;
import OneTransitionDemo.OneTransitionDemo.Repositories.AnswerFeedbackRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.ExamRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerFeedbackService {

    private final AnswerFeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    public AnswerFeedbackService(AnswerFeedbackRepository feedbackRepository,
                                 UserRepository userRepository,
                                 ExamRepository examRepository,
                                 QuestionRepository questionRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    public List<AnswerFeedback> createMultipleFeedbacks(List<AnswerFeedbackDTO> dtoList) {
        List<AnswerFeedback> savedFeedbacks = new ArrayList<>();

        for (AnswerFeedbackDTO dto : dtoList) {
            AnswerFeedback feedback = new AnswerFeedback();
            feedback.setScore(dto.getScore());

            // Fetch User if provided
            if (dto.getUserId() != null) {
                User user = userRepository.findById(dto.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                feedback.setUser(user);
            }

            // Fetch Exam
            Exam exam = examRepository.findById(dto.getExamId())
                    .orElseThrow(() -> new RuntimeException("Exam not found"));
            feedback.setExam(exam);

            // Fetch Question
            Question question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            feedback.setQuestion(question);

            // Save each feedback
            savedFeedbacks.add(feedbackRepository.save(feedback));
        }

        return savedFeedbacks;
    }
}
