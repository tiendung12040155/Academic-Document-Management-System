package com.example.ADMS.model.mapper;


import com.example.ADMS.entity.Comment;
import com.example.ADMS.entity.ReportComment;
import com.example.ADMS.model.dto.response.CommentReportDTOResponse;
import com.example.ADMS.model.dto.response.ReportCommentDTOResponse;
import com.example.ADMS.utils.S3Util;

import java.util.List;

public class ReportCommentMapper {
    public static List<ReportCommentDTOResponse> toReportCommentDTOResponseList(List<ReportComment> reportCommentList, S3Util s3Util) {
        return reportCommentList.stream()
                .map(reportComment -> ReportCommentDTOResponse.builder()
                        .reporter(UserMapper.toUserDTOResponse(reportComment.getReporter(), s3Util))
                        .createdAt(reportComment.getCreatedAt())
                        .id(reportComment.getId())
                        .message(reportComment.getMessage())
                        .build())
                .toList();
    }

    public static CommentReportDTOResponse toCommentReportDTOResponse(Comment comment, S3Util s3Util) {
        return CommentReportDTOResponse.builder()
                .resourceId(comment.getResource().getId())
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .active(comment.getActive())
                .commenter(comment.getCommenter().getUsername())
                .reportCommentDTOResponses(toReportCommentDTOResponseList(comment.getReportCommentList(), s3Util))
                .build();
    }
}
