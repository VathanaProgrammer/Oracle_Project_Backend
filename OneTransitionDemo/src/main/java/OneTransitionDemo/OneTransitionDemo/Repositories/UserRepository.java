package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
