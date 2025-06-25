package com.example.ADMS.service;


import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.BookSeriesDTOFilter;
import com.example.ADMS.model.dto.request.BookSeriesDTORequest;
import com.example.ADMS.model.dto.response.BookSeriesDTOResponse;

import java.util.List;

public interface BookSeriesService {
    BookSeriesDTOResponse createBookSeries(BookSeriesDTORequest request, Long classId);

    BookSeriesDTOResponse updateBookSeries(Long id, BookSeriesDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchBookSeries(BookSeriesDTOFilter bookSeriesDTOFilter, Long classId);

    BookSeriesDTOResponse viewBookSeriesById(Long id);

    List<BookSeriesDTOResponse> getListBookSeriesByClassesSubjectId(Long subjectId, Long classId);

    List<BookSeriesDTOResponse> getListBookSeriesByClassId(Long classId);
}
