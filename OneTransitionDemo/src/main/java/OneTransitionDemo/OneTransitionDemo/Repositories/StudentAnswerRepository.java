package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.DTO.CompleteExamDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Models.Student;
import OneTransitionDemo.OneTransitionDemo.Models.StudentAnswer;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {

    @Query("SELECT DISTINCT sa.student FROM StudentAnswer sa WHERE sa.exam = :exam")
    List<User> findDistinctStudentsByExam(@Param("exam") Exam exam);


    @Query("SELECT sa FROM StudentAnswer sa " +
            "JOIN FETCH sa.question q " +
            "JOIN FETCH sa.exam e " +
            "JOIN FETCH sa.student u " +          // 👈 Fetch the User
            "WHERE sa.exam.id = :examId AND u.id = :userId")
    List<StudentAnswer> findByExamIdAndUserId(@Param("examId") Long examId,
                                              @Param("userId") Long userId);

}


