package com.example.ADMS.service;


import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.BookVolumeDTOFilter;
import com.example.ADMS.model.dto.request.BookVolumeDTORequest;
import com.example.ADMS.model.dto.response.BookVolumeDTOResponse;

import java.util.List;

public interface BookVolumeService {
    BookVolumeDTOResponse createBookVolume(BookVolumeDTORequest request, Long bookSeriesSubjectId);

    BookVolumeDTOResponse updateBookVolume(Long id, BookVolumeDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchBookVolume(BookVolumeDTOFilter bookVolumeDTOFilter, Long bookSeriesSubjectId);


    BookVolumeDTOResponse viewBookVolumeById(Long id);

    List<BookVolumeDTOResponse> getListBookVolumeBySubjectId(Long subjectId, Long bookSeriesId);

    List<BookVolumeDTOResponse> getListBookVolumeByBookSeriesSubjectId(Long bookSeriesSubjectId);
}
