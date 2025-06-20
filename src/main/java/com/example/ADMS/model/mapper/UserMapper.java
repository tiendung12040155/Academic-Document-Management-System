package com.example.ADMS.model.mapper;


import com.example.ADMS.entity.User;
import com.example.ADMS.model.dto.request.RegisterDTORequest;
import com.example.ADMS.model.dto.request.RegisterDTOUpdate;
import com.example.ADMS.model.dto.request.RoleDTODisplay;
import com.example.ADMS.model.dto.response.*;
import com.example.ADMS.utils.Constants;
import com.example.ADMS.utils.DataHelper;
import com.example.ADMS.utils.DateTimeHelper;
import com.example.ADMS.utils.S3Util;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User toUser(RegisterDTORequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .active(true)
                .avatar("default-avatar.jpg")
                .createdAt(DateTimeHelper.getTimeNow())
                .violationTime(0L)
                .totalPoint(Constants.TOTAL_POINT_DEFAULT)
                .build();
    }

    public static User toUser(RegisterDTOUpdate request, User user) {
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setSchool(request.getSchool());
        user.setProvince(request.getProvince());
        user.setDistrict(request.getDistrict());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setVillage(request.getVillage());
        return user;
    }

    public static RegisterDTOResponse toRegisterDTOResponse(User user) {
        return RegisterDTOResponse.builder()
                .id(user.getId())
                .build();
    }

    public static UserDTOResponse toUserDTOResponse(User user, S3Util s3Util) {
        return UserDTOResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .avatar(DataHelper.getLinkAvatar(user, s3Util))
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .phone(user.getPhone())
                .district(user.getDistrict())
                .school(user.getSchool())
                .province(user.getProvince())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static ProfileDTOResponse toProfileDTOResponse(User user, S3Util s3Util) {
        List<RoleDTODisplay> roleDTOResponses = new ArrayList<>();
        if (user.getUserRoleList() != null) {
            roleDTOResponses = user.getUserRoleList().stream()
                    .filter(ur -> ur.getRole().getActive())
                    .map(role -> RoleMapper.toRoleDTODisplay(role.getRole()))
                    .toList();
        } else roleDTOResponses = null;
        return ProfileDTOResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .avatar(DataHelper.getLinkAvatar(user, s3Util))
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .phone(user.getPhone())
                .district(user.getDistrict())
                .school(user.getSchool())
                .province(user.getProvince())
                .village(user.getVillage())
                .createdAt(user.getCreatedAt())
                .classId(user.getClassObject().getId())
                .build();
    }

    public static PreviewInfoDTOResponse toPreviewInfoDTOResponse(User user, S3Util s3Util) {
        return PreviewInfoDTOResponse.builder()
                .userId(user.getId())
                .fullName(user.getFirstname() + " " + user.getLastname())
                .schoolName(user.getSchool())
                .avatar(DataHelper.getLinkAvatar(user, s3Util))
                .username(user.getUsername())
                .build();
    }

    public static UserViewDTOResponse toUserDTOUpdate(User user, S3Util s3Util) {
        return UserViewDTOResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .avatar(DataHelper.getLinkAvatar(user, s3Util))
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .phone(user.getPhone())
                .district(user.getDistrict())
                .school(user.getSchool())
                .province(user.getProvince())
                .createdAt(user.getCreatedAt())
                .className(user.getClassObject().getName())
                .build();
    }
}
