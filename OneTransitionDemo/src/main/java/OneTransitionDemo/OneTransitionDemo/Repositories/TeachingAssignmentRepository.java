package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.DTO.AssignedToDTO;
import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;
import OneTransitionDemo.OneTransitionDemo.Models.TeachingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, Long> {
    List<TeachingAssignment> findByTeacherId(Long teacherId);

}
