package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.CompleteExamDTO;
import OneTransitionDemo.OneTransitionDemo.Models.CompleteExam;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Models.Student;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.CompleteExamRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.ExamRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.StudentAnswerRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CompleteExamService {

    private final CompleteExamRepository completeExamRepository;
    private final ExamRepository examRepository;
    private final StudentAnswerRepository studentAnswerRepository; // track who submitted

    public CompleteExamService(
            CompleteExamRepository completeExamRepository,
            ExamRepository examRepository,
            StudentAnswerRepository studentAnswerRepository
    ) {
        this.completeExamRepository = completeExamRepository;
        this.examRepository = examRepository;
        this.studentAnswerRepository = studentAnswerRepository;
    }

    public Map<String, Object> getAllCompletedExams (Long id) {
        List<CompleteExamDTO> dto= completeExamRepository.findByExamId(id)
                .stream().map(CompleteExamDTO::new).toList();
        return ResponseUtil.success("Fuck",dto);
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // run every 60 seconds
    public void insertCompletedExams() {
        LocalDateTime now = LocalDateTime.now();
        List<Exam> exams = examRepository.findAll();

        for (Exam exam : exams) {
            // Find students who submitted answers for this exam
            List<Student> submittedStudents = studentAnswerRepository.findDistinctStudentsByExam(exam);
            for (Student student : submittedStudents) {
                User user = student.getUser(); // Assuming Student has a getUser() method
                // Only insert if not already recorded
                if (completeExamRepository.findByUserAndExam(user, exam) == null) {
                    CompleteExam completeExam = new CompleteExam(user, exam, now);
                    completeExamRepository.save(completeExam);
                }
            }
        }
    }
}
