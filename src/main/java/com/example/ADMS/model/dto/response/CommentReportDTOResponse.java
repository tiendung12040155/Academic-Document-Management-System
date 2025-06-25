package com.example.ADMS.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentReportDTOResponse {
    Long resourceId;
    Long commentId;
    LocalDateTime createdAt;
    Boolean active;
    String content;
    String commenter;
    List<ReportCommentDTOResponse> reportCommentDTOResponses;
}
