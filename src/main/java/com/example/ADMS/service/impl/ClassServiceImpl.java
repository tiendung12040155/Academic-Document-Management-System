package com.example.ADMS.service.impl;

import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ClassDTOFilter;
import com.example.ADMS.model.dto.request.ClassDTORequest;
import com.example.ADMS.model.dto.response.ClassDTOResponse;
import com.example.ADMS.model.mapper.ClassMapper;
import com.example.ADMS.repository.ClassRepository;
import com.example.ADMS.repository.UserRepository;
import com.example.ADMS.repository.criteria.ClassesCriteria;
import com.example.ADMS.service.ClassService;
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
public class ClassServiceImpl implements ClassService {
    ClassRepository classRepository;
    ClassesCriteria classCriteria;
    UserHelper userHelper;
    MessageException messageException;
    UserRepository userRepository;

    @Override
    public ClassDTOResponse createClass(ClassDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Optional<Class> classOptional = classRepository.findByName(request.getName(), 0L);
        if (classOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate class name");
        }

        // add entity
        Class classEntity = Class.builder()
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .name(request.getName())
                .userId(userLogged.getId())
                .build();
        User user = userRepository.findById(classEntity.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        classEntity = classRepository.save(classEntity);
        return ClassMapper.toClassDTOResponse(classEntity, user.getUsername());
    }

    @Override
    public ClassDTOResponse updateClass(Long id, ClassDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Optional<Class> classOptional = classRepository.findByName(request.getName(), id);
        if (classOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate class name");
        }

        // find Class id
        Class classObject = classRepository.findById(id).get();
        User user = userRepository
                .findById(classObject.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        // update class
        classObject.setName(request.getName());
        classObject.setUserId(userLogged.getId());


        classObject = classRepository.save(classObject);
        return ClassMapper.toClassDTOResponse(classObject, user.getUsername());
    }

    @Override
    public Boolean changeStatus(Long id) {
        // find Class id
        Class classObject = classRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));
        //change active to false
        classObject.setActive(!classObject.getActive());
        classRepository.save(classObject);
        return true;
    }


    @Override
    public ClassDTOResponse viewClassById(Long id) {
        Class classObject = classRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));
        User user = userRepository.findById(classObject.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return ClassMapper.toClassDTOResponse(classObject, user.getUsername());
    }

    @Override
    public PagingDTOResponse searchClass(ClassDTOFilter classDTOFilter) {
        return classCriteria.searchClass(classDTOFilter);
    }

    @Override
    public List<ClassDTOResponse> getListClasses() {
        return classRepository.findClassByActiveIsTrue()
                .stream().map(ClassMapper::toClassDTOResponse)
                .toList();
    }

    @Override
    public List<ClassDTOResponse> getListClassesBySubjectId(Long subjectId) {
        List<Class> classes = new ArrayList<>();
        if (subjectId == null) {
            classes = classRepository.findClassByActiveIsTrue();
        } else classes = classRepository.findAllBySubjectId(subjectId);
        return classes.stream().map(ClassMapper::toClassDTOResponse).toList();
    }
}

