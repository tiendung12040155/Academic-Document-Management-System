package com.example.ADMS.model.mapper;

import com.example.ADMS.entity.Class;
import com.example.ADMS.model.dto.response.ClassDTOResponse;

public class ClassMapper {
    public static ClassDTOResponse toClassDTOResponse(Class classEntity) {
        return ClassDTOResponse.builder()
                .name(classEntity.getName())
                .id(classEntity.getId())
                .active(classEntity.getActive())
                .createdAt(classEntity.getCreatedAt())
                .build();
    }

    public static ClassDTOResponse toClassDTOResponse(Class classEntity, String userName) {
        return ClassDTOResponse.builder()
                .name(classEntity.getName())
                .id(classEntity.getId())
                .active(classEntity.getActive())
                .createdAt(classEntity.getCreatedAt())
                .creator(userName)
                .build();
    }
}
