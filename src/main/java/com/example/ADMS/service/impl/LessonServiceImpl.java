package com.example.ADMS.service.impl;

import com.example.ADMS.entity.Chapter;
import com.example.ADMS.entity.Lesson;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.LessonDTOFilter;
import com.example.ADMS.model.dto.request.LessonDTORequest;
import com.example.ADMS.model.dto.response.LessonDTOResponse;
import com.example.ADMS.model.mapper.LessonMapper;
import com.example.ADMS.repository.ChapterRepository;
import com.example.ADMS.repository.LessonRepository;
import com.example.ADMS.repository.UserRepository;
import com.example.ADMS.repository.criteria.LessonCriteria;
import com.example.ADMS.service.LessonService;
import com.example.ADMS.utils.DateTimeHelper;
import com.example.ADMS.utils.MessageException;
import com.example.ADMS.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonServiceImpl implements LessonService {
    LessonRepository lessonRepository;
    ChapterRepository chapterRepository;
    LessonCriteria lessonCriteria;
    UserHelper userHelper;
    MessageException messageException;
    UserRepository userRepository;

    @Override
    public LessonDTOResponse createLesson(LessonDTORequest request, Long chapterId) {
        User userLogged = userHelper.getUserLogin();

        Optional<Lesson> optionalLesson = lessonRepository.findByName(request.getName(), 0L, chapterId);
        if (optionalLesson.isPresent()) {
            throw ApiException.badRequestException("Duplicate lesson name in chapter");
        }

        //find chapter id
        Chapter chapter = chapterRepository.findById(chapterId).get();
        User user = userRepository.findById(chapter.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        Lesson lesson = Lesson.builder()
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .name(request.getName())
                .userId(userLogged.getId())
                .chapter(chapter)
                .build();
        lesson = lessonRepository.save(lesson);
        return LessonMapper.toLessonDTOResponse(lesson, user.getUsername());
    }

    @Override
    public LessonDTOResponse updateLesson(Long id, LessonDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        //find Lesson id want to update
        Lesson lesson = lessonRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));

        Optional<Lesson> optionalLesson = lessonRepository
                .findByName(request.getName(), id, lesson.getChapter().getId());
        if (optionalLesson.isPresent()) {
            throw ApiException.badRequestException("Duplicate lesson name in chapter");
        }

        User user = userRepository.findById(lesson.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        //update
        lesson.setName(request.getName());
        lesson.setUserId(userLogged.getId());

        lesson = lessonRepository.save(lesson);
        return LessonMapper.toLessonDTOResponse(lesson, user.getUsername());
    }

    @Override
    public void changeStatus(Long id) {
        //find Lesson id want to delete
        Lesson lesson = lessonRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));
        lesson.setActive(!lesson.getActive());
        lessonRepository.save(lesson);
    }

    @Override
    public PagingDTOResponse searchLesson(LessonDTOFilter lessonDTOFilter, Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_CHAPTER_NOT_FOUND));
        return lessonCriteria.searchLesson(lessonDTOFilter, chapter.getId());
    }


    @Override
    public LessonDTOResponse viewLessonById(Long id) {
        //find lesson id
        Lesson lesson = lessonRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Lesson is not found"));
        User user = userRepository.findById(lesson.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return LessonMapper.toLessonDTOResponse(lesson, user.getUsername());
    }

    @Override
    public List<LessonDTOResponse> getListLessonsByChapterId(Long chapterId) {
        List<Lesson> lessons = new ArrayList<>();
        if (chapterId == null) {
            lessons = lessonRepository.findLessonByActiveTrue();
        } else {
            lessons = lessonRepository.findAllByChapterId(chapterId);
        }
        return lessons.stream().map(LessonMapper::toLessonDTOResponse).toList();
    }
}
