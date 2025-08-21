package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Models.Result;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByAssignmentIdAndExamId(Long assignmentId, Long examId);

    @Query("""
        SELECT r
        FROM Result r
        JOIN TeachingAssignment t ON t.assignedTo = r.assignment
        WHERE t.teacher.id = :teacherId
    """)
    List<Result> findResultsByTeacher(@Param("teacherId") Long teacherId);

    // Find a result by student and exam
    Optional<Result> findByStudentAndExam(User student, Exam exam);

    List<Result> findByAssignmentIdIn(List<Long> assignmentIds);


    List<Result> findByStudentId(Long studentId);
}

