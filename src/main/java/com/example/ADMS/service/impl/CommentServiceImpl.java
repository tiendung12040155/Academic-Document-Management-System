package com.example.ADMS.service.impl;

import com.example.ADMS.entity.Comment;
import com.example.ADMS.entity.ReportComment;
import com.example.ADMS.entity.Resource;
import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.CommentDTOFilter;
import com.example.ADMS.model.dto.request.CommentDTORequest;
import com.example.ADMS.model.dto.response.CommentDTOResponse;
import com.example.ADMS.model.dto.response.CommentDetailDTOResponse;
import com.example.ADMS.model.dto.response.CommentReportDetailDTOResponse;
import com.example.ADMS.model.mapper.CommentMapper;
import com.example.ADMS.repository.CommentRepository;
import com.example.ADMS.repository.ReportCommentRepository;
import com.example.ADMS.repository.ResourceRepository;
import com.example.ADMS.repository.criteria.CommentCriteria;
import com.example.ADMS.service.CommentService;
import com.example.ADMS.utils.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    ResourceRepository resourceRepository;
    MessageException messageException;
    UserHelper userHelper;
    S3Util s3Util;
    CommentCriteria commentCriteria;
    ReportCommentRepository reportCommentRepository;

    @Override
    public CommentDTOResponse createComment(CommentDTORequest request) {
        User commenter = userHelper.getUserLogin();
        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));

        Comment commentRoot = null;
        if (request.getCommentRootId() != null) {
            commentRoot = commentRepository.findById(request.getCommentRootId())
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_COMMENT_ROOT_ID_NOT_FOUND));
        }

        if (FileHelper.checkContentInputValid(request.getContent()))
            throw ApiException.badRequestException(messageException.MSG_TEXT_NO_STANDARD_WORD);

        Comment comment = Comment.builder()
                .commenter(commenter)
                .resource(resource)
                .content(request.getContent())
                .createdAt(DateTimeHelper.getTimeNow())
                .active(true)
                .commentRoot(commentRoot)
                .build();
        comment = commentRepository.save(comment);

        return CommentMapper.toCommentDTOResponse(comment, s3Util);
    }

    @Override
    public List<CommentDetailDTOResponse> getListCommentDetailDTOResponse(Long resourceId) {
        List<Comment> listCommentRoot = commentRepository.findByResourceIdAndCommentRootIdIsNull(resourceId);
        return listCommentRoot.stream()
                .map(comment -> {
                    CommentDTOResponse commentDTOResponse = CommentMapper.toCommentDTOResponse(comment, s3Util);
                    Long countReplyComments = commentRepository.countReplyComments(commentDTOResponse.getCommentId(), resourceId);
                    return CommentDetailDTOResponse.builder()
                            .commentDTOResponse(commentDTOResponse)
                            .numberOfReplyComments(countReplyComments)
                            .build();
                }).toList();
    }

    @Override
    public List<CommentDTOResponse> seeMoreReplyComment(Long id) {
        return commentRepository
                .findAllCommentReply(id).stream()
                .map(comment -> CommentMapper.toCommentDTOResponse(comment, s3Util))
                .toList();
    }

    @Override
    public CommentReportDetailDTOResponse getReportDetailComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_COMMENT_NOT_FOUND));
        return CommentMapper.toCommentReportDetailDTOResponse(comment, s3Util);
    }


    @Override
    public Boolean changeStatus(Long id) {
        User userLoggedIn = userHelper.getUserLogin();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> ApiException.notFoundException("Comment is not found"));

        if (DataHelper.isRole(userLoggedIn, 2L) || comment.getCommenter() == userLoggedIn) {
            comment.setActive(false);
        } else {
            throw ApiException.forBiddenException("Comment can not remove");
        }

        List<Comment> comments = commentRepository.findCommentByCommentRootId(id);
        if (!comments.isEmpty()) {
            comments = comments.stream()
                    .peek(c -> {
                        c.setActive(false);
                        List<ReportComment> reportComments = c.getReportCommentList();
                        if (!reportComments.isEmpty()) {
                            reportComments = reportComments.stream()
                                    .peek(rp -> rp.setActive(false))
                                    .toList();
                            reportCommentRepository.saveAll(reportComments);
                        }
                    })
                    .toList();
            commentRepository.saveAll(comments);
        }
        commentRepository.save(comment);
        return true;
    }

    @Override
    public PagingDTOResponse searchComment(CommentDTOFilter commentDTOFilter) {
        return commentCriteria.searchComment(commentDTOFilter);
    }
}
