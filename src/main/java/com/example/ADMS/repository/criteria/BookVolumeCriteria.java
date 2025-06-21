package com.example.ADMS.repository.criteria;


import com.example.ADMS.entity.BookVolume;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.BookVolumeDTOFilter;
import com.example.ADMS.model.dto.response.BookVolumeDTOResponse;
import com.example.ADMS.model.mapper.BookVolumeMapper;
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
public class BookVolumeCriteria {
    EntityManager em;
    MessageException messageException;
    UserRepository userRepository;

    public PagingDTOResponse searchBookVolume(BookVolumeDTOFilter bookVolumeDTOFilter, Long bookSeriesSubjectId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select t from BookVolume t where t.bookSeriesSubject.id = :bookSeriesSubjectId");
        params.put("bookSeriesSubjectId", bookSeriesSubjectId);

        if (bookVolumeDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + bookVolumeDTOFilter.getName() + "%");
        }

        if (bookVolumeDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active", bookVolumeDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = bookVolumeDTOFilter.getPageIndex();
        Long pageSize = bookVolumeDTOFilter.getPageSize();

        TypedQuery<BookVolume> bookVolumeTypedQuery = em.createQuery(sql.toString(), BookVolume.class);

        // Set param to query
        params.forEach((k, v) -> {
            bookVolumeTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        bookVolumeTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        bookVolumeTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<BookVolume> bookVolumeList = bookVolumeTypedQuery.getResultList();

        Long totalBookVolume = (Long) countQuery.getSingleResult();
        Long totalPage = totalBookVolume / pageSize;
        if (totalBookVolume % pageSize != 0) {
            totalPage++;
        }

        List<BookVolumeDTOResponse> bookVolumeDTOResponseList = new ArrayList<>();
        for (BookVolume entity : bookVolumeList) {
            User user = userRepository.findById(entity.getUserId())
                    .orElseThrow(
                            () -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND)
                    );
            BookVolumeDTOResponse bookVolumeDTOResponse =
                    BookVolumeMapper.toBookVolumeDTOResponse(entity, user.getUsername());
            bookVolumeDTOResponseList.add(bookVolumeDTOResponse);
        }

        return PagingDTOResponse.builder()
                .totalElement(totalBookVolume)
                .totalPage(totalPage)
                .data(bookVolumeDTOResponseList)
                .build();
    }
}
