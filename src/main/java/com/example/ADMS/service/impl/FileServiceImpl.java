package com.example.ADMS.service.impl;

import com.example.ADMS.entity.User;
import com.example.ADMS.entity.type.ResourceType;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.dto.response.FileDTOResponse;
import com.example.ADMS.service.FileService;
import com.example.ADMS.utils.DataHelper;
import com.example.ADMS.utils.MessageException;
import com.example.ADMS.utils.S3Util;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileServiceImpl implements FileService {
    MessageException messageException;
    S3Util s3Util;

    private String getThumbnailDocument(String fileNameExtension) {
        String pdf = List.of(ResourceType.PDF).toString();
        String slide = List.of(ResourceType.PPTX, ResourceType.PPT).toString();
        String doc = List.of(ResourceType.DOC, ResourceType.DOCX).toString();
        String img = List.of(ResourceType.JPEG, ResourceType.JPG, ResourceType.PNG).toString();

        if (pdf.contains(fileNameExtension)) {
            return "default/pdf.png";
        } else if (slide.contains(fileNameExtension)) {
            return "default/pptx.png";
        } else if (doc.contains(fileNameExtension)) {
            return "default/doc.png";
        } else if (ResourceType.MP3.toString().equalsIgnoreCase(fileNameExtension)) {
            return "default/mp3.png";
        } else if (ResourceType.MP4.toString().equalsIgnoreCase(fileNameExtension)) {
            return "default/mp4.png";
        } else if (img.contains(fileNameExtension)) {
            return "default/img.png";
        }
        return null;
    }

    @Override
    public FileDTOResponse uploadSingleFile(
            MultipartFile multipartFile,
            String fileName,
            Long lessonId
    ) {
        String filenameNoExtension = fileName.toLowerCase();
        //get file name extension from file
        String fileNameExtension = DataHelper.extractFileExtension(multipartFile.getOriginalFilename()).toLowerCase();
        fileName = DataHelper.removeDiacritics(fileName).toLowerCase();
        fileName = DataHelper.generateFilename(fileName, fileNameExtension);

        //get file name extension defined
        String fileNameExtensionList = Arrays.toString(ResourceType.values()).toLowerCase();

        if (lessonId == null && ResourceType.getResourceByLesson().toString().contains(fileNameExtension.toUpperCase())) {
            throw ApiException.badRequestException(messageException.MSG_FILE_TYPE_INVALID);
        }

        //only file name extension defined before
        if (!fileNameExtensionList.contains(fileNameExtension)) {
            throw ApiException.badRequestException(messageException.MSG_FILE_TYPE_INVALID);
        }

        File file = s3Util.convertMultiPartToFile(multipartFile);
        s3Util.uploadPhoto(fileName, file);

        //create path name thumbnail of resource
        String thumbnailSrc = fileName;

        thumbnailSrc = getThumbnailDocument(fileNameExtension.toUpperCase());
        return FileDTOResponse.builder()
                .size(multipartFile.getSize())
                .resourceSrc(fileName)
                .resourceType(ResourceType.valueOf(fileNameExtension.toUpperCase()))
                .thumbnailSrc(thumbnailSrc)
                .fileExtension(fileNameExtension)
                .originalFileName(fileName)
                .build();

    }

    @Override
    public List<FileDTOResponse> uploadMultiFile(
            MultipartFile[] files,
            Long lessonId,
            String filenameOption
    ) {
        return Arrays.stream(files).map(file -> {
            String filename = filenameOption == null ?
                    DataHelper.extractFilename(Objects.requireNonNull(file.getOriginalFilename()))
                    : filenameOption;
            return uploadSingleFile(file, filename, lessonId);
        }).toList();
    }

    @Override
    public void deleteFileResource(String filename) {
        s3Util.deleteFile(filename);
    }

    @Override
    public byte[] downloadFile(String fileName) {
        return s3Util.downloadPhoto(fileName);
    }

    @Override
    public String setAvatar(MultipartFile multipartFile, User userLoggedIn) {
        //get file name extension defined
        String fileNameExtensionList = ResourceType.getListImages().toString().toLowerCase();

        //get file name extension from file
        String fileNameExtension = DataHelper.extractFileExtension(multipartFile.getOriginalFilename());

        //only file name extension defined before
        if (!fileNameExtensionList.contains(fileNameExtension)) {
            throw ApiException.badRequestException(messageException.MSG_FILE_TYPE_INVALID);
        }

        //concat new filename and extension
        String fileName = userLoggedIn.getUsername().concat("_avatar")
                .concat(".").concat(fileNameExtension);

        File file = s3Util.convertMultiPartToFile(multipartFile);
        s3Util.uploadPhoto(fileName, file);

        return fileName;
    }
}
