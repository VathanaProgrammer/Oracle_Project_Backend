package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long id);
}
