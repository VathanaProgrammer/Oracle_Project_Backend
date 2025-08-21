package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.CompleteExamDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.ExamGRADED;
import OneTransitionDemo.OneTransitionDemo.DTO.QuestionDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.StudentAnswerDTO;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.*;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompleteExamService {

    private final CompleteExamRepository completeExamRepository;
    private final ExamRepository examRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private AnswerFeedbackRepository answerFeedbackRepository;

    @Autowired
    private QuestionRepository questionRepository;// track who submitted

    public CompleteExamService(
            CompleteExamRepository completeExamRepository,
            ExamRepository examRepository,
            StudentAnswerRepository studentAnswerRepository
    ) {
        this.completeExamRepository = completeExamRepository;
        this.examRepository = examRepository;
        this.studentAnswerRepository = studentAnswerRepository;
    }

    public Map<String, Object> getAllCompletedExams(Long examId) {
        List<CompleteExamDTO> dto = completeExamRepository.findByExamId(examId)
                .stream()
                .map(ce -> {
                    boolean feedbackSubmitted = answerFeedbackRepository.existsByExamIdAndUserId(
                            ce.getExam().getId(), ce.getUser().getId()
                    );
                    return new CompleteExamDTO(ce, feedbackSubmitted);
                })
                .toList();

        return ResponseUtil.success("Loaded submitted students", dto);
    }


    @Transactional
    @Scheduled(fixedRate = 60000) // run every 60 seconds
    public void insertCompletedExams() {
        LocalDateTime now = LocalDateTime.now();
        List<Exam> exams = examRepository.findAll();

        for (Exam exam : exams) {
            // Find users who submitted answers for this exam
            List<User> submittedUsers = studentAnswerRepository.findDistinctStudentsByExam(exam); // returns User now
            for (User user : submittedUsers) {
                // Only insert if not already recorded
                if (completeExamRepository.findByUserAndExam(user, exam) == null) {
                    CompleteExam completeExam = new CompleteExam(user, exam, now);
                    completeExamRepository.save(completeExam);
                }
            }
        }
    }

    public Map<String, Object> getViewAnswerById(Long id){
    Optional<StudentAnswer> answer = studentAnswerRepository.findById(id);
    if(answer.isEmpty()) return ResponseUtil.error("answer not found!");
    StudentAnswerDTO dto = new StudentAnswerDTO(answer.get());
    return ResponseUtil.success("CompleteExam found", dto);
    }
}
