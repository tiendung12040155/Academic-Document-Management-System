package com.example.ADMS.service;

import com.example.ADMS.entity.User;
import com.example.ADMS.model.dto.response.FileDTOResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<FileDTOResponse> uploadMultiFile(MultipartFile[] files, Long lessonId, String filenameOption);

    byte[] downloadFile(String fileName);

    String setAvatar(MultipartFile avatar, User userLoggedIn);

    FileDTOResponse uploadSingleFile(MultipartFile multipartFile, String fileName, Long lessonId);

    void deleteFileResource(String filename);
}
