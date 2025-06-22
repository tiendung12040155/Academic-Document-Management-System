package com.example.ADMS.service.impl;

import com.example.ADMS.entity.BookVolume;
import com.example.ADMS.entity.Chapter;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ChapterDTOFilter;
import com.example.ADMS.model.dto.request.ChapterDTORequest;
import com.example.ADMS.model.dto.response.ChapterDTOResponse;
import com.example.ADMS.model.mapper.ChapterMapper;
import com.example.ADMS.repository.BookVolumeRepository;
import com.example.ADMS.repository.ChapterRepository;
import com.example.ADMS.repository.UserRepository;
import com.example.ADMS.repository.criteria.ChapterCriteria;
import com.example.ADMS.service.ChapterService;
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
public class ChapterServiceImpl implements ChapterService {
    ChapterRepository chapterRepository;
    BookVolumeRepository bookVolumeRepository;
    ChapterCriteria chapterCriteria;
    UserHelper userHelper;
    MessageException messageException;
    UserRepository userRepository;

    @Override
    public ChapterDTOResponse createChapter(ChapterDTORequest request, Long bookVolumeId) {
        User userLogged = userHelper.getUserLogin();

        Optional<Chapter> chapterOptional = chapterRepository.findByName(request.getName(), 0L, bookVolumeId);
        if (chapterOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate chapter name in book volume");
        }

        BookVolume bookVolume = bookVolumeRepository
                .findById(bookVolumeId)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        //add chapter
        Chapter chapter = Chapter.builder()
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .name(request.getName())
                .userId(userLogged.getId())
                .bookVolume(bookVolume)
                .build();
        User user = userRepository.findById(bookVolume.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        chapter = chapterRepository.save(chapter);
        return ChapterMapper.toChapterDTOResponse(chapter, user.getUsername());
    }

    @Override
    public ChapterDTOResponse updateChapter(Long id, ChapterDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Chapter chapter = chapterRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));

        Optional<Chapter> chapterOptional = chapterRepository.findByName(request.getName(), id, chapter.getBookVolume().getId());
        if (chapterOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate chapter name in book volume");
        }
        // find chapter id want to update
        User user = userRepository.findById(chapter.getBookVolume().getId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        //update
        chapter.setName(request.getName());
        chapter.setUserId(user.getId());

        chapter = chapterRepository.save(chapter);
        return ChapterMapper.toChapterDTOResponse(chapter, user.getUsername());
    }

    @Override
    public void changeStatus(Long id) {
        //find id want to delete
        Chapter chapter = chapterRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        //delete
        chapter.setActive(!chapter.getActive());
        chapterRepository.save(chapter);
    }

    @Override
    public PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter, Long bookVolumeId) {
        BookVolume bookVolume = bookVolumeRepository.findById(bookVolumeId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        return chapterCriteria.searchChapter(chapterDTOFilter, bookVolume.getId());
    }


    @Override
    public ChapterDTOResponse viewChapterById(Long id) {
        Chapter chapter = chapterRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Chapter is not found"));
        User user = userRepository.findById(chapter.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return ChapterMapper.toChapterDTOResponse(chapter, user.getUsername());
    }

    @Override
    public List<ChapterDTOResponse> getListChapterByBookVolumeId(Long bookVolumeId) {
        List<Chapter> chapters = new ArrayList<>();
        if (bookVolumeId == null) {
            chapters = chapterRepository.findChapterByActiveTrue();
        } else {
            chapters = chapterRepository.findAllByBookVolumeId(bookVolumeId);
        }
        return chapters.stream().map(ChapterMapper::toChapterDTOResponse).toList();
    }

    // Check exist lesson in chapter
    boolean isCanChangeStatus(Chapter chapter) {
        return chapter.getLessonList().isEmpty();
    }
}
