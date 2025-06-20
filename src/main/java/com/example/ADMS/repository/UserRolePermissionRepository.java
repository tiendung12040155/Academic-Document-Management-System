package com.example.ADMS.repository;


import com.example.ADMS.entity.Role;
import com.example.ADMS.entity.SystemPermission;
import com.example.ADMS.entity.UserRolePermission;
import com.example.ADMS.entity.type.MethodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, Long> {

    @Query("select urp from UserRolePermission urp where urp.active = true and urp.permission.active = true and urp.role.name = :roleName " +
            "and urp.permission.methodType = :methodType and urp.permission.path = :url")
    UserRolePermission needCheckPermission(String url, MethodType methodType, String roleName);

    @Query("select urp.permission from UserRolePermission urp where urp.role.id = :id and urp.active = true and urp.permission.active = true")
    List<SystemPermission> getListSystemPermissionByRole(Long id);

    @Query("select urp from UserRolePermission urp where urp.permission.id = :systemPermissionId and urp.role.id = :roleId")
    UserRolePermission findAll(Long systemPermissionId, Long roleId);

    @Query("select urp from UserRolePermission urp where urp.permission = :permission and urp.role = :role")
    Optional<UserRolePermission> findByPermissionIdAndRole(SystemPermission permission, Role role);
}
