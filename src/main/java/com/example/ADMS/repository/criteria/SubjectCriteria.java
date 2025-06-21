package com.example.ADMS.repository.criteria;


import com.example.ADMS.entity.Subject;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.SubjectDTOFilter;
import com.example.ADMS.model.dto.response.SubjectDTOResponse;
import com.example.ADMS.model.mapper.SubjectMapper;
import com.example.ADMS.repository.UserRepository;
import com.example.ADMS.utils.MessageException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectCriteria {
    EntityManager em;
    MessageException messageException;
    UserRepository userRepository;

    public PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from Subject t where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();

        if (subjectDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + subjectDTOFilter.getName() + "%");
        }

        if (subjectDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active", subjectDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = subjectDTOFilter.getPageIndex();
        Long pageSize = subjectDTOFilter.getPageSize();

        TypedQuery<Subject> subjectTypedQuery = em.createQuery(sql.toString(), Subject.class);

        // Set param to query
        params.forEach((k, v) -> {
            subjectTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        subjectTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        subjectTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Subject> subjectList = subjectTypedQuery.getResultList();

        Long totalSubject = (Long) countQuery.getSingleResult();
        Long totalPage = totalSubject / pageSize;
        if (totalSubject % pageSize != 0) {
            totalPage++;
        }
        List<SubjectDTOResponse> subjectDTOResponseList = new ArrayList<>();
        for (Subject entity : subjectList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            SubjectDTOResponse subjectDTOResponse =
                    SubjectMapper.toSubjectDTOResponse(entity, user.getUsername());
            subjectDTOResponseList.add(subjectDTOResponse);
        }

        return PagingDTOResponse.builder()
                .totalElement(totalSubject)
                .totalPage(totalPage)
                .data(subjectDTOResponseList)
                .build();
    }
}
