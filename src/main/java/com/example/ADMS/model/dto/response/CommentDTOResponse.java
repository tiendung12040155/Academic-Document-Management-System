package com.example.ADMS.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTOResponse {
    Long commentId;
    LocalDateTime createdAt;
    Boolean active;
    String content;
    UserDTOResponse commenterDTOResponse;
    Long commentRootId;
}
