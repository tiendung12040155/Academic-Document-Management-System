package com.example.ADMS.model.mapper;

import com.example.ADMS.entity.SystemPermission;
import com.example.ADMS.model.dto.response.PermissionDTOResponse;

public class SystemPermissionMapper {
    public static PermissionDTOResponse toPermissionDTOResponse(SystemPermission permission) {
        return PermissionDTOResponse.builder()
                .permissionId(permission.getId())
                .permissionName(permission.getName())
                .build();
    }
}
