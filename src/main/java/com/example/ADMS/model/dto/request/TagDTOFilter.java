package com.example.ADMS.model.dto.request;

import com.example.ADMS.utils.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagDTOFilter {
    Long pageIndex = Constants.DEFAULT_PAGE_INDEX;
    Long pageSize = Constants.DEFAULT_TAG_PAGE_SIZE;
    String name = "";
    Boolean active;
}
