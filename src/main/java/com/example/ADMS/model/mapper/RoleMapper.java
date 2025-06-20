package com.example.ADMS.model.mapper;



import com.example.ADMS.entity.Role;
import com.example.ADMS.entity.SystemPermission;
import com.example.ADMS.entity.User;
import com.example.ADMS.model.dto.request.RoleDTODisplay;
import com.example.ADMS.model.dto.request.RoleDTORequest;
import com.example.ADMS.model.dto.request.RoleDTOUpdate;
import com.example.ADMS.model.dto.response.PermissionDTOResponse;
import com.example.ADMS.model.dto.response.RoleDTODetailResponse;
import com.example.ADMS.model.dto.response.RoleDTOResponse;
import com.example.ADMS.utils.DateTimeHelper;

import java.util.List;

public class RoleMapper {
    public static RoleDTOResponse toRoleDTOResponse(Role role, String creator) {
        return RoleDTOResponse.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .active(role.getActive())
                .createdAt(role.getCreatedAt())
                .description(role.getDescription())
                .creator(creator)
                .build();
    }

    public static RoleDTODetailResponse toRoleDTODetailResponse(Role role, List<SystemPermission> permissions, String creator) {
        List<PermissionDTOResponse> permissionDTOResponseList = null;
        if (permissions != null) {
            permissionDTOResponseList = permissions.stream()
                    .map(SystemPermissionMapper::toPermissionDTOResponse)
                    .toList();
        }
        return RoleDTODetailResponse.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .active(role.getActive())
                .createdAt(role.getCreatedAt())
                .description(role.getDescription())
                .creator(creator)
                .permissions(permissionDTOResponseList)
                .build();
    }

    public static Role toRole(RoleDTORequest request, User userLoggedIn) {
        return Role.builder()
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .name(request.getRoleName())
                .description(request.getDescription())
                .userId(userLoggedIn.getId())
                .build();
    }

    public static Role toRole(RoleDTOUpdate request, User userLoggedIn) {
        return Role.builder()
                .id(request.getRoleId())
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .name(request.getRoleName())
                .description(request.getDescription())
                .userId(userLoggedIn.getId())
                .build();
    }

    public static RoleDTODisplay toRoleDTODisplay(Role role) {
        return RoleDTODisplay.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .build();
    }
}
