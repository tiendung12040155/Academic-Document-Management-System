package com.example.ADMS.service.impl;

import com.example.ADMS.entity.BookSeriesSubject;
import com.example.ADMS.entity.Subject;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.SubjectDTOFilter;
import com.example.ADMS.model.dto.request.SubjectDTORequest;
import com.example.ADMS.model.dto.response.SubjectDTOResponse;
import com.example.ADMS.model.mapper.SubjectMapper;
import com.example.ADMS.repository.SubjectRepository;
import com.example.ADMS.repository.UserRepository;
import com.example.ADMS.repository.criteria.SubjectCriteria;
import com.example.ADMS.service.SubjectService;
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
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    MessageException messageException;
    UserRepository userRepository;
    SubjectCriteria subjectCriteria;
    UserHelper userHelper;

    @Override
    public SubjectDTOResponse createSubject(SubjectDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Optional<Subject> subjectOptional = subjectRepository.findByName(request.getName(), 0L);
        if (subjectOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate subject name");
        }

        // add entity
        Subject subject = Subject.builder()
                .active(true)
                .createdAt(DateTimeHelper.getTimeNow())
                .name(request.getName())
                .userId(userLogged.getId())
                .build();
        User user = userRepository.findById(subject.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        subject = subjectRepository.save(subject);
        return SubjectMapper.toSubjectDTOResponse(subject, user.getUsername());
    }

    @Override
    public SubjectDTOResponse updateSubject(Long id, SubjectDTORequest request) {
        User userLogged = userHelper.getUserLogin();

        Optional<Subject> subjectOptional = subjectRepository.findByName(request.getName(), id);
        if (subjectOptional.isPresent()) {
            throw ApiException.badRequestException("Duplicate subject name");
        }

        // find subject id want update
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));
        User user = userRepository.findById(subject.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        // update subject
        subject.setName(request.getName());
        subject.setUserId(userLogged.getId());

        subject = subjectRepository.save(subject);
        return SubjectMapper.toSubjectDTOResponse(subject, user.getUsername());
    }

    @Override
    public void changeStatus(Long id) {
        // find Subject id
        Subject subject = subjectRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));
        subject.setActive(!subject.getActive());
        subjectRepository.save(subject);
    }

    @Override
    public PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter) {
        return subjectCriteria.searchSubject(subjectDTOFilter);
    }


    @Override
    public SubjectDTOResponse viewSubjectById(Long id) {
        Subject subject = subjectRepository
                .findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Subject is not found"));

        User user = userRepository.findById(subject.getUserId())
                .orElseThrow(
                        () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                );
        return SubjectMapper.toSubjectDTOResponse(subject, user.getUsername());
    }

    @Override
    public List<SubjectDTOResponse> getListSubjects() {
        return subjectRepository.findAllByActiveTrue().stream()
                .map(SubjectMapper::toSubjectDTOResponse)
                .toList();
    }

    @Override
    public List<SubjectDTOResponse> getListSubjectsByBookSeries(Long bookSeriesId) {
        List<Subject> subjects = new ArrayList<>();
        List<Long> subjectIds = new ArrayList<>();
        if (bookSeriesId == null) {
            subjects = subjectRepository.findAllByActiveTrue();
        } else {
            subjects = subjectRepository.findSubjectByBookSeries(bookSeriesId);
            subjects = subjects.stream()
                    .peek(s -> {
                        BookSeriesSubject bss = subjectRepository
                                .findBookSeriesSubjectBySubjectIdBookSeriesId(s.getId(), bookSeriesId);
                        subjectIds.add(bss.getSubject().getId());
                        s.setId(bss.getId());
                    })
                    .toList();
        }

        List<SubjectDTOResponse> subjectDTOResponses = subjects.stream()
                .map(SubjectMapper::toSubjectDTOResponse)
                .toList();

        for (int i = 0; i < subjectIds.size(); i++) {
            subjectDTOResponses.get(i).setSubjectId(subjectIds.get(i));
        }

        return subjectDTOResponses;
    }
}
