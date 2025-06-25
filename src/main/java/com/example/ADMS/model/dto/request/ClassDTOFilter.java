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
public class ClassDTOFilter extends BaseFilter {
    String name = "";
    Boolean active;
}
