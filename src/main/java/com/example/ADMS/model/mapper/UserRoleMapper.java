package com.example.ADMS.model.mapper;


import com.example.ADMS.entity.Role;
import com.example.ADMS.entity.User;
import com.example.ADMS.entity.UserRole;
import com.example.ADMS.utils.DateTimeHelper;

public class UserRoleMapper {
    public static UserRole toUserRole(User user, Role role) {
        return UserRole.builder()
                .user(user)
                .role(role)
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .build();
    }
}
