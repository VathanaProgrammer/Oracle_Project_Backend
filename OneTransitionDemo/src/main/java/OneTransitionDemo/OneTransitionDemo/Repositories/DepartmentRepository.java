package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.teachers t")
    List<Department> findAllWithTeachers();

}
