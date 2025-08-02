package OneTransitionDemo.OneTransitionDemo.Repositories;

import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstname) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> searchAllRoles(@Param("search") String search, Pageable pageable);

    Page<User> findByRole(Role role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = :role AND (u.firstname LIKE %:search% OR u.lastname LIKE %:search%)")
    Page<User> searchByRole(@Param("role") Role role, @Param("search") String search, Pageable pageable);

    long countByRole(Role role);

    List<User> findTop4ByRoleOrderByIdDesc(Role role);

}
