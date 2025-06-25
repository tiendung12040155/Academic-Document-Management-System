package com.example.ADMS.repository.criteria;

import com.example.ADMS.entity.Chapter;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.ChapterDTOFilter;
import com.example.ADMS.model.dto.response.ChapterDTOResponse;
import com.example.ADMS.model.mapper.ChapterMapper;
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
public class ChapterCriteria {
    EntityManager em;
    MessageException messageException;
    UserRepository userRepository;

    public PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter, Long bookVolumeId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select t from Chapter t where t.bookVolume.id = :bookVolumeId ");
        params.put("bookVolumeId", bookVolumeId);

        if (chapterDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + chapterDTOFilter.getName() + "%");
        }

        if (chapterDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active", chapterDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = chapterDTOFilter.getPageIndex();
        Long pageSize = chapterDTOFilter.getPageSize();

        TypedQuery<Chapter> chapterTypedQuery = em.createQuery(sql.toString(), Chapter.class);

        // Set param to query
        params.forEach((k, v) -> {
            chapterTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        chapterTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        chapterTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Chapter> chapterList = chapterTypedQuery.getResultList();

        Long totalChapter = (Long) countQuery.getSingleResult();
        Long totalPage = totalChapter / pageSize;
        if (totalChapter % pageSize != 0) {
            totalPage++;
        }

        List<ChapterDTOResponse> chapterDTOResponseList = new ArrayList<>();
        for (Chapter entity : chapterList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            ChapterDTOResponse chapterDTOResponse =
                    ChapterMapper.toChapterDTOResponse(entity, user.getUsername());
            chapterDTOResponseList.add(chapterDTOResponse);
        }

        return PagingDTOResponse.builder()
                .totalElement(totalChapter)
                .totalPage(totalPage)
                .data(chapterDTOResponseList)
                .build();
    }
}
