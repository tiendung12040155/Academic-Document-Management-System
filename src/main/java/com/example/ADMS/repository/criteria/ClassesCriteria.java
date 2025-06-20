package com.example.ADMS.repository.criteria;

import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ClassDTOFilter;
import com.example.ADMS.model.dto.response.ClassDTOResponse;
import com.example.ADMS.model.mapper.ClassMapper;
import com.example.ADMS.repository.UserRepository;
import com.example.ADMS.utils.MessageException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import com.example.ADMS.entity.Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassesCriteria {
    EntityManager em;
    UserRepository userRepository;
    MessageException messageException;

    public PagingDTOResponse searchClass(ClassDTOFilter classDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from Class t where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();

        if (classDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + classDTOFilter.getName() + "%");
        }

        if (classDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active", classDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = classDTOFilter.getPageIndex();
        Long pageSize = classDTOFilter.getPageSize();

        TypedQuery<Class> classTypedQuery = em.createQuery(sql.toString(), Class.class);

        // Set param to query
        params.forEach((k, v) -> {
            classTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        classTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        classTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Class> classList = classTypedQuery.getResultList();

        Long totalClass = (Long) countQuery.getSingleResult();
        Long totalPage = totalClass / pageSize;
        if (totalClass % pageSize != 0) {
            totalPage++;
        }

        List<ClassDTOResponse> classDTOResponseList = new ArrayList<>();
        for (Class entity : classList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            ClassDTOResponse classDTOResponse =
                    ClassMapper.toClassDTOResponse(entity, user.getUsername());
            classDTOResponseList.add(classDTOResponse);
        }
        return PagingDTOResponse.builder()
                .totalElement(totalClass)
                .totalPage(totalPage)
                .data(classDTOResponseList)
                .build();
    }
}
