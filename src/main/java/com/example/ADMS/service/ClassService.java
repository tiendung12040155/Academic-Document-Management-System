package com.example.ADMS.service;


import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ClassDTOFilter;
import com.example.ADMS.model.dto.request.ClassDTORequest;
import com.example.ADMS.model.dto.response.ClassDTOResponse;

import java.util.List;

public interface ClassService {
    ClassDTOResponse createClass(ClassDTORequest request);

    ClassDTOResponse updateClass(Long id, ClassDTORequest request);

    Boolean changeStatus(Long id);

    ClassDTOResponse viewClassById(Long id);

    PagingDTOResponse searchClass(ClassDTOFilter classDTOFilter);

    List<ClassDTOResponse> getListClasses();

    List<ClassDTOResponse> getListClassesBySubjectId(Long subjectId);
}
