package com.example.ADMS.service;


import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.SubjectDTOFilter;
import com.example.ADMS.model.dto.request.SubjectDTORequest;
import com.example.ADMS.model.dto.response.SubjectDTOResponse;

import java.util.List;

public interface SubjectService {
    SubjectDTOResponse createSubject(SubjectDTORequest request);


    SubjectDTOResponse updateSubject(Long id, SubjectDTORequest request);

    void changeStatus(Long id);

    PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter);

    SubjectDTOResponse viewSubjectById(Long id);

    List<SubjectDTOResponse> getListSubjects();

    List<SubjectDTOResponse> getListSubjectsByBookSeries(Long bookSeriesId);
}
