package com.example.ADMS.model.mapper;


import com.example.ADMS.entity.Lesson;
import com.example.ADMS.model.dto.response.LessonDTOResponse;

public class LessonMapper {
    public static LessonDTOResponse toLessonDTOResponse(Lesson lesson) {
        return LessonDTOResponse.builder()
                .name(lesson.getName())
                .id(lesson.getId())
                .active(lesson.getActive())
                .createdAt(lesson.getCreatedAt())
                .chapterDTOResponse(ChapterMapper.toChapterDTOResponse(lesson.getChapter()))
                .build();
    }

    public static LessonDTOResponse toLessonDTOResponse(Lesson lesson, String userName) {
        return LessonDTOResponse.builder()
                .name(lesson.getName())
                .id(lesson.getId())
                .active(lesson.getActive())
                .createdAt(lesson.getCreatedAt())
                .chapterDTOResponse(ChapterMapper.toChapterDTOResponse(lesson.getChapter()))
                .creator(userName)
                .build();
    }
}
