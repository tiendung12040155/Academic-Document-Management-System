package com.example.ADMS.model.mapper;


import com.example.ADMS.entity.Chapter;
import com.example.ADMS.model.dto.response.ChapterDTOResponse;

public class ChapterMapper {
    public static ChapterDTOResponse toChapterDTOResponse(Chapter chapter) {
        return ChapterDTOResponse.builder()
                .name(chapter.getName())
                .id(chapter.getId())
                .active(chapter.getActive())
                .createdAt(chapter.getCreatedAt())
                .bookVolumeDTOResponse(BookVolumeMapper.toBookVolumeDTOResponse(chapter.getBookVolume()))
                .build();
    }

    public static ChapterDTOResponse toChapterDTOResponse(Chapter chapter, String userName) {
        return ChapterDTOResponse.builder()
                .name(chapter.getName())
                .id(chapter.getId())
                .active(chapter.getActive())
                .createdAt(chapter.getCreatedAt())
                .bookVolumeDTOResponse(BookVolumeMapper.toBookVolumeDTOResponse(chapter.getBookVolume()))
                .creator(userName)
                .build();
    }
}
