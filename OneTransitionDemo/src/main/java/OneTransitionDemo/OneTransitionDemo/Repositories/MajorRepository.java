package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByName(String name);
    @Query("SELECT COUNT(m) FROM Major m WHERE m.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);
}
