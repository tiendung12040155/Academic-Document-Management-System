package com.example.ADMS.model.dto.request;

import com.example.ADMS.model.BaseFilter;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTOFilter extends BaseFilter {
    String content;
}
