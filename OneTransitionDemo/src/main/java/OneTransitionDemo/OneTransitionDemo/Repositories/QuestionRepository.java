package OneTransitionDemo.OneTransitionDemo.Repositories;
import OneTransitionDemo.OneTransitionDemo.Models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Fetch all questions that belong to a specific exam
    @Query("SELECT q FROM Question q JOIN q.exams e WHERE e.id = :examId")
    List<Question> findByExamId(@Param("examId") Long examId);

    List<Question> findByExamsId(Long examId);
}

