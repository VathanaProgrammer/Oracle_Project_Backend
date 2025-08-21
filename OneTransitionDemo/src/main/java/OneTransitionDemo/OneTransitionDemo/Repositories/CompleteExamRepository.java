package OneTransitionDemo.OneTransitionDemo.Repositories;
import OneTransitionDemo.OneTransitionDemo.Models.CompleteExam;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CompleteExamRepository extends JpaRepository<CompleteExam, Long> {

    // Find all completed exams by a specific user
    List<CompleteExam> findByUser(User user);

    // Find all completions for a specific exam
    List<CompleteExam> findByExam(Exam exam);

    // Optional: find by user and exam (for checking if already completed)
    CompleteExam findByUserAndExam(User user, Exam exam);

    // Corrected method:
    List<CompleteExam> findByExamId(Long id);

    // In ExamSubmissionRepository
    boolean existsByExamIdAndUserId(Long examId, Long studentId);


    List<CompleteExam> findByUserId(Long userId);

    @Query("""
    SELECT DISTINCT ce.exam 
    FROM CompleteExam ce
    JOIN AnswerFeedback af 
      ON af.exam.id = ce.exam.id 
     AND af.user.id = ce.user.id
    WHERE ce.user.id = :userId
""")
    List<Exam> findCompletedAndGradedExams(@Param("userId") Long userId);

}

