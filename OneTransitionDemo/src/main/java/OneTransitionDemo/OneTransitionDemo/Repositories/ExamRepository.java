package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.ENUMS.ExamType;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Status;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByStatus(Status status);
    @Query("""
          SELECT DISTINCT e 
          FROM Exam e 
          LEFT JOIN FETCH e.questions q 
          WHERE e.id = :id
        """)
    Optional<Exam> findExamWithQuestions(@Param("id") Long id);
    @Query("""
    SELECT DISTINCT e
    FROM Exam e
    LEFT JOIN FETCH e.questions q
    LEFT JOIN FETCH e.assignedTo a
    LEFT JOIN FETCH a.major
    LEFT JOIN FETCH a.subject
    WHERE e.id = :id
""")
    Optional<Exam> findExamWithQuestionsForAdmin(@Param("id") Long id);



}
