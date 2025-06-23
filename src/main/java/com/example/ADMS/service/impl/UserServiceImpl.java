package com.example.ADMS.service.impl;

import com.example.ADMS.entity.Role;
import com.example.ADMS.entity.User;
import com.example.ADMS.entity.UserRole;
import com.example.ADMS.entity.type.ActionType;
import com.example.ADMS.entity.type.ResourceType;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ProfileDTOUpdate;
import com.example.ADMS.model.dto.request.UserChangePasswordDTORequest;
import com.example.ADMS.model.dto.request.UserDTOFilter;
import com.example.ADMS.model.dto.request.UserDTOUpdate;
import com.example.ADMS.model.dto.response.PreviewInfoDTOResponse;
import com.example.ADMS.model.dto.response.ProfileDTOResponse;
import com.example.ADMS.model.dto.response.UserViewDTOResponse;
import com.example.ADMS.model.mapper.UserMapper;
import com.example.ADMS.model.mapper.UserRoleMapper;
import com.example.ADMS.repository.*;
import com.example.ADMS.repository.criteria.UserCriteria;
import com.example.ADMS.service.FileService;
import com.example.ADMS.service.UserService;
import com.example.ADMS.utils.DataHelper;
import com.example.ADMS.utils.MessageException;
import com.example.ADMS.utils.S3Util;
import com.example.ADMS.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.example.ADMS.utils.Constants.DEFAULT_PASSWORD;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserHelper userHelper;
    ClassRepository classRepository;
    UserRepository userRepository;
    FileService fileService;
    PasswordEncoder passwordEncoder;
    MessageException messageException;
    UserResourceRepository userResourceRepository;
    S3Util s3Util;
    UserCriteria userCriteria;
    UserRoleRepository userRoleRepository;
    RoleRepository roleRepository;

    @Override
    public ProfileDTOResponse viewProfile() {
        User userLoggedIn = userHelper.getUserLogin();
        return UserMapper.toProfileDTOResponse(userLoggedIn, s3Util);
    }


    @Override
    public ProfileDTOResponse updateProfile(ProfileDTOUpdate profileDTOUpdate) {
        User userLoggedIn = userHelper.getUserLogin();

        Optional<User> userOptional = userRepository.findByPhone(profileDTOUpdate.getPhone(), userLoggedIn.getId());
        if (userOptional.isPresent()) {
            throw ApiException.badRequestException("Phone is existed");
        }

        userLoggedIn.setPhone(profileDTOUpdate.getPhone());
        userLoggedIn.setVillage(profileDTOUpdate.getVillage());
        userLoggedIn.setGender(profileDTOUpdate.getGender());
        userLoggedIn.setSchool(profileDTOUpdate.getSchool());
        userLoggedIn.setProvince(profileDTOUpdate.getProvince());
        userLoggedIn.setDistrict(profileDTOUpdate.getDistrict());
        userLoggedIn.setClassObject(classRepository
                .findById(profileDTOUpdate.getClassId()).orElseThrow());
        userLoggedIn.setLastname(profileDTOUpdate.getLastname());
        userLoggedIn.setFirstname(profileDTOUpdate.getFirstname());
        userLoggedIn.setDateOfBirth(profileDTOUpdate.getDateOfBirth());
        return UserMapper.toProfileDTOResponse(userRepository.save(userLoggedIn), s3Util);
    }

    @Override
    public ProfileDTOResponse changeAvatar(MultipartFile avatar) {
        User userLoggedIn = userHelper.getUserLogin();
        String filenameExtension = DataHelper
                .extractFileExtension(avatar.getOriginalFilename()).toUpperCase();
        boolean isImage = ResourceType.getListImages().stream()
                .anyMatch(rt -> rt.toString().equalsIgnoreCase(filenameExtension));
        if (!isImage) {
            throw ApiException.conflictResourceException("Update file name extension such as (png, jpg, jpeg)");
        }
        String avatarUrl = fileService.setAvatar(avatar, userLoggedIn);
        userLoggedIn.setAvatar(avatarUrl);
        User user = userRepository.save(userLoggedIn);
        return UserMapper.toProfileDTOResponse(user, s3Util);
    }

    @Override
    public Boolean changePassword(UserChangePasswordDTORequest request) {
        User userLoggedIn = userHelper.getUserLogin();
        boolean isPassword = passwordEncoder.matches(
                request.getCurrentPassword(),
                userLoggedIn.getPassword()
        );

        if (!isPassword) {
            throw ApiException.badRequestException(messageException.MSG_USER_WRONG_PASSWORD);
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw ApiException.badRequestException(messageException.MSG_USER_NOT_MATCH_PASSWORD);
        }
        userLoggedIn.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userLoggedIn = userRepository.save(userLoggedIn);
        return true;
    }

    @Override
    public PreviewInfoDTOResponse previewInfo() {
        User user = userHelper.getUserLogin();
        PreviewInfoDTOResponse response = UserMapper.toPreviewInfoDTOResponse(user, s3Util);
        response.setSavedCount(userResourceRepository.countUserResourceByType(user.getId(), ActionType.SAVED));
        response.setLikeCount(userResourceRepository.countUserLikeMyResource(user.getId()));
        response.setUploadCount(userResourceRepository.countResourceUploadedByUser(user.getId()));
        return response;
    }

    @Override
    public PagingDTOResponse searchUser(UserDTOFilter userDTOFilter) {
        return userCriteria.searchUser(userDTOFilter);
    }

    @Override
    public boolean resetPasswordUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userRepository.save(user);
        return true;
    }

    @Override
    public UserViewDTOResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        UserViewDTOResponse userDTOUpdate = UserMapper.toUserDTOUpdate(user, s3Util);
        userDTOUpdate.setTotalPosted((long) user.getResourceOwnerList().size());
        userDTOUpdate.setRoles(userRepository.findRoleIdBy(id));
        return userDTOUpdate;
    }

    @Override
    public Boolean updateUser(UserDTOUpdate request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        List<Long> listRoleRequest = request.getRoles();
        List<Long> listRolePresent = user.getUserRoleList().stream()
                .map(UserRole::getRole)
                .map(Role::getId)
                .toList();

        List<Long> listAdded = listRoleRequest.stream()
                .filter(s -> !listRolePresent.contains(s))
                .toList();

        List<Long> listDeleted = listRolePresent.stream()
                .filter(s -> !listRoleRequest.contains(s))
                .toList();

        if (!listAdded.isEmpty()) {
            List<UserRole> userRoleAdds = listAdded.stream()
                    .map(u -> {
                        Role role = roleRepository.findById(u).orElseThrow();
                        return UserRoleMapper.toUserRole(user, role);
                    })
                    .toList();
            userRoleRepository.saveAll(userRoleAdds);
        }

        if (!listDeleted.isEmpty()) {
            List<UserRole> userRoleDeletes = listDeleted.stream()
                    .map(u -> {
                        Role role = roleRepository.findById(u).orElseThrow();
                        return userRoleRepository.findByUserAndRole(user, role).orElseThrow();
                    })
                    .toList();
            removeUserRoles(user, userRoleDeletes);
        }
        return true;
    }

    @Transactional
    public void removeUserRoles(User user, List<UserRole> userRolesToRemove) {
        userRolesToRemove.forEach(userRole -> {
            userRole.setUser(null); // Important for maintaining the bidirectional relationship
            user.getUserRoleList().remove(userRole);
        });

        userRepository.save(user);
    }

    @Override
    public boolean changeActive(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        user.setActive(!user.getActive());
        userRepository.save(user);
        return true;
    }
}
