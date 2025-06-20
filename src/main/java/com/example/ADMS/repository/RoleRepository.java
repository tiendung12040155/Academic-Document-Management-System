package com.example.ADMS.repository;

import com.example.ADMS.entity.Role;
import com.example.ADMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select ur.role from UserRole ur where ur.user = :user and ur.active = true and ur.role.active = true")
    List<Role> findAllByUserAndActive(User user);

    Optional<Role> findByIdAndActiveTrue(Long roleId);

    @Query("select r from Role r where r.name = :roleName and r.id != :roleIdPresent")
    public Optional<Role> findByName(String roleName, Long roleIdPresent);

    @Query("select r from Role r where r.active = true")
    public List<Role> findByRoleActive();
}
