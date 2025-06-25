package com.example.ADMS.repository;

import com.example.ADMS.entity.Role;
import com.example.ADMS.entity.User;
import com.example.ADMS.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("select ur.user from UserRole ur where ur.role.id = 1 and " +
            " ( (ur.user.username like %:text%) or (ur.user.email like %:text%) ) and ur.user.id != :userId")
    List<User> findTeacherByUsernameOrEmailContaining(String text, Long userId);

    @Query("select ur from UserRole ur where ur.active = true and ur.user = :user")
    Set<UserRole> getUserRoleByActiveAndUser(User user);

    Optional<UserRole> findByUserAndRole(User user, Role role);

}
