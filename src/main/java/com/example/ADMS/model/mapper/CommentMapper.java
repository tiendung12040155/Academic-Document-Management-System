package com.example.ADMS.model.mapper;
import com.example.ADMS.entity.Comment;
import com.example.ADMS.entity.ReportComment;
import com.example.ADMS.model.dto.response.CommentDTOResponse;
import com.example.ADMS.model.dto.response.CommentReportDetailDTOResponse;
import com.example.ADMS.model.dto.response.UserReportComment;
import com.example.ADMS.utils.DataHelper;
import com.example.ADMS.utils.S3Util;

import java.util.List;

import static com.example.ADMS.utils.Constants.API_VERSION;
import static com.example.ADMS.utils.Constants.HOST;

public class CommentMapper {
    public static CommentDTOResponse toCommentDTOResponse(Comment comment, S3Util s3Util) {
        Long commentIdRoot = null;
        if (comment.getCommentRoot() != null) {
            commentIdRoot = comment.getCommentRoot().getCommentId();
        }
        return CommentDTOResponse.builder()
                .commentId(comment.getCommentId())
                .commenterDTOResponse(UserMapper.toUserDTOResponse(comment.getCommenter(), s3Util))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .active(comment.getActive())
                .commentRootId(commentIdRoot)
                .build();
    }

    public static UserReportComment toUserReportComment(ReportComment reportComment, S3Util s3Util) {
        Comment comment = reportComment.getComment();
        String fullName = comment.getCommenter().getFirstname() + " " + comment.getCommenter().getLastname();
        return UserReportComment.builder()
                .fullName(fullName)
                .content(reportComment.getMessage())
                .avatar(DataHelper.getLinkAvatar(comment.getCommenter(), s3Util))
                .build();
    }

    public static CommentReportDetailDTOResponse toCommentReportDetailDTOResponse(Comment comment, S3Util s3Util) {
        String fullName = comment.getCommenter().getFirstname() + " " + comment.getCommenter().getLastname();
        List<UserReportComment> userReportComments = comment.getReportCommentList().stream()
                .map(urc -> CommentMapper.toUserReportComment(urc, s3Util))
                .toList();
        return CommentReportDetailDTOResponse.builder()
                .commentId(comment.getCommentId())
                .name(comment.getResource().getName())
                .fullName(fullName)
                .avatar(DataHelper.getLinkAvatar(comment.getCommenter(), s3Util))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .linkResource(HOST + API_VERSION + "/resource/detail/" + comment.getResource().getId())
                .userReportComments(userReportComments)
                .build();
    }
}
