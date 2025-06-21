package com.example.ADMS.model.mapper;


import com.example.ADMS.entity.Subject;
import com.example.ADMS.model.dto.response.SubjectDTOResponse;

public class SubjectMapper {
    public static SubjectDTOResponse toSubjectDTOResponse(Subject subject) {
        return SubjectDTOResponse.builder()
                .name(subject.getName())
                .id(subject.getId())
                .active(subject.getActive())
                .createdAt(subject.getCreatedAt())
                .build();
    }

    public static SubjectDTOResponse toSubjectDTOResponse(Subject subject, String userName) {
        return SubjectDTOResponse.builder()
                .name(subject.getName())
                .id(subject.getId())
                .active(subject.getActive())
                .createdAt(subject.getCreatedAt())
                .creator(userName)
                .build();
    }
}
