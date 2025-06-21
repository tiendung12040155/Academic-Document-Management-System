package com.example.ADMS.service.impl;

import com.example.ADMS.entity.BookSeriesSubject;
import com.example.ADMS.entity.BookVolume;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.BookVolumeDTOFilter;
import com.example.ADMS.model.dto.request.BookVolumeDTORequest;
import com.example.ADMS.model.dto.response.BookVolumeDTOResponse;
import com.example.ADMS.model.mapper.BookVolumeMapper;
import com.example.ADMS.repository.BookSeriesSubjectRepository;
import com.example.ADMS.repository.BookVolumeRepository;
import com.example.ADMS.repository.UserRepository;
import com.example.ADMS.repository.criteria.BookVolumeCriteria;
import com.example.ADMS.service.BookVolumeService;
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
public class BookVolumeServiceImpl implements BookVolumeService {
    BookVolumeRepository bookVolumeRepository;
    MessageException messageException;
    UserRepository userRepository;
    BookSeriesSubjectRepository bookSeriesSubjectRepository;
    BookVolumeCriteria bookVolumeCriteria;
    UserHelper userHelper;

    @Override
    public BookVolumeDTOResponse createBookVolume(BookVolumeDTORequest request, Long bookSeriesSubjectId) {
        User userLogged = userHelper.getUserLogin();

        Optional<BookVolume> optionalLesson = bookVolumeRepository.findByName(request.getName(), 0L, bookSeriesSubjectId);
        if (optionalLesson.isPresent()) {
            throw ApiException.badRequestException("Duplicate book volume name in subject");
        }

        BookSeriesSubject bookSeriesSubject = bookSeriesSubjectRepository.findById(bookSeriesSubjectId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_SUBJECT_NOT_FOUND));
        // add entity
        BookVolume bookVolume = BookVolume.builder()
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .name(request.getName())
                .userId(userLogged.getId())
                .bookSeriesSubject(bookSeriesSubject)
                .build();
        User user = userRepository.findById(bookVolume.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        bookVolume = bookVolumeRepository.save(bookVolume);
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume, user.getUsername());
    }

    @Override
    public BookVolumeDTOResponse updateBookVolume(Long id, BookVolumeDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));

        Optional<BookVolume> optionalLesson = bookVolumeRepository.findByName(
                request.getName(),
                id,
                bookVolume.getBookSeriesSubject().getId()
        );

        if (optionalLesson.isPresent()) {
            throw ApiException.badRequestException("Duplicate book volume name in subject");
        }

        User user = userRepository
                .findById(bookVolume.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        //update BookVolume
        bookVolume.setName(request.getName());
        bookVolume.setUserId(userLogged.getId());

        bookVolume = bookVolumeRepository.save(bookVolume);
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume, user.getUsername());
    }

    public void changeStatus(Long id) {
        //find BookVolume want delete
        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        bookVolume.setActive(!bookVolume.getActive());
        bookVolumeRepository.save(bookVolume);
    }

    @Override
    public PagingDTOResponse searchBookVolume(BookVolumeDTOFilter bookVolumeDTOFilter, Long bookSeriesSubjectId) {
        BookSeriesSubject bookSeriesSubject = bookSeriesSubjectRepository.findById(bookSeriesSubjectId)
                .orElseThrow(() -> ApiException.notFoundException("Subject in book series is not found"));
        return bookVolumeCriteria.searchBookVolume(bookVolumeDTOFilter, bookSeriesSubjectId);
    }


    @Override
    public BookVolumeDTOResponse viewBookVolumeById(Long id) {
        BookVolume bookVolume = bookVolumeRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookVolume is not found"));
        User user = userRepository.findById(bookVolume.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return BookVolumeMapper.toBookVolumeDTOResponse(bookVolume, user.getUsername());
    }

    @Override
    public List<BookVolumeDTOResponse> getListBookVolumeBySubjectId(Long subjectId, Long bookSeriesId) {
        List<BookVolume> bookVolumes = new ArrayList<>();
        if (subjectId == null || bookSeriesId == null) {
            bookVolumes = bookVolumeRepository.findBookVolumeByActiveTrue();
        } else {
            bookVolumes = bookVolumeRepository.findAllBySubjectId(subjectId, bookSeriesId);
        }
        return bookVolumes.stream().map(BookVolumeMapper::toBookVolumeDTOResponse).toList();
    }

    @Override
    public List<BookVolumeDTOResponse> getListBookVolumeByBookSeriesSubjectId(Long bookSeriesSubjectId) {
        List<BookVolume> bookVolumes = new ArrayList<>();
        if (bookSeriesSubjectId == null) {
            bookVolumes = bookVolumeRepository.findBookVolumeByActiveTrue();
        } else {
            bookVolumes = bookVolumeRepository.findBookVolumeByBookSeriesSubjectId(bookSeriesSubjectId);
        }
        return bookVolumes.stream()
                .map(BookVolumeMapper::toBookVolumeDTOResponse)
                .toList();
    }

    boolean isCanChangeStatus(BookVolume bookVolume) {
        return bookVolume.getChapterList().isEmpty();
    }
}

