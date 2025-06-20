package com.example.ADMS.repository;

import com.example.ADMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User as u where u.email = ?1 or u.username = ?1 and u.active = true")
    Optional<User> findByUsernameOrEmailActive(String userEmail);

    Optional<User> findByPhoneAndActiveTrue(String phone);

    @Query("select u.username from User u where u.id = :id")
    String findUsernameByUserId(Long id);

    @Query("select distinct r.id from User u join u.userRoleList ur join ur.role r where u.active = true and ur.active = true and r.active = true and u.id = :userId")
    List<Long> findRoleIdBy(Long userId);

    @Query("select u from User u where u.phone = :phone and u.id != :userIdPresent")
    Optional<User> findByPhone(String phone, Long userIdPresent);
}
