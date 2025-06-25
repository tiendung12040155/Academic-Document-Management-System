package com.example.ADMS.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDetailDTOResponse {
    CommentDTOResponse commentDTOResponse;
    Long numberOfReplyComments;
}
