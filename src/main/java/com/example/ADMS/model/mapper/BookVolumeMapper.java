package com.example.ADMS.model.mapper;


import com.example.ADMS.entity.BookVolume;
import com.example.ADMS.model.dto.response.BookVolumeDTOResponse;

public class BookVolumeMapper {
    public static BookVolumeDTOResponse toBookVolumeDTOResponse(BookVolume bookVolume) {
        return BookVolumeDTOResponse.builder()
                .name(bookVolume.getName())
                .id(bookVolume.getId())
                .active(bookVolume.getActive())
                .createdAt(bookVolume.getCreatedAt())
                .build();
    }

    public static BookVolumeDTOResponse toBookVolumeDTOResponse(BookVolume bookVolume, String userName) {
        return BookVolumeDTOResponse.builder()
                .name(bookVolume.getName())
                .id(bookVolume.getId())
                .active(bookVolume.getActive())
                .createdAt(bookVolume.getCreatedAt())
                .creator(userName)
                .build();
    }
}
