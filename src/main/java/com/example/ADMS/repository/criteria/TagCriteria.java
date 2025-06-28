package com.example.ADMS.repository.criteria;

import com.example.ADMS.entity.Tag;
import com.example.ADMS.model.dto.request.TagDTOFilter;
import com.example.ADMS.model.dto.response.PagingTagDTOResponse;
import com.example.ADMS.model.dto.response.TagDTOResponse;
import com.example.ADMS.model.mapper.TagMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagCriteria {
    EntityManager em;

    public PagingTagDTOResponse searchTag(TagDTOFilter tagDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from Tag t where 1=1");
        Map<String, Object> params = new HashMap<>();

        if (tagDTOFilter.getName() != null) {
            sql.append(" and t.name like :name ");
            params.put("name", "%" + tagDTOFilter.getName() + "%");
        }

        if (tagDTOFilter.getActive() != null) {
            sql.append(" and t.active = :active ");
            params.put("active", tagDTOFilter.getActive());
        }

        Query countQuery = em.createQuery(sql.toString()
                .replace("select t", "select count(t.id)"));

        Long pageIndex = tagDTOFilter.getPageIndex();
        Long pageSize = tagDTOFilter.getPageSize();

        sql.append(" order by t.name asc ");

        TypedQuery<Tag> tagTypedQuery = em.createQuery(sql.toString(), Tag.class);

        // Set param to query
        params.forEach((k, v) -> {
            tagTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        tagTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        tagTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Tag> tagList = tagTypedQuery.getResultList();

        Long totalTag = (Long) countQuery.getSingleResult();
        Long totalPage = totalTag / pageSize;
        if (totalTag % pageSize != 0) {
            totalPage++;
        }

        List<TagDTOResponse> tagDTOResponseList = tagList.stream()
                .map(TagMapper::toTagDTOResponse)
                .toList();

        return PagingTagDTOResponse.builder()
                .totalElement(totalTag)
                .totalPage(totalPage)
                .data(tagDTOResponseList)
                .build();
    }
}
