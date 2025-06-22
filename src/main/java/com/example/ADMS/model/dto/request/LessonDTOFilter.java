package com.example.ADMS.model.dto.request;

import com.example.ADMS.model.BaseFilter;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonDTOFilter extends BaseFilter {
    String name = "";
    Boolean active;
}
