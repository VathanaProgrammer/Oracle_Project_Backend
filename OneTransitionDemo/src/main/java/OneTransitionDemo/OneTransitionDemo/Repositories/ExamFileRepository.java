package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.ExamFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamFileRepository extends JpaRepository<ExamFile, Long> {
}
