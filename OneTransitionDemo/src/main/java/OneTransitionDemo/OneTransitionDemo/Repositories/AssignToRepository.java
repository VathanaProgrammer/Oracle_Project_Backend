package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignToRepository  extends JpaRepository<AssignedTo, Long> {
}
