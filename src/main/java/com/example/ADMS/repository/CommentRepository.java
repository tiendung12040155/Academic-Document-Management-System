package com.example.ADMS.repository;

import com.example.ADMS.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.resource.id = :resourceId and c.commentRoot.commentId is null and c.active = true order by c.createdAt asc")
    List<Comment> findByResourceIdAndCommentRootIdIsNull(Long resourceId);

    @Query("select count(c) from Comment c where c.commentRoot.commentId = :commentId and c.resource.id = :resourceId and c.active = true")
    Long countReplyComments(Long commentId, Long resourceId);

    @Query("select c from Comment c where c.commentRoot.commentId = :id and c.active = true order by c.createdAt asc")
    List<Comment> findAllCommentReply(Long id);

    @Query("select c from Comment c where c.commentRoot.commentId = :id and c.active = true")
    List<Comment> findCommentByCommentRootId(Long id);
}
