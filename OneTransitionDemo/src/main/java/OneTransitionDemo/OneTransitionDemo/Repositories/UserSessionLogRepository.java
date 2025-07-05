package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.UserSessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserSessionLogRepository extends JpaRepository<UserSessionLog, Long> {

    @Query("SELECT COALESCE(SUM(TIMESTAMPDIFF(SECOND, s.startTime, s.endTime)), 0) " +
            "FROM UserSessionLog s WHERE s.user.id = :userId AND s.startTime >= :start AND s.endTime <= :end")
    Long getTotalSecondsSpent(@Param("userId") Long userId,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end);

    Optional<UserSessionLog> findTopByUserIdAndEndTimeIsNullOrderByStartTimeDesc(Long userId);
}

