package OneTransitionDemo.OneTransitionDemo.Repositories;
import OneTransitionDemo.OneTransitionDemo.Models.CompleteExam;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompleteExamRepository extends JpaRepository<CompleteExam, Long> {

    // Find all completed exams by a specific user
    List<CompleteExam> findByUser(User user);

    // Find all completions for a specific exam
    List<CompleteExam> findByExam(Exam exam);

    // Optional: find by user and exam (for checking if already completed)
    CompleteExam findByUserAndExam(User user, Exam exam);
}

