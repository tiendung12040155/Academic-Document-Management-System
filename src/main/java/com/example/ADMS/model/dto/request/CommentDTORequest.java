package com.example.ADMS.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTORequest {

    @NotNull(message = "Resource is mandatory")
    Long resourceId;

    Long commentRootId;

    @NotBlank(message = "Content is mandatory")
    String content;
}
