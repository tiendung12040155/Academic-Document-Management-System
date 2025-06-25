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
public class CommentReportDetailDTOResponse {
    Long commentId;
    String avatar;
    LocalDateTime createdAt;
    String content;
    String fullName;
    String name;
    String linkResource;
    List<UserReportComment> userReportComments;
}
