package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.AnswerFeedbackDTO;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.*;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnswerFeedbackService {

    private final AnswerFeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private AnswerFeedbackRepository feedbackRepo;

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
    public Map<String, Object> createMultipleFeedbacks(List<AnswerFeedbackDTO> dtoList) {
        List<AnswerFeedback> savedFeedbacks = new ArrayList<>();

        for (AnswerFeedbackDTO dto : dtoList) {
            AnswerFeedback feedback = new AnswerFeedback();
            feedback.setScore(dto.getScore());

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            feedback.setUser(user);

            Exam exam = examRepository.findById(dto.getExamId())
                    .orElseThrow(() -> new RuntimeException("Exam not found"));
            feedback.setExam(exam);

            Question question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            feedback.setQuestion(question);

            savedFeedbacks.add(feedbackRepository.save(feedback));
        }

        // Update Result table
        Map<String, List<AnswerFeedback>> feedbackByStudentExam = savedFeedbacks.stream()
                .collect(Collectors.groupingBy(f -> f.getUser().getId() + "-" + f.getExam().getId()));

        for (List<AnswerFeedback> list : feedbackByStudentExam.values()) {
            AnswerFeedback anyFeedback = list.get(0);
            double totalScore = list.stream().mapToDouble(AnswerFeedback::getScore).sum();

            Result result = resultRepository.findByStudentAndExam(anyFeedback.getUser(), anyFeedback.getExam())
                    .orElse(new Result());

            result.setStudent(anyFeedback.getUser());
            result.setExam(anyFeedback.getExam());
            result.setAssignment(anyFeedback.getExam().getAssignedTo());
            result.setScore(totalScore);
            result.setSubmittedAt(LocalDateTime.now());

            resultRepository.save(result);
        }

        return ResponseUtil.success("Feedbacks created and results updated!");
    }

    public List<AnswerFeedback> getFeedbacksForStudentExam(Long studentId, Long examId) {
        return feedbackRepo.findByUserIdAndExamId(studentId, examId);
    }

    public List<AnswerFeedback> findByUserIdAndExamId(Long studentId, Long examId) {
        return feedbackRepo.findByUserIdAndExamId(studentId, examId);
    }
}
