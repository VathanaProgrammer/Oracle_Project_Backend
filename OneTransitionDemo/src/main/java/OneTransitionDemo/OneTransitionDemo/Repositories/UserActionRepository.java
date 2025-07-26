package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.DTO.UserActionDTO;
import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {
    List<UserAction> findTop10ByOrderByTimestampDesc();

    List<UserAction> findByUserId(Long userId);
}
