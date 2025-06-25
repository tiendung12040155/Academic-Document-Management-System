package com.example.ADMS.repository.criteria;

import com.example.ADMS.entity.Comment;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.CommentDTOFilter;
import com.example.ADMS.model.dto.response.CommentReportDTOResponse;
import com.example.ADMS.model.mapper.ReportCommentMapper;
import com.example.ADMS.utils.S3Util;
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
public class CommentCriteria {
    EntityManager em;
    S3Util s3Util;

    public PagingDTOResponse searchComment(CommentDTOFilter commentDTOFilter) {
        StringBuilder sql = new StringBuilder("select distinct c from Comment c join c.commenter ct join c.reportCommentList rc where c.active = true and rc.active = true ");
        Map<String, Object> params = new HashMap<>();

        if (commentDTOFilter.getContent() != null) {
            sql.append(" and (c.content like :content1 or ct.username like :content2 or ct.email like :content3) ");
            params.put("content1", "%" + commentDTOFilter.getContent() + "%");
            params.put("content2", "%" + commentDTOFilter.getContent() + "%");
            params.put("content3", "%" + commentDTOFilter.getContent() + "%");
        }

        sql.append(" order by c.createdAt DESC");

        Query countQuery = em.createQuery(sql.toString().replace("select distinct c", "select count(distinct c.id)"));

        Long pageIndex = commentDTOFilter.getPageIndex();
        Long pageSize = commentDTOFilter.getPageSize();

        TypedQuery<Comment> commentTypedQuery = em.createQuery(sql.toString(), Comment.class);

        // Set param to query
        params.forEach((k, v) -> {
            commentTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        commentTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        commentTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Comment> comments = commentTypedQuery.getResultList();

        Long totalComment = (Long) countQuery.getSingleResult();
        Long totalPage = totalComment / pageSize;
        if (totalComment % pageSize != 0) {
            totalPage++;
        }


        List<CommentReportDTOResponse> commentReportDTOResponses =
                comments.stream().map(ct -> ReportCommentMapper.toCommentReportDTOResponse(ct, s3Util)).toList();

        return PagingDTOResponse.builder()
                .totalElement(totalComment)
                .totalPage(totalPage)
                .data(commentReportDTOResponses)
                .build();
    }
}
