package com.example.ADMS.service.impl;

import com.example.ADMS.entity.BookSeries;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.BookSeriesDTOFilter;
import com.example.ADMS.model.dto.request.BookSeriesDTORequest;
import com.example.ADMS.model.dto.response.BookSeriesDTOResponse;
import com.example.ADMS.model.mapper.BookSeriesMapper;
import com.example.ADMS.repository.BookSeriesRepository;
import com.example.ADMS.repository.ClassRepository;
import com.example.ADMS.repository.UserRepository;
import com.example.ADMS.repository.criteria.BookSeriesCriteria;
import com.example.ADMS.service.BookSeriesService;
import com.example.ADMS.utils.DateTimeHelper;
import com.example.ADMS.utils.MessageException;
import com.example.ADMS.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import com.example.ADMS.entity.Class;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookSeriesServiceImpl implements BookSeriesService {
    BookSeriesRepository bookSeriesRepository;
    ClassRepository classRepository;
    MessageException messageException;
    BookSeriesCriteria bookSeriesCriteria;
    UserHelper userHelper;
    UserRepository userRepository;

    @Override
    public BookSeriesDTOResponse createBookSeries(BookSeriesDTORequest request, Long classId) {
        User userLogged = userHelper.getUserLogin();

        Optional<BookSeries> bookSeriesOptional = bookSeriesRepository.findByName(request.getName(), 0L, classId);
        if (bookSeriesOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate book series name in class");
        }

        Class classObject = classRepository
                .findById(classId)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));
        User user = userRepository.findById(classObject.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );

        BookSeries bookSeries = BookSeries.builder()
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .name(request.getName())
                .userId(userLogged.getId())
                .classObject(classObject)
                .build();

        bookSeries = bookSeriesRepository.save(bookSeries);
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries, user.getUsername());
    }

    @Override
    public BookSeriesDTOResponse updateBookSeries(Long id, BookSeriesDTORequest request) {
        User userLoggedIn = userHelper.getUserLogin();

        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));

        Optional<BookSeries> bookSeriesOptional = bookSeriesRepository
                .findByName(request.getName(), bookSeries.getId(), bookSeries.getClassObject().getId());

        if (bookSeriesOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate book series name in class");
        }

        User user = userRepository.findById(bookSeries.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        // update book series
        bookSeries.setName(request.getName());
        bookSeries.setUserId(userLoggedIn.getId());


        bookSeries = bookSeriesRepository.save(bookSeries);
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries, user.getUsername());
    }

    @Override
    public void changeStatus(Long id) {
        // find BookSeries id
        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));
        bookSeries.setActive(!bookSeries.getActive());
        bookSeriesRepository.save(bookSeries);
    }

    @Override
    public PagingDTOResponse searchBookSeries(BookSeriesDTOFilter bookSeriesDTOFilter, Long classId) {
        Class classObject = classRepository.findById(classId).orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        return bookSeriesCriteria.searchBookSeries(bookSeriesDTOFilter, classObject.getId());
    }


    @Override
    public BookSeriesDTOResponse viewBookSeriesById(Long id) {
        BookSeries bookSeries = bookSeriesRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("BookSeries is not found"));
        User user = userRepository.findById(bookSeries.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return BookSeriesMapper.toBookseriesDTOResponse(bookSeries, user.getUsername());
    }

    // Check exist subject in book series
    boolean isCanChangeStatus(BookSeries bookSeries) {
        return bookSeries.getBookSeriesSubjects().isEmpty();
    }

    @Override
    public List<BookSeriesDTOResponse> getListBookSeriesByClassesSubjectId(Long subjectId, Long classId) {
        List<BookSeries> bookSeries = new ArrayList<>();
        if (subjectId == null || classId == null) {
            bookSeries = bookSeriesRepository.findBookSeriesByActiveTrue();
        } else bookSeries = bookSeriesRepository.findAllBySubjectIdClassId(subjectId, classId);
        return bookSeries.stream().map(BookSeriesMapper::toBookseriesDTOResponse).toList();
    }

    @Override
    public List<BookSeriesDTOResponse> getListBookSeriesByClassId(Long classId) {
        List<BookSeries> bookSeries = new ArrayList<>();
        if (classId == null) {
            bookSeries = bookSeriesRepository.findBookSeriesByActiveTrue();
        } else bookSeries = bookSeriesRepository.findAllByClassId(classId);
        return bookSeries.stream().map(BookSeriesMapper::toBookseriesDTOResponse).toList();
    }
}

