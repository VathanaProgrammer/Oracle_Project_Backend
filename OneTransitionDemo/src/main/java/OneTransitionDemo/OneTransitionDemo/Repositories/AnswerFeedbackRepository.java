package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.AnswerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerFeedbackRepository extends JpaRepository<AnswerFeedback, Long> {
    Optional<AnswerFeedback> findByUserIdAndExam_IdAndQuestion_Id(Long userId, Long examId, Long questionId);

    List<AnswerFeedback> findByUserIdAndExamId(Long userId, Long id);

    @Query("SELECT SUM(af.score) FROM AnswerFeedback af WHERE af.user.id = :userId AND af.exam.id = :examId")
    Long sumScoreByUserAndExam(@Param("userId") Long userId, @Param("examId") Long examId);

    boolean existsByExamIdAndUserId(Long id, Long id1);


}
