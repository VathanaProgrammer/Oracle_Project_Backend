package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.ENUMS.ExamType;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Status;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Models.Student;
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

    // Find exams that a user has NOT completed
    @Query("SELECT e FROM Exam e WHERE e.id NOT IN " +
            "(SELECT ce.exam.id FROM CompleteExam ce WHERE ce.user.id = :userId)")
    List<Exam> findExamsNotCompletedByUser(@Param("userId") Long userId);

    @Query("""
        SELECT e 
        FROM Exam e
        WHERE e.assignedTo.batch = :#{#student.batch}
          AND e.assignedTo.semester = :#{#student.semester}
          AND e.assignedTo.shift = :#{#student.shift}
          AND e.assignedTo.major = :#{#student.major}
          AND e.assignedTo.location = :#{#student.location}
          AND e.assignedTo.academicYear.id = :#{#student.year}
          AND e.id NOT IN (
              SELECT ce.exam.id 
              FROM CompleteExam ce 
              WHERE ce.user.id = :userId
          )
    """)
    List<Exam> findAvailableExamsForStudent(@Param("student") Student student, @Param("userId") Long userId);

    // Average score
    @Query("""
        SELECT AVG(af.score) 
        FROM AnswerFeedback af
        WHERE af.user.id = :userId
    """)
    Double findAverageScore(@Param("userId") Long userId);

    // Count completed exams
    @Query("""
        SELECT COUNT(DISTINCT ce.exam.id)
        FROM CompleteExam ce
        WHERE ce.user.id = :userId
    """)
    Long countCompletedExams(@Param("userId") Long userId);


    @Query("SELECT DISTINCT e FROM Exam e JOIN e.questions q JOIN StudentAnswer sa ON sa.question = q WHERE sa.student.id = :studentId")
    List<Exam> findExamsByStudentId(@Param("studentId") Long studentId);

}
