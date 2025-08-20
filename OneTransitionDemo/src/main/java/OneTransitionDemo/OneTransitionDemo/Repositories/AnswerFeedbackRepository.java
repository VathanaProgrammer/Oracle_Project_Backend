package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.AnswerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerFeedbackRepository extends JpaRepository<AnswerFeedback, Long> {
    Optional<AnswerFeedback> findByUserIdAndExam_IdAndQuestion_Id(Long userId, Long examId, Long questionId);
}
