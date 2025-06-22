package com.example.ADMS.service;


import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.LessonDTOFilter;
import com.example.ADMS.model.dto.request.LessonDTORequest;
import com.example.ADMS.model.dto.response.LessonDTOResponse;

import java.util.List;

public interface LessonService {
    LessonDTOResponse createLesson(LessonDTORequest request, Long chapterId);

    LessonDTOResponse updateLesson(Long id, LessonDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchLesson(LessonDTOFilter lessonDTOFilter, Long chapterId);

    LessonDTOResponse viewLessonById(Long id);

    List<LessonDTOResponse> getListLessonsByChapterId(Long chapterId);
}
