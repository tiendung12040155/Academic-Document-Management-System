package com.example.ADMS.service;

import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ProfileDTOUpdate;
import com.example.ADMS.model.dto.request.UserChangePasswordDTORequest;
import com.example.ADMS.model.dto.request.UserDTOFilter;
import com.example.ADMS.model.dto.request.UserDTOUpdate;
import com.example.ADMS.model.dto.response.PreviewInfoDTOResponse;
import com.example.ADMS.model.dto.response.ProfileDTOResponse;
import com.example.ADMS.model.dto.response.UserViewDTOResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ProfileDTOResponse viewProfile();

    ProfileDTOResponse updateProfile(ProfileDTOUpdate profileDTOUpdate);

    ProfileDTOResponse changeAvatar(MultipartFile avatar);

    Boolean changePassword(UserChangePasswordDTORequest request);

    PreviewInfoDTOResponse previewInfo();

    PagingDTOResponse searchUser(UserDTOFilter userDTOFilter);

    boolean resetPasswordUser(Long id);

    UserViewDTOResponse getUserById(Long id);

    Boolean updateUser(UserDTOUpdate request, Long id);

    boolean changeActive(Long id);
}
