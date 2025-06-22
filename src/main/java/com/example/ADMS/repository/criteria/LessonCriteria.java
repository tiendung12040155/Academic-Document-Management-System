package com.example.ADMS.repository.criteria;

import com.example.ADMS.entity.Lesson;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.LessonDTOFilter;
import com.example.ADMS.model.dto.response.LessonDTOResponse;
import com.example.ADMS.model.mapper.LessonMapper;
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
public class LessonCriteria {
    EntityManager em;
    MessageException messageException;
    UserRepository userRepository;

    public PagingDTOResponse searchLesson(LessonDTOFilter lessonDTOFilter, Long chapterId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select t from Lesson t where t.chapter.id = :chapterId");
        params.put("chapterId", chapterId);

        if (lessonDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + lessonDTOFilter.getName() + "%");
        }

        if (lessonDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active", lessonDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = lessonDTOFilter.getPageIndex();
        Long pageSize = lessonDTOFilter.getPageSize();

        TypedQuery<Lesson> lessonTypedQuery = em.createQuery(sql.toString(), Lesson.class);

        // Set param to query
        params.forEach((k, v) -> {
            lessonTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        lessonTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        lessonTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Lesson> lessonList = lessonTypedQuery.getResultList();

        Long totalLesson = (Long) countQuery.getSingleResult();
        Long totalPage = totalLesson / pageSize;
        if (totalLesson % pageSize != 0) {
            totalPage++;
        }

        List<LessonDTOResponse> lessonDTOResponseList = new ArrayList<>();
        for (Lesson entity : lessonList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            LessonDTOResponse lessonDTOResponse =
                    LessonMapper.toLessonDTOResponse(entity, user.getUsername());
            lessonDTOResponseList.add(lessonDTOResponse);
        }

        return PagingDTOResponse.builder()
                .totalElement(totalLesson)
                .totalPage(totalPage)
                .data(lessonDTOResponseList)
                .build();
    }
}
