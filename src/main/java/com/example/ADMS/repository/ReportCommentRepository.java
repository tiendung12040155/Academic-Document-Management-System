package com.example.ADMS.repository;

import com.example.ADMS.entity.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {

    @Query("select rc from ReportComment rc where rc.reporter.id = ?1 and rc.active = true and rc.comment.commentId = ?2")
    Optional<ReportComment> findByReporterIdActive(Long reportId, Long commentId);
}
