package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByIsDeletedFalse();
}
