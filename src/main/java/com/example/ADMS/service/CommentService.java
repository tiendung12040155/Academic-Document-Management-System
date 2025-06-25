package com.example.ADMS.service;


import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.CommentDTOFilter;
import com.example.ADMS.model.dto.request.CommentDTORequest;
import com.example.ADMS.model.dto.response.CommentDTOResponse;
import com.example.ADMS.model.dto.response.CommentDetailDTOResponse;
import com.example.ADMS.model.dto.response.CommentReportDetailDTOResponse;

import java.util.List;

public interface CommentService {
    CommentDTOResponse createComment(CommentDTORequest request);

    List<CommentDetailDTOResponse> getListCommentDetailDTOResponse(Long resourceId);

    List<CommentDTOResponse> seeMoreReplyComment(Long id);

    CommentReportDetailDTOResponse getReportDetailComment(Long id);

    Boolean changeStatus(Long id);

    PagingDTOResponse searchComment(CommentDTOFilter commentDTOFilter);
}
